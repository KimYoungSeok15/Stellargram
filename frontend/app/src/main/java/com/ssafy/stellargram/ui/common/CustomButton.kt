package com.ssafy.stellargram.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.stellargram.ui.theme.Constant
import com.ssafy.stellargram.ui.theme.Purple40
import com.ssafy.stellargram.ui.theme.Purple80

@Composable
fun CustomTextButton(
    text: String,
//    textSize: TextSizeType = TextSizeType.SMALL,
    isBold: Boolean = false,
    onClick: () -> Unit,
    margin: Dp = 0.dp,
) {

    Column(
        modifier = Modifier
            .padding(margin)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color.Black),
                onClick = onClick
            )
            .clip(
                RoundedCornerShape(
                    Constant.boxCornerSize.dp
                )
            )
            .background(Purple80)
    ) {
        Text(
            text = text, color = Color.Black, style = TextStyle(
                fontWeight = if (isBold == true) FontWeight.Bold else FontWeight.Normal
            ), modifier = Modifier.padding(vertical = 12.dp, horizontal = 22.dp)
        )
    }

}

@Composable
fun CustomTextButtonDark(
    text: String,
//    textSize: TextSizeType = TextSizeType.SMALL,
    isBold: Boolean = false,
    onClick: () -> Unit,
    margin: Dp = 0.dp,
) {

    Column(
        modifier = Modifier
            .padding(margin)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color.Black),
                onClick = onClick
            )
            .clip(
                RoundedCornerShape(
                    Constant.boxCornerSize.dp
                )
            )
            .background(Purple40)
    ) {
        Text(
            text = text, color = Color.White, style = TextStyle(
                fontWeight = if (isBold == true) FontWeight.Bold else FontWeight.Normal
            ), modifier = Modifier.padding(vertical = 12.dp, horizontal = 22.dp)
        )
    }

}

@Composable
fun CustomButtonLined(
    text: String,
    isBold: Boolean = false,
    onClick: () -> Unit,
    margin: Dp = 0.dp,
    isSelected:Boolean=false,
){
    Column(
        modifier = Modifier
            .padding(4.dp)
            .clip(
                RoundedCornerShape(
                    Constant.boxCornerSize.dp
                )
            )
            .border(
                width = 2.dp, Purple80, shape = RoundedCornerShape(Constant.boxCornerSize.dp)
            )
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color.Black),
                onClick = onClick
            )
            .background(if(isSelected) Purple40 else Color.Unspecified),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text, color = Color.White, style = TextStyle(
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
            ), modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
        )
    }

}