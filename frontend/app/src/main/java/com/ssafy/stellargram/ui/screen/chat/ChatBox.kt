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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.stellargram.ui.theme.Constant
import com.ssafy.stellargram.ui.common.ProfilePhoto
import com.ssafy.stellargram.ui.theme.Purple80
import com.ssafy.stellargram.ui.theme.Turquoise
import com.ssafy.stellargram.util.TimeUtil

// css. 메세지 전체 컨테이너 modifier
val containerModifier: Modifier = Modifier
    .fillMaxWidth()
    .padding(vertical = 10.dp)

@Preview(showBackground = true)
@Composable
fun ChatBox(
    isMine: Boolean = false,
    imgUrl: String = "",
    nickname: String = "유저의 닉네임",
    content: String = "메세지 내용",
    unixTimestamp: Long = 1699489600,
) {
    // 타임스탬프 파싱
    var parsedYearMonth = TimeUtil.getYearMonth(unixTimestamp = unixTimestamp)
    var parsedHourMinute = TimeUtil.getHourMinute(unixTimestamp = unixTimestamp)

    //내 메세지면 오른쪽붙이기
    if (isMine) MyChat(
        imgUrl = imgUrl,
        nickname = nickname,
        content = content,
        yearMonth = parsedYearMonth,
        hourMinute = parsedHourMinute,
        isMine = isMine
    )
    //남 메세지면 왼쪽붙이기
    else YourChat(
        imgUrl = imgUrl,
        nickname = nickname,
        content = content,
        yearMonth = parsedYearMonth,
        hourMinute = parsedHourMinute,
        isMine = isMine
    )
}

@Composable
fun MyChat(
    imgUrl: String,
    nickname: String,
    content: String,
    yearMonth: String,
    hourMinute: String,
    isMine: Boolean
) {
    // 컨테이너 렌더링
    Row(
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
        modifier = containerModifier
    ) {

        TimeBox(yearMonth = yearMonth, hourMinute = hourMinute)

        // css. 메세지 박스 modifier
        val messageBoxModifier: Modifier = Modifier
            .weight(1f)
            .clip(
                RoundedCornerShape(
                    topStart = if (isMine) Constant.boxCornerSize.dp else 0.dp,
                    topEnd = if (isMine) 0.dp else Constant.boxCornerSize.dp,
                    bottomStart = Constant.boxCornerSize.dp,
                    bottomEnd = Constant.boxCornerSize.dp
                )
            )
            .background(if (isMine) Turquoise else Purple80)
            .padding(10.dp)
// 메세지 박스 렌더링
        Column(
            modifier = messageBoxModifier

        ) {
            Row {
                ProfilePhoto(imgUrl = imgUrl)

                Text(
                    text = nickname,
                    style = TextStyle(fontSize = Constant.middleText.sp),
                    color = Color.Black,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Text(
                text = content,
                style = TextStyle(fontSize = Constant.middleText.sp),
                color = Color.Black
            )
        }
    }
}

@Composable
fun YourChat(
    imgUrl: String,
    nickname: String,
    content: String,
    yearMonth: String,
    hourMinute: String,
    isMine: Boolean
) {
    // 컨테이너 렌더링
    Row(
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
        modifier = containerModifier
    ) {

        // css. 메세지 박스 modifier
        val messageBoxModifier: Modifier = Modifier
            .weight(1f)
            .clip(
                RoundedCornerShape(
                    topStart = if (isMine) Constant.boxCornerSize.dp else 0.dp,
                    topEnd = if (isMine) 0.dp else Constant.boxCornerSize.dp,
                    bottomStart = Constant.boxCornerSize.dp,
                    bottomEnd = Constant.boxCornerSize.dp
                )
            )
            .background(if (isMine) Turquoise else Purple80)
            .padding(10.dp)
// 메세지 박스 렌더링
        Column(
            modifier = messageBoxModifier

        ) {
            Row {
                ProfilePhoto(imgUrl = imgUrl)

                Text(
                    text = nickname,
                    style = TextStyle(fontSize = Constant.middleText.sp),
                    color = Color.Black,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Text(
                text = content,
                style = TextStyle(fontSize = Constant.middleText.sp),
                color = Color.Black
            )
        }
        TimeBox(yearMonth = yearMonth, hourMinute = hourMinute)
    }
}

@Composable
fun MessageBox(imgUrl: String, nickname: String, content: String, isMine: Boolean) {

}

@Composable
fun TimeBox(yearMonth: String, hourMinute: String) {
    // css .시간 Modifier
    val timeModifier: Modifier = Modifier.padding(horizontal = 10.dp)


    //시간 렌더링
    Column(modifier = timeModifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = yearMonth,
            style = TextStyle(fontSize = Constant.smallText.sp),
            color = Color.Black
        )
        Text(
            text = hourMinute,
            style = TextStyle(fontSize = Constant.smallText.sp),
            color = Color.Black
        )
    }
}
