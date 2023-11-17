package com.ssafy.stellargram.ui.screen.identify

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropperLoading
import com.mr0xf00.easycrop.rememberImagePicker
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import com.ssafy.stellargram.model.IdentifyStarInfo
import com.ssafy.stellargram.ui.common.CustomSpinner
import com.ssafy.stellargram.ui.common.CustomTextButton
import com.ssafy.stellargram.ui.screen.chat.ChatBox
import com.ssafy.stellargram.ui.screen.chat.TestValue
import com.ssafy.stellargram.ui.theme.Constant
import com.ssafy.stellargram.ui.theme.EasyCropTheme
import com.ssafy.stellargram.ui.theme.Purple40
import com.ssafy.stellargram.ui.theme.Purple80


@OptIn(ExperimentalPermissionsApi::class, ExperimentalGlideComposeApi::class)
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
                .weight(0.4f)
                .padding(vertical = 10.dp)
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(Constant.boxCornerSize.dp)
                )
                .border(
                    width = 2.dp, Purple80, shape = RoundedCornerShape(Constant.boxCornerSize.dp)
                )

                .background(if (selectedImage == null) Color.DarkGray else Color.Unspecified),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (selectedImage != null) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    if (selectedImage.width > selectedImage.height) {
                        Image(
                            bitmap = selectedImage,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Image(
                            bitmap = selectedImage,
                            contentDescription = null,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                    if (viewModel.isIdentifying) {
                        CustomSpinner()
                    }
                }


            } else {
                Text("사진을 선택해주세요")
            }
        }
        Column(
            modifier = Modifier.weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "장애물이 없고 화각이 30도에 가까울수록",
                color = Color.White,
                style = TextStyle(fontSize = Constant.verySmallText.sp)
            )
            Text(
                text = "인식률이 올라갑니다",
                color = Color.White,
                style = TextStyle(fontSize = Constant.verySmallText.sp)
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextButton(
                    text = "사진 고르기",
                    onClick = onPick,
                    margin = 10.dp,
                    isBold = false
                )
                CustomTextButton(
                    text = "별 인식하기",
                    onClick = {

                        if (selectedImage != null) viewModel.identifyStarsFromBitmap(
                            selectedImage
                        )
                        else Toast.makeText(context, "사진을 선택해주세요", Toast.LENGTH_SHORT)
                    },
                    margin = 10.dp,
                    isBold = false
                )
//                Button(
//                    onClick = onPick, modifier = Modifier.padding(10.dp)
//                ) { Text("사진 고르기") }
//                Button(
//                    onClick =, modifier = Modifier.padding(10.dp)
//                ) {
//                    Text(text = "별 인식하기")
//                }
            }

            if (viewModel.isFailed) {
                Text(
                    text = "인식에 실패하였습니다",
                    color = Color.White,
                    style = TextStyle(fontSize = Constant.smallText.sp)
                )
            } else {
                // TODO: 인식된 별 리스트 표시하기
                Divider(thickness = 4.dp, color = Purple80)

                val templist = listOf(1, 2, 3,4,5,6)
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                ) {

                    itemsIndexed(templist) { index, card ->
                        IdentifyCard()
                        if(index!=templist.lastIndex)
                            Divider(thickness = 2.dp, color = Purple40)

                    }
                }
                Divider(thickness = 4.dp, color = Purple80)

            }
        }
    }
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

