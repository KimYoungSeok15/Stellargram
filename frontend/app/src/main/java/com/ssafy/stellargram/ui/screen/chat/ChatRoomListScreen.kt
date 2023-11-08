package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ssafy.stellargram.data.remote.ApiServiceForChat
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.CardsData
import com.ssafy.stellargram.model.ChatRoom
import com.ssafy.stellargram.model.ChatRoomsData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//@Preview(showBackground = true)
@Composable
fun ChatRoomListScreen(navController: NavController = rememberNavController()) {
    var roomCount by remember { mutableIntStateOf(0) }
    var roomList by remember { mutableStateOf<List<ChatRoom>>(emptyList()) }
    // TODO: 나의 아이디 임시 입력
    val myId: Int = 1

//    val roomListInfo = GetMyRooms()
    LaunchedEffect(key1 = true) {
        // API 호출을 비동기로 수행
        val response = NetworkModule.provideRetrofitInstanceChat().getRoomList(myId = myId)
        if (response?.code == 200) {
            roomCount = response.data.roomCount
            roomList = response.data.roomList
        } else {
            null
        }
    }

    Column {
        LazyColumn() {
            items(roomList) { room ->
                ChatRoomCard(
                    roomId = room.roomId,
                    personnel = room.personnel,
                    observeSite = room.observeSiteId,
                    navController = navController
                )
            }
        }

        Text(text = "현재 참여한 방 "+roomCount.toString()+"개")
    }


}

