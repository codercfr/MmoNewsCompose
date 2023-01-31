package com.example.mmonewscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mmonews.mmodetail.MmoDetailScreen
import com.example.mmonews.mmolist.MmoListScreen
import com.example.mmonews.ui.theme.MmoNewsTheme
import com.example.mmonewscompose.nav.DrawerBody
import com.example.mmonewscompose.nav.DrawerHeader
import com.example.mmonewscompose.nav.MenuItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MmoNewsTheme {
                NavHost(
                    navController = navController,
                    startDestination = "mmo_list_screen"
                ) {
                    composable("mmo_list_screen") {
                        MmoListScreen(navController = navController)
                    }
                    composable(
                        "mmo_detail_screen/{dominantColor}/{mmoName}",
                        arguments = listOf(
                            navArgument("dominantColor") {
                                type = NavType.IntType
                            },
                            navArgument("mmoName") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) } ?: Color.White
                        }
                        val mmoName = remember {
                            it.arguments?.getString("mmoName")
                        }
                        if (mmoName != null) {
                            MmoDetailScreen(
                                dominant = dominantColor,
                                id= mmoName.toInt(),
                                navController= navController

                            )
                        }
                    }
                }
            }
        }
    }
}