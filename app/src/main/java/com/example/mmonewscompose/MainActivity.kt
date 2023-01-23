package com.example.mmonewscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mmonews.mmodetail.MmoDetailScreen
import com.example.mmonews.mmolist.MmoListScreen
import com.example.mmonews.ui.theme.MmoNewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MmoNewsTheme {
                val navController = rememberNavController()
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