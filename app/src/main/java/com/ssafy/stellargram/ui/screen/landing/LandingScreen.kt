package com.ssafy.stellargram.ui.screen.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ssafy.stellargram.R

@Composable
fun LandingScreen(navController: NavController){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
    ){
        Image(
            painter =
            painterResource(id = R.drawable.stellargram),
            contentDescription = "LOGO",
            modifier = Modifier

        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter =
            painterResource(id = R.drawable.kakao_login_large_narrow),
            contentDescription = null,
            modifier = Modifier
                .clickable { navController.navigate("kakao") }
        )
    }

}