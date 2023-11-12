package com.ssafy.stellargram.ui.screen.camera

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.R
import com.ssafy.stellargram.ui.screen.camera.CameraActivity

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
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForGallery {
        Log.d("SelectedImage", it.toString())
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
                .width(400.dp)
                .height(300.dp)
                .border(width = 1.dp, Color.Black, shape = RoundedCornerShape(20.dp))
                .clickable {
                    val intent = Intent(context, CameraActivity::class.java)
                    context.startActivity(intent)
                }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GlideImage(
                    model = R.drawable.icon_camera,
                    contentDescription = "카메라 애니메이션 아이콘",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(200.dp)
                )
                Text(
                    text = "촬영하기",
                    fontSize = 24.sp,
                    color = Color.Black,
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
                .width(400.dp)
                .height(300.dp)
                .border(width = 1.dp, Color.Black, shape = RoundedCornerShape(20.dp))
                .clickable {
                    galleryLauncher.launch(null)
                }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GlideImage(
                    model = R.drawable.icon_gallery,
                    contentDescription = "그림 애니메이션 아이콘",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(200.dp)
                )
                Text(
                    text = "불러오기",
                    fontSize = 24.sp,
                    color = Color.Black,
                )
            }
        }
    }

}

@Composable
fun rememberLauncherForGallery(onResult: (Uri) -> Unit): ManagedActivityResultLauncher<String, Uri?> {
    return rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { onResult(it) }
    }
}

