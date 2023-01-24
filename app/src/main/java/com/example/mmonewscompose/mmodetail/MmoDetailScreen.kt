package com.example.mmonews.mmodetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.mmonews.data.remote.response.ResponseApiItem
import com.example.mmonews.data.remote.response.ResponseId
import com.example.mmonews.data.remote.response.Screenshot
import com.example.mmonews.util.Ressource
import java.util.*
import kotlin.math.round

@Composable
fun MmoDetailScreen(
    dominant:androidx.compose.ui.graphics.Color,
    id:Int,
    navController: NavController,
    topPadding: Dp = 20.dp,
    mmoImageSize: Dp = 200.dp,
    viewModel: MmoDetailViewModel = hiltViewModel()
) {
    //need to indicate witch type of data the loading get in <>
    val mmoInfo = produceState<Ressource<ResponseId>>(initialValue = Ressource.Loading()){
        value = viewModel.getMmmoInfo(id)
    }.value // refer to the ressource not the state
    Box(modifier = Modifier
        .fillMaxSize()
        .background(dominant)
        .padding(bottom = 16.dp)
    ){
        MmoDetailTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
        MmoDetailStateWraper(
            mmoInfo =mmoInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + mmoImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
        )

        Box(contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()){
            if(mmoInfo is Ressource.Succes){
                mmoInfo.data?.thumbnail?.let {
                    SubcomposeAsyncImage(
                        model = it,
                        contentDescription = mmoInfo.data.title,
                        modifier = Modifier
                            .size(mmoImageSize)
                            .offset(y = topPadding)
                    )
                }
            }
        }

    }

}


@Composable
fun MmoDetailTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
    ){
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(10.dp, 10.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}


@Composable
fun MmoDetailStateWraper(
    mmoInfo: Ressource<ResponseId>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when(mmoInfo){
        is Ressource.Succes->{
            MmoDetailSection(
                mmoInfo =mmoInfo.data!! ,
                modifier = modifier
                    .offset(y=(-20).dp)
            )
        } is Ressource.Error->{
        Text(
            text = mmoInfo.data.toString(),
            color = androidx.compose.ui.graphics.Color.Red,
            modifier = modifier
        )

    } is Ressource.Loading->{
        CircularProgressIndicator(
            color = MaterialTheme.colors.primary,
            modifier = loadingModifier
        )
    }
    }
}

@Composable
fun MmoDetailSection(
    mmoInfo:ResponseId,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val pattern = "<[^>]*>".toRegex()
    val cleanedText = pattern.replace(mmoInfo.short_description, "")
    val spacedText = cleanedText.replace(".", "")
   /* val spacedRequirements=mmoInfo.minimum_system_requirements.toString()
    val requerementsToString = spacedRequirements.replace(",",",\n")*/
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "${mmoInfo.platform}",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = spacedText,
            modifier = Modifier.padding(16.dp),
            style = TextStyle(textAlign = TextAlign.Left),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black
                        )
                    )
                )
        ) {
            Text(
                text = "Minimum System Requirements ",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface,
            )
            Text(
                text = mmoInfo.minimum_system_requirements.graphics,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface,
                style = TextStyle(fontSize = 15.sp),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            for (i in mmoInfo.screenshots.indices) {
                val model = mmoInfo.screenshots[i].image
                AsyncImage(
                    model = model,
                    contentDescription = "screenshots",
                    modifier = Modifier
                        .width(100.dp)
                        .align(alignment = CenterVertically)
                )
                if (i != mmoInfo.screenshots.size - 1) {
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
        }
    }
/*@Composable
fun ListScreenShots(
    mmoInfo: ResponseId,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (i in mmoInfo.screenshots.indices) {
            val model = mmoInfo.screenshots[i].image
            AsyncImage(
                model = model,
                contentDescription = "screenshots",
                modifier = Modifier
                    .size(200.dp)
            )
        }
    }
}*/

@Composable
fun MmoDetailDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(painter = dataIcon, contentDescription = null, tint = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit",
            color = Color.Black

        )
    }
}
