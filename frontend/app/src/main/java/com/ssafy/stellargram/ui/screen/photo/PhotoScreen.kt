package com.ssafy.stellargram.ui.screen.photo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ssafy.stellargram.R
import com.ssafy.stellargram.ui.theme.Constant
import com.ssafy.stellargram.ui.theme.Purple80


@Composable
fun PhotoScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MoveButton(navController)
    }
}

@Composable
fun MoveButton(navController: NavController) {
    val modifier = Modifier

    val boxModifier =

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = modifier
        ) {
            Box(
                modifier =  Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .width(250.dp)
                    .height(150.dp)
                    .clip(
                        RoundedCornerShape(Constant.boxCornerSize.dp)
                    )
                    .border(
                        width = 2.dp,
                        Purple80,
                        shape = RoundedCornerShape(Constant.boxCornerSize.dp)
                    )
                    .background(Color.DarkGray)
                    .clickable {
                        navController.navigate("cameranew")
                    }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter =
                        painterResource(id = R.drawable.cameraicon),
                        contentDescription = "촬영하기",
                        modifier = Modifier
                            .size(70.dp)
                    )
                    Text(
                        text = "촬영하기",
                        fontSize = 24.sp,
                        color = Color.White,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .width(250.dp)
                    .height(150.dp)
                    .clip(
                        RoundedCornerShape(Constant.boxCornerSize.dp)
                    )
                    .border(
                        width = 2.dp,
                        Purple80,
                        shape = RoundedCornerShape(Constant.boxCornerSize.dp)
                    )
                    .background(Color.DarkGray)
                    .clickable {
                        navController.navigate("makecard")
                    }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter =
                        painterResource(id = R.drawable.writing),
                        contentDescription = "카드 만들기",
                        modifier = Modifier
                            .size(70.dp)

                    )
                    Text(
                        text = "카드 만들기",
                        fontSize = 24.sp,
                        color = Color.White,
                    )
                }
            }
            Box(
                modifier =  Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .width(250.dp)
                    .height(150.dp)
                    .clip(
                        RoundedCornerShape(Constant.boxCornerSize.dp)
                    )
                    .border(
                        width = 2.dp,
                        Purple80,
                        shape = RoundedCornerShape(Constant.boxCornerSize.dp)
                    )
                    .background(Color.DarkGray)
                    .clickable {
                        navController.navigate("identify")
                    }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter =
                        painterResource(id = R.drawable.identify),
                        contentDescription = "사진 인식하기",
                        modifier = Modifier
                            .size(70.dp)
                    )
                    Text(
                        text = "사진 인식하기",
                        fontSize = 24.sp,
                        color = Color.White,
                    )
                }
            }
        }
}
