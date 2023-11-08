package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ssafy.stellargram.R
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.ChatRoom
import com.ssafy.stellargram.model.MessageInfo
import com.ssafy.stellargram.ui.theme.PurpleGrey40
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun ChatRoomScreen(
    navController: NavController = rememberNavController(),
    roomId: Int? = 2,
    personnel: Int? = 0,
    observeSiteId: String? = ""
) {
    // TODO: 나의 아이디 임시 입력
    val myId: Int = 1

    // 채팅 뷰모델 생성
    val viewModel: ChatViewModel = hiltViewModel()

    // 다음 커서
    var nextCursor: Int = 0
    // TODO: 메세지 리스트 업데이트 방법 찾을 것
    var messageList: List<MessageInfo> by remember { mutableStateOf<List<MessageInfo>>(viewModel.messages) }

    // 뷰모델에 채팅방 정보 세팅
    viewModel.setRoomInfo(
        newInfo = ChatRoom(
            roomId = roomId ?: -1, personnel = personnel ?: 0, observeSiteId = observeSiteId ?: ""
        )
    )
    // 뷰모델을 통해 메세지 가져오기
    LaunchedEffect(key1 = true) {
        // 최초 실행시 메세지 가져오기
        nextCursor = viewModel.getMessages(nextCursor) ?: -1
    }

    // 상수
    val inputBoxCornerSize = 10

    // 입력박스 내 메세지 내용
    var messageContent by remember { mutableStateOf("") }

    // css. 메세지 입력박스 modifier
    var inputModifier: Modifier = Modifier
        .clip(
            RoundedCornerShape(
                inputBoxCornerSize.dp
//        topStart = if (isMine) inputBoxCornerSize.dp else 0.dp,
//        topEnd = if (isMine) 0.dp else inputBoxCornerSize.dp,
//        bottomStart = inputBoxCornerSize.dp,
//        bottomEnd = inputBoxCornerSize.dp
            )
        )
        .border(width = 5.dp, color = PurpleGrey40)

    Column {
        // 메세지들 컨테이너
        LazyColumn() {
            items(messageList) { message ->
                ChatBox(
                    isMine = (myId == TestValue.myId),
                    imgUrl = message.memberImagePath,
                    nickname = message.memberNickName,
                    content = message.content
                )
            }
        }

//        Column {
//            ChatBox(
//                isMine = (myId == 1),
//                imgUrl = "https://cdn.dailycc.net/news/photo/202308/753330_655480_558.jpg",
//                nickname = "후라이",
//                content = "이 관측소 빛공해는 어느 정도 인가요?"
//            )
//            ChatBox(isMine = (myId == 2), nickname = "쿠크다스", content = "주변에 전광판이 많아서 좀 있는 편이에요")
//        }

        // 메세지 입력 필드
        Row {
            // 입력 필드
            BasicTextField(
                value = messageContent,
                onValueChange = { messageContent = it },
                modifier = inputModifier
            )
            // 전송 버튼
            Button(
                onClick = { /*TODO*/ },
            ) {
//                Text(text = "전송")
                Image(
                    painter = painterResource(id = R.drawable.send_violet),
                    contentDescription = "no image", // 이미지 설명
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        // TODO: 테스트용. 나중에 지울 것
        Text(text = messageContent)

        // TODO: 테스트용. 나중에 지울 것
        Text(text = "방 번호: " + roomId.toString())
    }

}