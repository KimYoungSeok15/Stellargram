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
    object Landing : Screen("landing", "")
    object Kakao : Screen("kakao", "")
    object Home : Screen("home", "메인", R.drawable.home_page)
    object Search : Screen("search", "")
    object StarDetail : Screen("stardetail", "별 정보")
    object Example : Screen("example", "")
    object GoogleMap : Screen("googlemap", "지도", R.drawable.address)
    object MyPage : Screen("mypage", "마이페이지", R.drawable.account)
    object SkyMap : Screen("skymap", "천구", R.drawable.constellation)
    object SignUp : Screen("signup", "회원가입")
    object Camera : Screen("camera", "사진", R.drawable.google_images)
    object ChatRoom : Screen("chatroom","채팅")
    object ChatRoomList : Screen("chatroomlist","채팅방목록")
}

@Composable
fun rememberAppNavigationController(): NavHostController {
    return rememberNavController()
}