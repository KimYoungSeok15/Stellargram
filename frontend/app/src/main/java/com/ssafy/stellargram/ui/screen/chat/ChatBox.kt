package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.stellargram.R


@Preview(showBackground = true)
@Composable
fun ChatBox(
    imgUrl : String = "url",
    nickname : String = "유저의 닉네임",
    content : String = "메세지 내용"
) {

    val textSize = 22
    Column(Modifier.padding(10.dp)) {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.account_active),
                contentDescription = "no Profile Image",
                Modifier.size(22.dp)
            )
            Text(
                text = nickname,
//                style = TextStyle(fontSize = textSize.sp),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Text(text = content,
//            style = TextStyle(fontSize = textSize.sp)
        )
    }


}