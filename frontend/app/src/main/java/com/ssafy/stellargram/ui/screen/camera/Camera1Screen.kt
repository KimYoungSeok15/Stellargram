package com.ssafy.stellargram.ui.screen.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.stellargram.R

@Composable
fun Camera1Screen(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter =
            painterResource(id = R.drawable.construction),
            contentDescription = "공사중",
            Modifier.padding(10.dp),
        )
        Text(
            text = "Sorry! 아직 공사중" +
                    "\n\n"+
                    "촬영하기 Tip\n\n" +
                    "보다 선명한 별 사진 촬영을 위해\n카메라 프로 설정에서\n다음과 같이 값을 설정해주세요:\n\n" +
                    "ISO: 400\n" +
                    "White balance: 7600 K\n" +
                    "Shutter Speed: 15s",
            fontSize = 24.sp,
            color = Color.White,
        )
    }
}
