package com.ssafy.stellargram.ui.screen.identify

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.stellargram.R
import com.ssafy.stellargram.model.IdentifyStarInfo
import com.ssafy.stellargram.ui.theme.Constant
import com.ssafy.stellargram.ui.theme.Purple80

@Composable //info:IdentifyStarInfo
fun IdentifyCard(info: IdentifyStarInfo, index: Int, selectedIndex: Int, onClickCard: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color.Black),
            ) {
                onClickCard()
            }
            .background(if (index == selectedIndex) Color.DarkGray else Color.Unspecified)
            .padding(4.dp)

    ) {
        Image(
            // TODO: 이거 그냥 노란별로 바꾸고싶다
            painterResource(id = R.drawable.star),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Column(
            modifier = Modifier

                .padding(horizontal = 4.dp)
        ) {
            Text(text = info.name ?: "이름 없는 별")
            Row {
                Text(text = "겉보기 등급 : ")
                Text(text = info.mag.toString())
            }
            Row {
                Text(text = "절대 등급 : ")
                Text(text = info.absmag.toString())
            }
            Row {
                Text(text = "별자리 구역 : ")
                Text(text = info.con)
            }
        }
    }


}

