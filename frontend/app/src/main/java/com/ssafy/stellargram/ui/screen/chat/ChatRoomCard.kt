package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ssafy.stellargram.R
import com.ssafy.stellargram.model.CombinedChatRoom
import com.ssafy.stellargram.ui.Screen
import com.ssafy.stellargram.ui.rememberAppNavigationController

@Composable
fun ChatRoomCard(
    roomInfo: CombinedChatRoom,
    navController: NavController
) {
//    var navController: NavController = rememberAppNavigationController()

//    var roomCardModifier: Modifier =
//        Modifier
//            .fillMaxWidth()
//            .height(40.dp)
//            .clickable { onClickCard(roomId, personnel, observeSiteId, navController) }
//    Button(onClick = { navController.navigate(route = Screen.ChatRoom.route + "/${roomId}") }) {

    Row {
        Image(
            painterResource(id = com.kakao.sdk.friend.R.drawable.kakao_sdk_ico_chattype_openchat),
            contentDescription = "채팅 아이콘",
            modifier = Modifier.size(24.dp)
        )
        Column(modifier = Modifier) {
            Row {
                Text(text = if (roomInfo.siteName.isNullOrBlank()) "이름 없음" else roomInfo.siteName!!)
                Text(text = roomInfo.personnel.toString() + "명")
            }
            Row {
                Text(text = "위도 ")
                Text(text = roomInfo.latitude.toString())
            }
            Row {
                Text(text = "경도 ")
                Text(text = roomInfo.longitude.toString())
            }
        }
    }


//    }


}

fun onClickCard(
    roomId: Int,
    personnel: Int,
    observeSiteId: String, navController: NavController
) {
    navController.navigate(route = Screen.ChatRoom.route + "/${roomId}/${personnel}/${observeSiteId}")
}