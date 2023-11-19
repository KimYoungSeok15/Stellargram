package com.ssafy.stellargram.ui.screen.camera

import android.app.Activity
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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.R
import com.ssafy.stellargram.ui.Screen


@Composable
fun CameraScreen(navController: NavController) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GifImage(navController, selectedImageUri) {
                updatedUri -> selectedImageUri = updatedUri
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GifImage(navController: NavController, selectedImageUri: Uri?, onImageSelected: (Uri) -> Unit) {
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForGallery(selectedImageUri) {
        Log.d("SelectedImage", it.toString())
        onImageSelected(it)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .border(width = 1.dp, Color.White, shape = RoundedCornerShape(20.dp))
                .background(Color.White)
                .width(400.dp)
                .height(300.dp)
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
                    if (ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        != ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        )
                    } else {
                        galleryLauncher.launch("image/*")
                    }
                }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if(selectedImageUri == null) {
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
                } else {
                    DisplaySelectedimage(selectedImageUri!!)
                    Button(
                        onClick = {
//                            navController.navigate(Screen.Camera.route)
                        }
                    ) {
                        Text(text = "저장하기")
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DisplaySelectedimage(selectedImageUri: Uri) {
    GlideImage(
        model = selectedImageUri,
        contentDescription = "Selected Image",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(200.dp)
            .background(Color.Gray)
    )
}

@Composable
fun rememberLauncherForGallery(selectedImageUri: Uri?, onResult: (Uri) -> Unit): ManagedActivityResultLauncher<String, Uri?> {
    return rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            onResult(it)
        }
    }
}

const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
