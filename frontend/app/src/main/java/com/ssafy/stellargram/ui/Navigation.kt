package com.ssafy.stellargram.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssafy.stellargram.R

sealed class Screen(
    val route: String = "",
    val title: String = "",
    val icon: Int = 0
) {
    object Landing : Screen("landing")
    object Kakao : Screen("kakao")
    object Home : Screen("home", "메인", R.drawable.home_page)
    object Example : Screen("example")
    object GoogleMap : Screen("googlemap", "지도", R.drawable.address)
    object MyPage : Screen("mypage", "마이페이지", R.drawable.account)
    object SkyMap : Screen("skymap", "천구", R.drawable.constellation)
    object CameraX : Screen("camerax", "카메라X", R.drawable.google_images)

}

@Composable
fun rememberAppNavigationController(): NavHostController {
    return rememberNavController()
}