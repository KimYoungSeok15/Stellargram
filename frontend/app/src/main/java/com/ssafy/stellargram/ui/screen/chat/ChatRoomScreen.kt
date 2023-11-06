package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    Column {
        Text(text = "test", modifier=Modifier.padding(50.dp))
        ChatBox(nickname = "더하다 우엉차", content = "이 관측소 빛공해는 어느 정도 인가요?")
        ChatBox(nickname = "쿠크다스", content = "주변에 전광판이 많아서 좀 있는 편이에요")

    }
}