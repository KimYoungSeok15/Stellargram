package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.stellargram.ui.common.ProfilePhoto
import com.ssafy.stellargram.ui.theme.Purple80
import com.ssafy.stellargram.ui.theme.Turquoise
import com.ssafy.stellargram.util.TimeUtil

const val textSize = 22

@Preview(showBackground = true)
@Composable
fun ChatBox(
    isMine: Boolean = false,
    imgUrl: String = "",
    nickname: String = "유저의 닉네임",
    content: String = "메세지 내용",
    unixTimestamp: Long = 1699489600
) {
    // 타임스탬프 파싱
    unixTimestamp
    var parsedYearMonth = TimeUtil.getYearMonth(unixTimestamp = unixTimestamp)
    var parsedHourMinute = TimeUtil.getHourMinute(unixTimestamp = unixTimestamp)

    // css. 메세지 전체 컨테이너 modifier
    val containerModifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp)

    // 컨테이너 렌더링
    Row(
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
        modifier = containerModifier
    ) {
        //내 것이라면 여기에 시간 렌더링
        if (isMine)
            MyChat(
                imgUrl = imgUrl,
                nickname = nickname,
                content = content,
                yearMonth = parsedYearMonth,
                hourMinute = parsedHourMinute
            )
        else
            YourChat(
                imgUrl = imgUrl,
                nickname = nickname,
                content = content,
                yearMonth = parsedYearMonth,
                hourMinute = parsedHourMinute
            )
    }
}

@Composable
fun MyChat(
    imgUrl: String,
    nickname: String,
    content: String,
    yearMonth: String,
    hourMinute: String
) {
    TimeBox(yearMonth = yearMonth, hourMinute = hourMinute)
    MessageBox(imgUrl = imgUrl, nickname = nickname, content = content, isMine = true)
}

@Composable
fun YourChat(
    imgUrl: String,
    nickname: String,
    content: String,
    yearMonth: String,
    hourMinute: String
) {
    MessageBox(imgUrl = imgUrl, nickname = nickname, content = content, isMine = false)
    TimeBox(yearMonth = yearMonth, hourMinute = hourMinute)
}

@Composable
fun MessageBox(imgUrl: String, nickname: String, content: String, isMine: Boolean) {
    // 코너 사이즈
    val messageBoxCornerSize = 10

    // css. 메세지 박스 modifier
    val messageBoxModifier: Modifier =
        Modifier
//            .fillMaxWidth(0.8f)
//            .width(intrinsicSize = IntrinsicSize.Min)
            .clip(
                RoundedCornerShape(
                    topStart = if (isMine) messageBoxCornerSize.dp else 0.dp,
                    topEnd = if (isMine) 0.dp else messageBoxCornerSize.dp,
                    bottomStart = messageBoxCornerSize.dp,
                    bottomEnd = messageBoxCornerSize.dp
                )
            )
            .background(if (isMine) Turquoise else Purple80)

//            .border(
//                width = 0.dp, color = Color.Black, shape = RoundedCornerShape(10.dp)
//            )
            .padding(10.dp)
// 메세지 박스 렌더링
    Column(
        modifier = messageBoxModifier

    ) {
        Row {
            ProfilePhoto(imgUrl = imgUrl)

            Text(
                text = nickname,
                style = TextStyle(fontSize = textSize.sp),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Text(
            text = content,
            style = TextStyle(fontSize = textSize.sp)
        )
    }
}

@Composable
fun TimeBox(yearMonth: String, hourMinute: String) {
    // css .시간 Modifier
    val timeModifier: Modifier = Modifier
        .padding(horizontal = 10.dp)


    //시간 렌더링
    Column(modifier = timeModifier) {
        Text(text = yearMonth, style = TextStyle(fontSize = (textSize - 5).sp))
        Text(text = hourMinute, style = TextStyle(fontSize = (textSize - 5).sp))
    }
}