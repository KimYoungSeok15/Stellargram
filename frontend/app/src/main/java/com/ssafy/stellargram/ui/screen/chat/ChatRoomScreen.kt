package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.ssafy.stellargram.model.ChatRoom
import com.ssafy.stellargram.model.MessageInfo
import com.ssafy.stellargram.ui.theme.PurpleGrey40

// 상수
const val inputBoxCornerSize = 10

@Preview(showBackground = true)
@Composable
fun ChatRoomScreen(
    navController: NavController = rememberNavController(),
    roomId: Int = 1,
    personnel: Int? = 0,
    observeSiteId: String? = ""
) {

    // 채팅 뷰모델 생성
    val viewModel: ChatViewModel = hiltViewModel()

    // 스톰프 저장용
//    val stomp = StompUtil.getStompConnection()

    // 채팅방 열 때 실행
    LaunchedEffect(key1 = true) {
        // 뷰모델에 채팅방 정보 세팅
        viewModel.setRoomInfo(
            newInfo = ChatRoom(
                roomId = roomId ?: -1,
                personnel = personnel ?: 0,
                observeSiteId = observeSiteId ?: ""
            )
        )
        viewModel.getLastCursor()

        // 스톰프로 웹소켓 연결
        viewModel.makeConnect()
        // 최초 실행시 메세지 가져오고 다음커서 세팅하기
        viewModel.getMessages()
    }
    // TODO: 메세지 리스트 업데이트 방법 찾을 것
    var messageList: List<MessageInfo> by remember { mutableStateOf<List<MessageInfo>>(viewModel.messages) }


    // 스크린 전체
    Column(modifier = Modifier, verticalArrangement = Arrangement.SpaceAround) {
        // 스크롤 상태 기억
        val scrollState = rememberScrollState()

        // 메세지들 컨테이너
        LazyColumn(modifier = Modifier.height(700.dp)) {
            items(messageList) { message ->
                ChatBox(
                    isMine = (message.memberId == TestValue.myId),
                    imgUrl = message.memberImagePath,
                    nickname = message.memberNickName,
                    content = message.content,
                    unixTimestamp = message.time
                )
            }
        }
        MessageInput(viewModel = viewModel, roomId = roomId)

        // TODO: 테스트용. 나중에 지울 것
        Text(text = "방 번호: " + roomId.toString())
    }

}

@Composable
fun MessageInput(viewModel: ChatViewModel, roomId: Int) {

    // 입력박스 내 메세지 내용
    var messageContent by remember { mutableStateOf("") }

    // css. 메세지 입력박스 modifier
    var inputModifier: Modifier = Modifier
        .fillMaxWidth(0.8f)
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
            onClick = {
                viewModel.publishToChannel(
                    roomId = roomId,
                    messageContent = messageContent
                )
                messageContent = ""
            },
            modifier = Modifier.width(200.dp)
        ) {
//                Text(text = "전송")
            Image(
                painter = painterResource(id = R.drawable.send_violet),
                contentDescription = "no image", // 이미지 설명
                modifier = Modifier.size(36.dp)
            )
        }
    }
}