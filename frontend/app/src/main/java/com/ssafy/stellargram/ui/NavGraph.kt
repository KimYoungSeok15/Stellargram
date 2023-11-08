package com.ssafy.stellargram.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ssafy.stellargram.ui.screen.example.ExampleScreen
import com.ssafy.stellargram.ui.screen.googlemap.GoogleMapScreen
import com.ssafy.stellargram.ui.screen.kakao.KakaoScreen
import com.ssafy.stellargram.ui.screen.landing.LandingScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberAppNavigationController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Landing.route,
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
            ExampleScreen(navController = navController, modifier = modifier)
        }
        composable(route = Screen.GoogleMap.route){
            GoogleMapScreen(navController = navController)
        }

    }
}


@Composable
fun HomeScreen(navController: NavController) {
    Button(onClick = { navController.navigate(Screen.GoogleMap.route) }) {
        Text(text = "Google Map")
    }
}

//@Composable
//fun ExampleScreen(navController: NavController) {
//    Button(onClick = { navController.navigate("Home") }) {
//        Text(text = "Example")
//    }
//}



