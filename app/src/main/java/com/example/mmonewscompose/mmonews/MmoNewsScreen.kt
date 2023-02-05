package com.example.mmonewscompose.mmonews

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.mmonews.R
import com.example.mmonews.mmolist.*
import com.example.mmonewscompose.MainActivity
import com.example.mmonewscompose.mmolist.nav.AppBar
import com.example.mmonewscompose.mmolist.nav.DrawerBody
import com.example.mmonewscompose.mmolist.nav.DrawerHeader
import com.example.mmonewscompose.mmolist.nav.MenuItem
import com.example.mmonewscompose.remote.models.NewsListEntry
import kotlinx.coroutines.launch

@Composable
fun ListNewsScreen(
    navController: NavController
) {
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
                            icon = ContextCompat.getDrawable(context, R.drawable.home)!!
                        ),
                        MenuItem(
                            id = "settings",
                            title = "Settings",
                            contentDescription = "Go to settings",
                            icon = ContextCompat.getDrawable(context, R.drawable.settings)!!
                        ),
                        MenuItem(
                            id = "mmoNews",
                            title = "mmoNews ",
                            contentDescription = "Go to MmoNews",
                            icon = ContextCompat.getDrawable(context, R.drawable.news)!!
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
            ListNews()
        }
    }
}

@Composable
fun ListNews(
    viewModel: MmoNewsViewModel = hiltViewModel()
) {
    val mmoListNews by remember {
        viewModel.mmoList
    }

    val loadError by remember {
        viewModel.loadError
    }
    val isLoading by remember {
        viewModel.isLoading
    }
    val endReached by remember {
        viewModel.endReached
    }

    // reyclerview in jetPack
    LazyColumn(contentPadding = PaddingValues(15.dp)){
            val itemCount= mmoListNews.size
            items(itemCount){
                if( it >= itemCount - 1&& !endReached && !isLoading){
                    viewModel.loadMmoNews()
                }
                MmoRowNews(rowIndex = it, entries = mmoListNews)
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
                viewModel.loadMmoNews()
            }
        }
    }
}

@Composable
fun MmoEntryNews(
    entry:NewsListEntry,
){
    val defaultDominanColor = MaterialTheme.colors.surface
    Box(
        contentAlignment= Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.LightGray,
                        defaultDominanColor,
                        Color.Black
                    )
                )
            )
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(entry.main_image)
                    .crossfade(true)
                    .build(),
                contentDescription = "news image",
                loading = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary, modifier = Modifier.scale(0.5F)
                    )
                },
                success = { success ->
                    SubcomposeAsyncImageContent()
                },
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.Top)

            )
            Column() {
                Text(
                    text = entry.title,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = entry.short,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}



@Composable
fun MmoRowNews(
    rowIndex:Int,
    entries: List<NewsListEntry>,
){
    Column() {
        Row(

        ) {
                MmoEntryNews(
                    entry = entries[rowIndex]
                    )
            }
        }
    }



