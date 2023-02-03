package com.example.mmonews.mmolist

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.mmonews.data.remote.models.MmoListEntry
import com.example.mmonewscompose.MainActivity
import com.example.mmonewscompose.mmolist.nav.AppBar
import com.example.mmonewscompose.mmolist.nav.DrawerBody
import com.example.mmonewscompose.mmolist.nav.DrawerHeader
import com.example.mmonewscompose.mmolist.nav.MenuItem
import kotlinx.coroutines.launch



@Composable
fun MmoListScreen(
    navController: NavController,
    viewModel: MmoListViewModel= hiltViewModel()
){
    //root elements for screen
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            val context = LocalContext.current
            var isDrawerOpen by remember {
                mutableStateOf(false)
            }

            Scaffold(
                scaffoldState = scaffoldState,
                modifier = Modifier
                    .then(if (isDrawerOpen) Modifier.fillMaxSize() else Modifier.height(50.dp)),
                topBar = {
                    AppBar(

                        onNavigationIconClick = {
                            scope.launch {
                                if (isDrawerOpen) {
                                    scaffoldState.drawerState.close()
                                    isDrawerOpen = false
                                } else {
                                    scaffoldState.drawerState.open()
                                    isDrawerOpen = true
                                }
                            }
                        }

                    )
                },
                drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
                drawerContent = {
                    DrawerHeader()
                    DrawerBody(items = listOf(
                        MenuItem(
                            id = "home",
                            title = "Home",
                            contentDescription = "Go to home screen",
                            icon = Icons.Default.Home
                        ),
                        MenuItem(
                            id = "settings",
                            title = "Settings",
                            contentDescription = "Go to settings",
                            icon = Icons.Default.Settings
                        ),
                        MenuItem(
                            id = "mmoNews",
                            title = "mmoNews ",
                            contentDescription = "Go to MmoNews",
                            icon = Icons.Default.Info
                        ),

                        ),
                        onItemClick = {
                            if (it.id == "mmoNews") {
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                            }
                        }
                    )
                },
            ) {

            }
            if (scaffoldState.drawerState.isClosed){
                isDrawerOpen = false
            }

            Spacer(modifier = Modifier.heightIn(50.dp))
            SearchBar(
                hint="Search ",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)

            ){
                viewModel.searchMmoList(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            MmoList(navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint:String ="",
    //fonction lambda qui prend une string et retourn Unit
    onSearch:(String)->Unit={}
){
    var text by remember {
        mutableStateOf("")
    }
    var isHintisDisplayed by remember {
        mutableStateOf(hint!="")
    }
    Box(modifier = modifier){
        BasicTextField(
            value =text ,
            onValueChange ={
                text=it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                // errase the hint once someone tap something
                .onFocusChanged {
                    isHintisDisplayed = it.isFocused != true && text.isNotEmpty()
                }
        )
        if(isHintisDisplayed){
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier .padding(horizontal = 20.dp, vertical = 12.dp))
        }
    }
}


@Composable
fun MmoList(
    navController: NavController,
    viewModel: MmoListViewModel= hiltViewModel()
){
    val mmoList by remember {
        viewModel.mmoList
    }
    val endReached by remember {
        viewModel.endReached
    }
    val loadError by remember {
        viewModel.loadError
    }
    val isLoading by remember {
        viewModel.isLoading
    }
    val isSearching by remember {
        viewModel.isSearching
    }
    // reyclerview in jetPack
    LazyColumn(contentPadding = PaddingValues(16.dp)){
        val itemCount = if(mmoList.size % 2 ==0){
            mmoList.size/2
        }else{
            mmoList.size/2 +1
        }
        items(itemCount){
            if( it >= itemCount - 1&& !endReached && !isLoading&& !isSearching){
                viewModel.loadMmoPage()
            }
            MmoRow(rowIndex = it, entries = mmoList, navController = navController )
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
        if(isLoading){
            CircularProgressIndicator()
        }
        if(loadError.isNotEmpty()){
            RetrySection(error = loadError) {
                viewModel.loadMmoPage()
            }
        }
    }
}


@Composable
fun MmoEntry(
    entry:MmoListEntry,
    navController: NavController,
    modifier: Modifier=Modifier,
    viewModel: MmoListViewModel = hiltViewModel()
){
    val defaultDominanColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominanColor)
    }

    Box(
        contentAlignment= Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Red,
                        defaultDominanColor
                    )
                )
            )
            .clickable {
                navController.navigate(
                    "mmo_detail_screen/${dominantColor.toArgb()}/${entry.id}"
                )
            }
    ){
        Column() {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(entry.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "game_name",
                loading = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary, modifier = Modifier.scale(0.5F)
                    )
                },
                success = { success ->
                    SubcomposeAsyncImageContent()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
            )

        }
        Text(
            text =entry.name,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter))
    }
}



@Composable
fun MmoRow(
    rowIndex:Int,
    entries: List<MmoListEntry>,
    navController: NavController
){
    Column() {
        Row() {
            MmoEntry(
                entry =entries[rowIndex * 2 ] ,
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            //si c'est le cas on a encore 2 entrées donc on sait qu'on peut encore ajouter 2
            if(entries.size >=rowIndex * 2 + 2 ){
                MmoEntry(
                    entry = entries[rowIndex *2 +1],
                    navController=navController,
                    modifier = Modifier.weight(1f)
                )
            }else
            {
                Spacer(modifier =Modifier.weight(1f))
            }
        }
        Spacer(modifier =  Modifier.height(16.dp))
    }
}




@Composable
fun RetrySection(
    error: String,
    onRetry: ()->Unit
){
    Column() {
        Text(error, color = Color.Red, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onRetry },
            modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Retry")
        }

    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = "mmo_list_screen"){
        composable("mmo_list_screen"){
            MmoListScreen(navController = navController)
        }
    }
}

