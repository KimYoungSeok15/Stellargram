package com.ssafy.stellargram.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ssafy.stellargram.ui.screen.base.BaseFrame
import com.ssafy.stellargram.ui.screen.camera.CameraScreen
import com.ssafy.stellargram.ui.screen.chat.ChatRoomListScreen
import com.ssafy.stellargram.ui.screen.chat.ChatRoomScreen
import com.ssafy.stellargram.ui.screen.googlemap.GoogleMapScreen
import com.ssafy.stellargram.ui.screen.home.HomeScreen
import com.ssafy.stellargram.ui.screen.kakao.KakaoScreen
import com.ssafy.stellargram.ui.screen.landing.LandingScreen
import com.ssafy.stellargram.ui.screen.mypage.MypageScreen
import com.ssafy.stellargram.ui.screen.search.SearchScreen
import com.ssafy.stellargram.ui.screen.signup.SignUpScreen
import com.ssafy.stellargram.ui.screen.skymap.SkyMapScreen
import com.ssafy.stellargram.ui.screen.stardetail.StarDetailScreen

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAppNavigationController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Landing.route,
        modifier = modifier
    ) {
        composable(route = Screen.Landing.route) {
            LandingScreen(navController = navController)
        }
        composable(route = Screen.Kakao.route) {
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
        composable(
            route = "${Screen.StarDetail.route}/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val starName = arguments.getString("name") ?: ""

            BaseFrame(navController, screen = Screen.StarDetail) {
                StarDetailScreen(navController = navController, id = 1)
            }
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
        composable(route = Screen.GoogleMap.route){
            BaseFrame(navController, screen = Screen.GoogleMap) {
                GoogleMapScreen(navController = navController)
            }
        }
        composable(Screen.SignUp.route){
            SignUpScreen(navController = navController)
        }
        composable(route = Screen.MyPage.route){
            BaseFrame(navController, screen = Screen.MyPage) {
                MypageScreen(navController = navController)
            }
        }
        composable(
            route = Screen.ChatRoom.route + "/{roomId}/{personnel}/{observeSiteId}",
            arguments = listOf(
                navArgument("roomId") { type = NavType.IntType },
                navArgument("personnel") { type = NavType.IntType },
                navArgument("observeSiteId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.let {
                ChatRoomScreen(
                    navController = navController,
                    roomId = it.getInt("roomId"),
                    personnel = backStackEntry.arguments?.getInt("personnel"),
                    observeSiteId = backStackEntry.arguments?.getString("observeSiteId"),
                )
            }
        }
        composable(route = Screen.ChatRoomList.route) {
            BaseFrame(navController, screen = Screen.ChatRoomList) {
                ChatRoomListScreen(navController = navController)
            }
        }
    }
}

