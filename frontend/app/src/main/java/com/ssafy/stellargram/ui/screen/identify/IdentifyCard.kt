package com.ssafy.stellargram.ui.screen.identify

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.stellargram.R
import com.ssafy.stellargram.model.IdentifyStarInfo
@Preview
@Composable //info:IdentifyStarInfo
fun IdentifyCard (){
    Column(modifier = Modifier.fillMaxWidth()) {

        Row {
            Image(
                // TODO: 이거 그냥 노란별로 바꾸고싶다
                painterResource(id = R.drawable.star),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(text = "별 이름")
        }
        Row {
            Column {
                Row {
                    Text(text = "겉보기 등급 : ")
                    Text(text = "4 등급")
                }
                Row {
                    Text(text = "절대 등급 : ")
                    Text(text = "4 등급")
                }
                Row {
                    Text(text = "별자리 구역 : ")
                    Text(text = "어디 별자리")
                }
            }
        }


    }

}