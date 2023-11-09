package com.ssafy.stellargram.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ssafy.stellargram.ui.screen.skymap.SkyMapScreen
import com.ssafy.stellargram.ui.screen.camera.CameraScreen
import com.ssafy.stellargram.ui.screen.base.BaseFrame
import com.ssafy.stellargram.ui.screen.googlemap.GoogleMapScreen
import com.ssafy.stellargram.ui.screen.home.HomeScreen
import com.ssafy.stellargram.ui.screen.kakao.KakaoScreen
import com.ssafy.stellargram.ui.screen.chat.ChatRoomScreen
import com.ssafy.stellargram.ui.screen.landing.LandingScreen
import com.ssafy.stellargram.ui.screen.mypage.MypageScreen
import com.ssafy.stellargram.ui.screen.search.SearchScreen
import com.ssafy.stellargram.ui.screen.skymap.SkyMapScreen

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
            BaseFrame(navController, screen = Screen.Home) {
                HomeScreen(navController = navController)
            }
        }
        composable(route = Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(route = Screen.Example.route) {
            SkyMapScreen(navController = navController, modifier = modifier)
        }
        composable(route = Screen.SkyMap.route){
            BaseFrame(navController, screen = Screen.SkyMap) {
                SkyMapScreen(navController = navController)
            }
        }
        composable(route = Screen.Camera.route){
            BaseFrame(navController, screen = Screen.Camera) {
                CameraScreen(navController = navController)
            }
        }
        composable(route = Screen.GoogleMap.route){            BaseFrame(navController, screen = Screen.GoogleMap) {
                GoogleMapScreen(navController = navController)
            }
        }
        composable(route = Screen.MyPage.route){
            BaseFrame(navController, screen = Screen.MyPage) {
                MypageScreen(navController = navController)
            }
        }
        composable(route = Screen.ChatRoom.route){
            ChatRoomScreen(navController = navController)
        }
    }
}

