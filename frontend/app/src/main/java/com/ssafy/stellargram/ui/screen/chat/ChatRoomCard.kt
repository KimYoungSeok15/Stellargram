package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.ssafy.stellargram.ui.theme.Constant
import com.ssafy.stellargram.ui.theme.Purple80

@Composable
fun ChatRoomCard(
    roomInfo: CombinedChatRoom,
    navController: NavController
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(Constant.boxCornerSize.dp))
        .border(
            width = 2.dp,
            color = Purple80,
            shape = RoundedCornerShape(Constant.boxCornerSize.dp)
        )
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(color = Color.DarkGray),
        ) {
            navController.navigate(route = Screen.ChatRoom.route + "/${roomInfo.roomId}/${roomInfo.personnel}/${roomInfo.observeSiteId}")
        }
        .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(id = com.kakao.sdk.friend.R.drawable.kakao_sdk_ico_chattype_openchat),
            contentDescription = "채팅 아이콘",
            modifier = Modifier
                .size(24.dp)
                .fillMaxHeight()
                .padding(horizontal = 4.dp)
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier= Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
                ) {
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
}