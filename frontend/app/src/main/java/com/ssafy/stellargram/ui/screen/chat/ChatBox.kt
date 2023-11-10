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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.stellargram.ui.common.ProfilePhoto
import com.ssafy.stellargram.ui.theme.Purple80
import com.ssafy.stellargram.ui.theme.Turquoise
import com.ssafy.stellargram.util.TimeUtil


@Preview(showBackground = true)
@Composable
fun ChatBox(
    isMine: Boolean = false,
    imgUrl: String? = null,
    nickname: String = "유저의 닉네임",
    content: String = "메세지 내용",
    unixTimestamp: Long = 1699489600
) {
    val textSize = 22
    val messageBoxCornerSize = 10

    // 타임스탬프 파싱
    unixTimestamp
    var parsedYearMonth = TimeUtil.getYearMonth(unixTimestamp = unixTimestamp*1000)
    var parsedHourMinute = TimeUtil.getHourMinute(unixTimestamp = unixTimestamp*1000)

    // css. 메세지 전체 컨테이너 modifier
    val containerModifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp)

    // css. 메세지 박스 modifier
    val messageBoxModifier: Modifier =
        Modifier
            .fillMaxWidth(0.8f)
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

    // css .시간 Modifier
    val timeModifier: Modifier = Modifier

        .padding(vertical = 10.dp)
    // 컨테이너 렌더링
    Row(
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
        modifier = containerModifier
    ) {
        //내 것이라면 여기에 시간 렌더링
        if (isMine)
            Column {
                Text(text = parsedYearMonth)
                Text(text = parsedHourMinute)
            }

        // 메세지 박스 렌더링
        Column(
            modifier = messageBoxModifier

        ) {
            Row {
                ProfilePhoto(imgUrl = imgUrl)

                Text(
                    text = nickname,
//                style = TextStyle(fontSize = textSize.sp),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Text(
                text = content,
//            style = TextStyle(fontSize = textSize.sp)
            )
        }
        //내 것이 아니라면 여기에 시간 렌더링
        if (!isMine)
            Column {
                Text(text = parsedYearMonth)
                Text(text = parsedHourMinute)
            }
    }


}