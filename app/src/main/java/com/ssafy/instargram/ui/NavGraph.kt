package com.ssafy.instargram.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ssafy.instargram.ui.screen.kakao.KakaoScreen
import com.ssafy.instargram.ui.screen.landing.LandingScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberAppNavigationController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route= Screen.Landing.route){
            LandingScreen(navController = navController)
        }
        composable(route= Screen.Kakao.route){
            KakaoScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Example.route) {
            ExampleScreen(navController = navController)
        }

    }
}


@Composable
fun HomeScreen(navController: NavController) {
    Button(onClick = { navController.navigate("Example") }) {
        Text(text = "Home")
    }
}

@Composable
fun ExampleScreen(navController: NavController) {
    Button(onClick = { navController.navigate("Home") }) {
        Text(text = "Example")
    }
}



