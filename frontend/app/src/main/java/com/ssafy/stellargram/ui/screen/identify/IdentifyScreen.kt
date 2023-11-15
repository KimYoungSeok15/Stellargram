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
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.CropperLoading
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.rememberImagePicker
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import com.ssafy.stellargram.model.Constant
import com.ssafy.stellargram.ui.screen.camera.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
import com.ssafy.stellargram.ui.screen.camera.rememberLauncherForGallery
import com.ssafy.stellargram.ui.theme.EasyCropTheme
import com.ssafy.stellargram.ui.theme.Purple80
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.IOException
import kotlin.math.roundToInt


@OptIn(ExperimentalPermissionsApi::class, ExperimentalGlideComposeApi::class)
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun IdentifyScreen(navController: NavController) {
    val viewModel: IdentifyViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val imagePicker = rememberImagePicker(onImage = { uri -> viewModel.setSelectedImage(uri) })

    val cropState = viewModel.imageCropper.cropState
    val loadingStatus = viewModel.imageCropper.loadingStatus
    val selectedImage = viewModel.selectedImage.collectAsState().value
    val onPick = { imagePicker.pick() }


    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 크롭창 활성화
        if (cropState != null) {
            EasyCropTheme(darkTheme = true) {
                ImageCropperDialog(state = cropState)
            }
        }
        // 로딩중 안내메세지
        if (cropState == null && loadingStatus != null) {
            LoadingDialog(status = loadingStatus)
        }

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(Constant.boxCornerSize.dp)
                )
                .border(
                    width = 2.dp,
                    Purple80,
                    shape = RoundedCornerShape(Constant.boxCornerSize.dp)
                )
                .weight(0.5f)
                .background(if (selectedImage == null) Color.DarkGray else Color.Unspecified),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (selectedImage != null) {
                if (selectedImage.width > selectedImage.height) {
                    GlideImage(model = selectedImage, contentDescription = null)
                    Image(
                        bitmap = selectedImage, contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Image(
                        bitmap = selectedImage, contentDescription = null,
                        modifier = Modifier.fillMaxHeight()
                    )
                }


            } else {
                Text("사진을 선택해주세요")
            }
        }
        Column(
            modifier = Modifier.weight(0.5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPick, modifier = Modifier
                        .padding(10.dp)
                ) { Text("사진 고르기") }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Text(text = "별 인식하기")
                }
            }
        }


//        Button(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .padding(16.dp)
//        ) {
//            Text(text = "천체카드 작성하기")
//        }
    }
}

fun onClickSelect() {

}

@Composable
fun CropError.getMessage(): String = remember(this) {
    when (this) {
        CropError.LoadingError -> "Error while opening the image !"
        CropError.SavingError -> "Error while saving the image !"
    }
}


@Composable
fun LoadingDialog(status: CropperLoading) {
    var dismissed by remember(status) { mutableStateOf(false) }
    if (!dismissed) Dialog(onDismissRequest = { dismissed = true }) {
        Surface(
            shape = MaterialTheme.shapes.small
//            ,  elevation = 6.dp
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                CircularProgressIndicator()
                Text(text = status.getMessage())
            }
        }
    }
}

@Composable
fun CropperLoading.getMessage(): String {
    return remember(this) {
        when (this) {
            CropperLoading.PreparingImage -> "Preparing Image"
            CropperLoading.SavingResult -> "Saving Result"
        }
    }
}

