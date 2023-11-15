package com.ssafy.stellargram.ui.screen.makecard

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.ui.screen.camera.rememberLauncherForGallery


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MakeCardScreen(navController: NavController){
    val viewModel: MakeCardViewModel = viewModel()
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver

    var src: Bitmap? = null

    var uri: Uri =
        Uri.parse("content://com.android.providers.media.documents/document/image%3A11364")

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForGallery(selectedImageUri) {
        Log.d("테스트", "uri: ${it.toString()}")
//        val filePath =    getRealPathFromURI(context, it)
        Log.d("테스트", "uri path: ${it.path}" ?: "path 없음")
        selectedImageUri = it

//        ImageDecoder.createSource()
    }
    // selectedImageUri 변경 시 uploadCard 실행
    LaunchedEffect(selectedImageUri) {

        // *** 카드 등록 함수 - uri와 content만 변경하고 나머지는 아래와같이 넣을 것 ***
        viewModel.uploadCard(
            uri= selectedImageUri.toString(),
            content= "테스트",
            photoAt= "",
            category= "galaxy",
            tool= "",
            observeSiteId=""
        )
    }


    val takePhotoFromAlbumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    Log.d("테스트", uri.toString())

                } ?: run {
                    Log.e("테스트", "select fail")
                }
            } else if (result.resultCode != Activity.RESULT_CANCELED) {
                Log.e("테스트", "active fail")
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

    }

}