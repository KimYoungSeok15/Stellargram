package com.ssafy.instargram.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Example : Screen("example")
}

@Composable
fun rememberAppNavigationController(): NavHostController {
    return rememberNavController()
}