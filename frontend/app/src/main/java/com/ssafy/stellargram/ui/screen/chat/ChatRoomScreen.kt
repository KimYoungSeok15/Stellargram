package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@Preview(showBackground = true)
@Composable
fun ChatRoomScreen(
    navController: NavController = rememberNavController(),
) {
    // TODO: 나의 아이디 임시 입력
    val myId: Int = 1
    val roomId: Int = 2

    // 입력할 메세지 내용
    var messageContent by remember { mutableStateOf("") }

    Column {
        // 메세지들 컨테이너
        Column {
            ChatBox(isMine = (myId == 1), nickname = "더하다 우엉차", content = "이 관측소 빛공해는 어느 정도 인가요?")
            ChatBox(isMine = (myId == 2), nickname = "쿠크다스", content = "주변에 전광판이 많아서 좀 있는 편이에요")
        }

        // 메세지 입력
        Row {
            // 입력 필드
            TextField(value = messageContent, onValueChange = { messageContent = it })
            // 전송 버튼
            Button(onClick = { /*TODO*/ },
                ) {
                Text(text = "전송")
            }
        }


        Text(text = messageContent)
    }
}