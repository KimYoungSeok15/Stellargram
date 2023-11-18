package com.ssafy.stellargram.ui.screen.identify

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.ssafy.stellargram.R
import com.ssafy.stellargram.model.IdentifyStarInfo
import com.ssafy.stellargram.ui.common.CustomSpinner
import com.ssafy.stellargram.ui.common.CustomTextButton
import com.ssafy.stellargram.ui.screen.chat.ChatBox
import com.ssafy.stellargram.ui.screen.chat.TestValue
import com.ssafy.stellargram.ui.theme.Constant
import com.ssafy.stellargram.ui.theme.EasyCropTheme
import com.ssafy.stellargram.ui.theme.Purple40
import com.ssafy.stellargram.ui.theme.Purple80


@Composable
fun IdentifyScreen(navController: NavController) {
    val viewModel: IdentifyViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val selectedIndex = viewModel.selectedIndex

    val imagePicker = rememberImagePicker(onImage = { uri -> viewModel.setSelectedImage(uri) })

    val cropState = viewModel.imageCropper.cropState
    val loadingStatus = viewModel.imageCropper.loadingStatus
    val selectedImage = viewModel.selectedImage.collectAsState().value
    val paintedAllImage = viewModel.paintedAllImage.collectAsState().value
    val paintedOneImage = viewModel.paintedImage.collectAsState().value
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

        // 렌더링 시작
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
            // 캡쳐된 사진이 있다면
            if (selectedImage != null) {
                // 사진 박스
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    // 선택된 인덱스가 없다면 원본캡쳐 보이기
                    if (selectedIndex == -2) {
                        Image(
                            bitmap = selectedImage,
                            contentDescription = "캡처된 이미지",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    // 전체 별 보기 선택됐으면
                    if (selectedIndex == -1) {
                        // 아직 생성중이라서 그림이 없다면 스피너
                        if (paintedAllImage == null)
                            CustomSpinner()
                        // 그림이 있다면
                        else
                            Image(
                                bitmap = paintedAllImage!!,
                                contentDescription = "전체 별 선택된 이미지",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                    }

                    // 1개 별 보기 선택됐으면
                    if (selectedIndex >= 0) {
                        // 아직 생성중이라서 그림이 없다면 스피너
                        if (paintedOneImage == null)
                            CustomSpinner()
                        // 그림이 있다면
                        else
                            Image(
                                bitmap = paintedOneImage!!,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                    }

                    // 인식중이라면 스피너
                    if (viewModel.isIdentifying)
                        CustomSpinner()
                }
            }
            // 캡쳐된 사진이 없다면
            else {
                Text("사진을 선택해주세요")
            }
        }
        Column(
            modifier = Modifier.weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 안내문구
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

            // 버튼들
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextButton(
                    text = "사진 고르기", onClick = onPick, margin = 10.dp, isBold = false
                )
                CustomTextButton(
                    text = "별 인식하기", onClick = {

                        if (selectedImage != null) viewModel.identifyStarsFromBitmap(
                            selectedImage
                        )
                        else Toast.makeText(context, "사진을 선택해주세요", Toast.LENGTH_SHORT)
                    }, margin = 10.dp, isBold = false
                )
            }
            Divider(thickness = 4.dp, color = Purple80)
            // 인식된 정보 표시 시작
            if (viewModel.isFailed) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "인식에 실패하였습니다",
                        color = Color.White,
                        style = TextStyle(fontSize = Constant.smallText.sp)
                    )
                }

            } else {
                // 리스트가 비어있지 않다면
                if (viewModel.starInfoList.isNotEmpty()) {
                    // 전체 별 표시 버튼
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(color = Color.Black),
                            ) {
                                // 전체보기가 아닌 상태라면
                                if (selectedIndex != -1) {
                                    viewModel.selectedIndex = -1
                                }
                                // 이미 전체보기 상태라면
                                else {
                                    // 선택 안함 상태로 변경
                                    viewModel.selectedIndex = -2
                                }
                            }
                            .background(if (selectedIndex == -1) Color.DarkGray else Color.Unspecified),
//                        .padding(4.dp)
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "전체 별 표시하기")
                    }
                    Divider(thickness = 2.dp, color = Purple40)
                    // 인식된 별 목록
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        itemsIndexed(viewModel.starInfoList) { index, starInfo ->
                            IdentifyCard(info = starInfo,
                                index = index,
                                selectedIndex = selectedIndex,
                                onClickCard = {
                                    // 선택된 카드가 아니라면 저장 후 이미지 칠하기
                                    if (index != selectedIndex) {
                                        viewModel.selectedIndex = index
                                        viewModel.paintOneStarToNewImage(starInfo)
                                    }
                                    // 이미 선택된 카드라면
                                    else {
                                        // 선택 안함 상태로 변경
                                        viewModel.selectedIndex = -2
                                    }
                                })
                            // 마지막 인덱스가 아니라면 구분선 추가
                            if (index != viewModel.starInfoList.lastIndex)
                                Divider(thickness = 2.dp, color = Purple40)
                        }
                    }
                }

            }
            Divider(thickness = 4.dp, color = Purple80)

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

