package com.ssafy.stellargram.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.stellargram.ui.theme.Constant
import com.ssafy.stellargram.ui.theme.Purple80
import javax.annotation.meta.When

@Composable
fun CustomTextButton(
    text: String,
    textColor: Color?,
    textSize: TextSizeType,
    backgroundColor: Color?,
    isBold: Boolean?,
    onClick: () -> Unit,
    margin: Dp?
) {


    Button(
        onClick = onClick, modifier = Modifier
            .padding(margin ?: 4.dp)
            .clip(RoundedCornerShape(Constant.boxCornerSize.dp))
            .background(backgroundColor ?: Purple80)
            .padding()
    )
    {
        Text(
            text = text,
            color = textColor ?: Color.Black,
            style = TextStyle(
                fontSize = when (textSize) {
                    TextSizeType.TYNY -> Constant.tinyText
                    TextSizeType.VERYSMALL -> Constant.verySmallText
                    TextSizeType.SMALL -> Constant.smallText
                    TextSizeType.MIDDLE -> Constant.middleText
                    TextSizeType.LARGE -> Constant.largeText
                }.sp,
                fontWeight = if (isBold == true) FontWeight.Bold else FontWeight.Normal
            ),
            modifier = Modifier.padding(top = 4.dp)

        )
    }
}

enum class TextSizeType {
    TYNY,
    VERYSMALL,
    SMALL,
    MIDDLE,
    LARGE
}