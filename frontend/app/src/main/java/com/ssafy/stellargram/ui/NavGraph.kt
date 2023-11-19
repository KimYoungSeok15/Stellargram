package com.ssafy.stellargram.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ssafy.stellargram.ui.screen.base.BaseFrame
import com.ssafy.stellargram.ui.screen.camera.Camera1Screen
import com.ssafy.stellargram.ui.screen.camera.CameraScreen
import com.ssafy.stellargram.ui.screen.cameranew.CameraNewScreen
import com.ssafy.stellargram.ui.screen.chat.ChatRoomListScreen
import com.ssafy.stellargram.ui.screen.chat.ChatRoomScreen
import com.ssafy.stellargram.ui.screen.googlemap.GoogleMapScreen
import com.ssafy.stellargram.ui.screen.home.HomeScreen
import com.ssafy.stellargram.ui.screen.identify.IdentifyScreen
import com.ssafy.stellargram.ui.screen.kakao.KakaoScreen
import com.ssafy.stellargram.ui.screen.landing.LandingScreen
import com.ssafy.stellargram.ui.screen.makecard.MakeCardScreen
import com.ssafy.stellargram.ui.screen.mypage.MypageScreen
import com.ssafy.stellargram.ui.screen.photo.PhotoScreen
import com.ssafy.stellargram.ui.screen.search.SearchScreen
import com.ssafy.stellargram.ui.screen.signup.SignUpScreen
import com.ssafy.stellargram.ui.screen.skymap.SkyMapFrame
import com.ssafy.stellargram.ui.screen.skymap.SkyMapScreen
import com.ssafy.stellargram.ui.screen.stardetail.StarDetailScreen

@RequiresApi(Build.VERSION_CODES.P)
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
            route = "${Screen.StarDetail.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val starID = arguments.getInt("id") ?: -1
            BaseFrame(navController, screen = Screen.StarDetail) {
                StarDetailScreen(navController = navController, id = starID)
            }
        }

        composable(route = Screen.SkyMap.route){
            SkyMapFrame(navController, screen = Screen.SkyMap) {
            }
        }
//        composable(route = Screen.Camera.route) {
//            BaseFrame(navController, screen = Screen.Camera) {
//                CameraScreen(navController = navController)
//            }
//        }
        composable(route = Screen.Camera1.route) {
            BaseFrame(navController, screen = Screen.Camera1) {
                Camera1Screen(navController = navController)
            }
        }
        composable(route = Screen.Photo.route) {
            BaseFrame(navController, screen = Screen.Photo) {
                PhotoScreen(navController = navController)
            }
        }
        composable(route = Screen.GoogleMap.route) {
            BaseFrame(navController, screen = Screen.GoogleMap) {
                GoogleMapScreen(navController = navController)
            }
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }
        composable(
            route = "${Screen.MyPage.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) {backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val userId = arguments.getLong("id") ?: -1
            BaseFrame(navController, screen = Screen.MyPage) {
                MypageScreen(navController = navController, id = userId)
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
                BaseFrame(navController, screen = Screen.ChatRoom) {
                    ChatRoomScreen(
                        navController = navController,
                        roomId = it.getInt("roomId"),
                        personnel = backStackEntry.arguments?.getInt("personnel"),
                        observeSiteId = backStackEntry.arguments?.getString("observeSiteId"),
                    )
                }
            }
        }
        composable(route = Screen.ChatRoomList.route) {
            BaseFrame(navController, screen = Screen.ChatRoomList) {
                ChatRoomListScreen(navController = navController)
            }
        }
        composable(route = Screen.Identify.route){
            BaseFrame(navController, screen = Screen.Identify){
                IdentifyScreen(navController = navController)
            }
        }
        composable(route = Screen.MakeCard.route){
            MakeCardScreen(navController = navController)
        }
        composable(route = Screen.CameraNew.route){
            CameraNewScreen(navController = navController)
        }
    }
}

