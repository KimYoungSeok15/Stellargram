package com.ssafy.stellargram.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ssafy.stellargram.ui.screen.example.ExampleScreen
import com.ssafy.stellargram.ui.screen.base.BaseFrame
import com.ssafy.stellargram.ui.screen.googlemap.GoogleMapScreen
import com.ssafy.stellargram.ui.screen.home.HomeScreen
import com.ssafy.stellargram.ui.screen.kakao.KakaoScreen
import com.ssafy.stellargram.ui.screen.landing.LandingScreen
import com.ssafy.stellargram.ui.screen.signup.SignUpScreen

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun  NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAppNavigationController()
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
            BaseFrame(navController) {
                HomeScreen(navController = navController)
            }
        }
        composable(route = Screen.Example.route) {
            ExampleScreen(navController = navController, modifier = modifier)
        }
        composable(route = Screen.GoogleMap.route){
            BaseFrame(navController) {
                GoogleMapScreen(navController = navController)
            }
        }
        composable(Screen.SignUp.route){
            SignUpScreen(navController = navController)
        }
    }
}




//@Composable
//fun ExampleScreen(navController: NavController) {
//    Button(onClick = { navController.navigate("Home") }) {
//        Text(text = "Example")
//    }
//}



