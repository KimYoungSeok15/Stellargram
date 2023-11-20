package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ssafy.stellargram.R
import com.ssafy.stellargram.StellargramApplication
import com.ssafy.stellargram.model.ChatRoom
import com.ssafy.stellargram.ui.theme.Constant
import com.ssafy.stellargram.model.MessageInfo
import com.ssafy.stellargram.ui.theme.Purple40

@Composable
fun ChatRoomScreen(
    navController: NavController = rememberNavController(),
    roomId: Int = 1,
    personnel: Int? = 0,
    observeSiteId: String? = ""
) {
    // 내 아이디 가져오기
    val myId = StellargramApplication.prefs.getString("myId", "").toLong()

    // 위도 경도 파싱
    val latilong = observeSiteId!!.split("-")
    val thisLatitude = latilong[0].toDouble() / 1000
    val thisLongitude = latilong[1].toDouble() / 1000

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
        // 마지막 커서 받아온 후 메세지 받아오기
        viewModel.enterRoom()

        // 스톰프로 웹소켓 연결
        viewModel.makeConnect()

    }

    // 메세지 viewModel에서 가져오는 곳
    var messageList: List<MessageInfo> by remember { mutableStateOf<List<MessageInfo>>(viewModel.messages) }

    //
    val scrollState = rememberLazyListState()
    val isAtTopScroll = !scrollState.canScrollBackward
    val isAtBottomScroll = !scrollState.canScrollForward
    val scrollMax = scrollState.layoutInfo.viewportSize



    LaunchedEffect(isAtBottomScroll) {
        if (isAtBottomScroll) viewModel.getMessages()
    }

    // 스크린 컨테이너
    ScreenContainer(
        isChatScreen = true,
        latitude = thisLatitude,
        longitude = thisLongitude,
        observeSiteId = observeSiteId,
        navController = navController,
        customChild = {
            // 메세지들
            LazyColumn(
                reverseLayout = true,
                state = scrollState,
                modifier = Modifier
                    .weight(1f)
            ) {

                itemsIndexed(messageList) { index, message ->
                    ChatBox(
                        isMine = (message.memberId == myId),
                        imgUrl = message.memberImagePath,
                        nickname = message.memberNickName,
                        content = message.content,
                        unixTimestamp = message.time
                    )
                    if (messageList.size - 1 == index) LaunchedEffect(key1 = true) {
                        if (isAtBottomScroll && isAtTopScroll) viewModel.getMessages()
                    }
                }
            }

            // 입력 박스
            MessageInput(viewModel = viewModel, roomId = roomId)

        })
//        // 스크롤 상태 기억
//        val scrollState = rememberScrollState()

}

// 이미지 입력 박스
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(viewModel: ChatViewModel, roomId: Int) {

    // 입력박스 내 메세지 내용
    var messageContent by remember { mutableStateOf("") }

    // css. 입력 컨테이너 modifier
    var containerModifier: Modifier = Modifier
        .fillMaxWidth()
//        .height(300.dp)
//        .defaultMinSize(minHeight = 150.dp)


    // css. 메세지 입력박스 modifier
    var inputModifier: Modifier = Modifier.fillMaxWidth(0.9f)

    // 메세지 입력 필드
    Column(
        modifier = containerModifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max)
        ) {
            // 입력 필드
            OutlinedTextField(
                value = messageContent,
                onValueChange = { messageContent = it },
                modifier = inputModifier,
                trailingIcon = {
                    IconButton(
                        onClick = { messageContent = "" }, modifier = Modifier.padding(13.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                    }
                },
                singleLine = false,
                maxLines = 3,
                textStyle = TextStyle(fontSize = Constant.middleText.sp, color = Color.Black),
                colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = Purple40)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 전송버튼
            FloatingActionButton(
                onClick = {
                    if (messageContent != "") viewModel.publishToChannel(
                        roomId = roomId, messageContent = messageContent
                    )
                    messageContent = ""
                }, modifier = Modifier.fillMaxHeight()
//                    .height(intrinsicSize = IntrinsicSize.Max)
                , shape = RoundedCornerShape(Constant.boxCornerSize),
                containerColor = Purple40,
                contentColor = Purple40
            ) {
                Image(
                    painter = painterResource(id = R.drawable.send_violet),
                    contentDescription = "no image", // 이미지 설명
                    modifier = Modifier.size(Constant.middleText.dp)
                )
            }
        }
    }
}