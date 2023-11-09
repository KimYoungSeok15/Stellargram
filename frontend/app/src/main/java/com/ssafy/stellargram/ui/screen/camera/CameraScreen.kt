package com.ssafy.stellargram.ui.screen.camera
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.R
import com.ssafy.stellargram.ui.Screen

@Composable
fun CameraScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GifImage(navController)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GifImage(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {
                navController.navigate(Screen.Camera.route)
            },
            modifier = Modifier
                .padding(16.dp)

        ) {
            GlideImage(
                model = R.drawable.icon_camera,
                contentDescription = "<a href=\"https://www.flaticon.com/kr/free-animated-icons/\" title=\"카메라 애니메이션 아이콘\">카메라 애니메이션 아이콘 제작자: Freepik - Flaticon</a>",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "촬영하기",
                fontSize = 24.sp,
                color = Color.Black
            )
        }
        Button(
            onClick = {
                navController.navigate(Screen.Camera.route)
            },
            modifier = Modifier
                .padding(16.dp)
        ) {
            GlideImage(
                model = R.drawable.icon_gallery,
                contentDescription = "<a href=\"https://www.flaticon.com/kr/free-animated-icons/\" title=\"그림 애니메이션 아이콘\">그림 애니메이션 아이콘 제작자: Freepik - Flaticon</a>",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = "불러오기",
                fontSize = 24.sp,
                color = Color.Black
            )
        }

    }

}
