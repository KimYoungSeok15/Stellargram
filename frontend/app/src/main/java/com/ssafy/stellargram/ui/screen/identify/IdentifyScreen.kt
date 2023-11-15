package com.ssafy.stellargram.ui.screen.identify

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ssafy.stellargram.ui.screen.camera.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
import com.ssafy.stellargram.ui.screen.camera.rememberLauncherForGallery
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.math.roundToInt


@OptIn(ExperimentalPermissionsApi::class, ExperimentalGlideComposeApi::class)
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun IdentifyScreen(navController: NavController) {
    val context = LocalContext.current
//    val contentResolver: ContentResolver = context.contentResolver


    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var pixelx by remember { mutableStateOf(0) }
    var pixely by remember { mutableStateOf(0) }

    var testx by remember { mutableStateOf(0) }

    LaunchedEffect(selectedImageUri) {
        if (selectedImageUri != null) {
            var bit: Bitmap = ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    selectedImageUri!!
                )
            )
            Log.d("bitmap test", bit.byteCount.toString())
            pixelx = bit.width
            pixely = bit.height
            testx = bit.density
        }
    }

    val galleryLauncher = rememberLauncherForGallery(selectedImageUri) {
        // TODO: 선택 실패시 에러 처리할 것
        Log.d("SelectedImage", it.toString())
        selectedImageUri = it
    }


    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        if (scale > 1) scale = 1f
        rotation += rotationChange
        offset += offsetChange
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        DraggableTextLowLevel()
        GlideImage(
            model = selectedImageUri,
            contentDescription = "Selected Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(),
        )
        Box(
            modifier = Modifier
                .weight(0.5f)
                .background(Color.Blue)
                .transformable(state = state)

        ) {

            Box(
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .background(Color.White)
                    .fillMaxSize()
            ) {

            }
        }
        Column(modifier = Modifier) {
            Text(text = "scale=${scale}")
            Text(text = "offset.x=${offset.x}")
            Text(text = "offset.y=${offset.y}")
            Text(text = "pixelx=${pixelx}")
            Text(text = "pixely=${pixely}")
            Text(text = "density=${testx}")

        }
//        GlideImage(
//            model = selectedImageUri,
//            contentDescription = "Selected Image",
//            contentScale = ContentScale.Fit,
//            modifier = Modifier
//                .fillMaxWidth()
//        )

        Button(
            onClick = {
                galleryLauncher.launch("image/*")


            },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "사진 고르기")
        }


        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "별 인식하기")
        }


        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "천체카드 작성하기")
        }
    }
}

fun onClickSelect() {

}