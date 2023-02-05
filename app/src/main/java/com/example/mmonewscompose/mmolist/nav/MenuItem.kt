package com.example.mmonewscompose.mmolist.nav

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val id: String,
    val title: String,
    val contentDescription: String,
    val icon: Drawable
)