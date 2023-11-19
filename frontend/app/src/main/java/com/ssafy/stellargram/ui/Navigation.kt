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
//    object Camera : Screen("camera", "사진", R.drawable.google_images)
    object Camera1 : Screen("camera1", "카메라 안내")
    object Photo : Screen("photo", "사진 페이지", R.drawable.google_images)
    object ChatRoom : Screen("chatroom","채팅")
    object ChatRoomList : Screen("chatroomlist","채팅방목록")
    object Identify : Screen("identify", "별 인식")
    object MakeCard : Screen("makecard","천체카드 만들기")
    object CameraNew : Screen("cameranew","카메라X")
}

@Composable
fun rememberAppNavigationController(): NavHostController {
    return rememberNavController()
}