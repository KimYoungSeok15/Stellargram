package com.ssafy.stellargram.ui.screen.chat;

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun SiteInfoBox(
    latitude: Double = 1000.0,
    longitude: Double = 1000.0,
    name: String = "관측소이름",
    reviewCount: Int = -1,
    ratingSum: Int = 0,
    isChatScreen: Boolean = true
) {
    val containerModifier: Modifier = Modifier

    val rating: Double =
        (Math.round((ratingSum.toDouble() / reviewCount.toDouble()) * 10).toDouble()) / 10


    Column(modifier = containerModifier) {
        Text(text = name)
        Row {
            Text(text = rating.toString())
            Text(text = "star rating")
            Text(text = "(${reviewCount})")
            // 채팅스크린이라면 리뷰 버튼
            if (isChatScreen) {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "리뷰")
                }
            }
            // 리뷰스크린이라면 채팅버튼
            else {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "채팅")
                }
            }
        }
    }
}
