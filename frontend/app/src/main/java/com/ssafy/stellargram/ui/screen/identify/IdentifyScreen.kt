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
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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


@OptIn(ExperimentalPermissionsApi::class, ExperimentalGlideComposeApi::class)
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun IdentifyScreen(navController: NavController) {
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver

    var src: Bitmap? = null

    var uri: Uri =
        Uri.parse("content://com.android.providers.media.documents/document/image%3A11364")

//    GlideImage(
//        model = uri,
//        contentDescription = "Selected Image",
//        contentScale = ContentScale.Fit,
//        modifier = Modifier
//            .size(200.dp)
//            .background(Color.Gray)
//    )


    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForGallery(selectedImageUri) {
        Log.d("SelectedImage", it.toString())
//        val filePath =    getRealPathFromURI(context, it)
        Log.d("SelectedImage", it.path ?: "")
        selectedImageUri = it
    }

    val takePhotoFromAlbumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    Log.d("select for identify", uri.toString())

                } ?: run {
                    Log.e("select for identify", "select fail")
                }
            } else if (result.resultCode != Activity.RESULT_CANCELED) {
                Log.e("select for identify", "active fail")
            }
        }


    val takePhotoFromAlbumIntent =
        Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
            putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf("image/jpeg", "image/png", "image/bmp", "image/webp")
            )
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        }

    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
//    var offset by remember { mutableStateOf(Offset.Zero) } TODO
    val state = rememberTransformableState {
            zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
//        offset += offsetChange TODO
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Box(
//            modifier = Modifier
//                .graphicsLayer(
//                    scaleX = scale,
//                    scaleY = scale,
//                    translationX = offset.x,
//                    translationY = offset.y
//                )
//                .transformable(state = state)
//                .background(Color.Blue)
//                .fillMaxSize()
//        ) TODO
        GlideImage(
            model = selectedImageUri,
            contentDescription = "Selected Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
        )

        Button(
            onClick = { galleryLauncher.launch("image/*") },
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

