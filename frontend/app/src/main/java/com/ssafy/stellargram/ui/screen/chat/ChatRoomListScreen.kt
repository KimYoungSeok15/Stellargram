package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ssafy.stellargram.data.remote.ApiServiceForChat
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.CardsData
import com.ssafy.stellargram.model.ChatRoom
import com.ssafy.stellargram.model.ChatRoomsData
import com.ssafy.stellargram.ui.theme.Purple40
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//@Preview(showBackground = true)
@Composable
fun ChatRoomListScreen(navController: NavController = rememberNavController()) {
    // 채팅방 목록 뷰모델 생성
    val viewModel: ChatRoomListViewModel = hiltViewModel()
    val roomCount = viewModel.roomCount
    val rooms = viewModel.rooms

    LaunchedEffect(key1 = true) {
        viewModel.getRoomInfo()
    }

    Column {
        Text(text = "현재 참여한 방 " + roomCount.toString() + "개")
        Divider(thickness = 2.dp, color = Purple40)
        LazyColumn() {
            items(rooms) { room ->
                ChatRoomCard(
                    roomInfo = room,
                    navController = navController
                )
            }
        }

    }


}

