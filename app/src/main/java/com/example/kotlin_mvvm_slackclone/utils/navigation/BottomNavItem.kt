package com.example.kotlin_mvvm_slackclone.utils.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name:String,
    val route:String,
    val icon:Int,
    val iconSelected:Int
)
