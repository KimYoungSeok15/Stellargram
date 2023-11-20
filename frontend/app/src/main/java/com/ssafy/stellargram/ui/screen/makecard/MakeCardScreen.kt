package com.ssafy.stellargram.ui.screen.makecard

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Space
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.R
import com.ssafy.stellargram.ui.common.CustomTextButton
import com.ssafy.stellargram.ui.screen.camera.rememberLauncherForGallery
import com.ssafy.stellargram.ui.theme.Constant
import com.ssafy.stellargram.ui.theme.Purple80


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MakeCardScreen(navController: NavController) {
    val viewModel: MakeCardViewModel = viewModel()


    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var content by remember { mutableStateOf("") }

    val galleryLauncher = rememberLauncherForGallery(selectedImageUri) {
        Log.d("테스트", "uri: ${it.toString()}")
        Log.d("테스트", "uri path: ${it.path}" ?: "path 없음")
        selectedImageUri = it
    }

    var uploadCompleteDialogVisible by remember { mutableStateOf(false) }
    var uploadCardRequested by remember { mutableStateOf(false) }
    var showImageSelectDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uploadCardRequested) {
        if (uploadCardRequested) {
            if (selectedImageUri != null) {
                viewModel.uploadCard(
                    uri = selectedImageUri.toString(),
                    content = content,
                    photoAt = "",
                    category = "galaxy",
                    tool = "",
                    observeSiteId = ""
                )
                // 업로드 완료 다이얼로그 표시
                uploadCompleteDialogVisible = true
            } else {
                // "사진을 선택해주세요" 다이얼로그 표시
                showImageSelectDialog = true
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(20.dp, 40.dp, 20.dp, 0.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .height(440.dp)
                .padding(vertical = 10.dp)
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(Constant.boxCornerSize.dp)
                )
                .border(
                    width = 2.dp, Purple80, shape = RoundedCornerShape(Constant.boxCornerSize.dp)
                )

                .background(if (selectedImageUri == null) Color.DarkGray else Color.Unspecified)
                .clickable {
                    galleryLauncher.launch("image/*")
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (selectedImageUri != null) {
                GlideImage(
                    model = selectedImageUri,
                    contentDescription = "Selected Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.upload),
                    contentDescription = "upload", // 이미지 설명
                    modifier = Modifier
                        .size(50.dp)
                )
            }
        }
            Box(modifier = Modifier) {
                val keyboardController = LocalSoftwareKeyboardController.current
                // TextField를 사용하여 사용자에게 입력 받음
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("설명") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .padding(vertical = 16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()}),
                    maxLines = 2
                    )
            }
            Row (
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ){

                CustomTextButton(
                    text = "등록",
                    onClick = {
                            // "등록하기" 버튼 클릭 시 uploadCardRequested 값을 true로 변경
                            uploadCardRequested = true
                    }
                )
                CustomTextButton(
                    text = "취소",
                    onClick = { navController.popBackStack() },
                )
            }
        }
        // "업로드 완료" 다이얼로그
        if (uploadCompleteDialogVisible) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = {
                        uploadCompleteDialogVisible = false
                        navController.navigate("photo") // 확인 버튼 클릭 시 이동
                    },
                    title = { Text("업로드 완료") },
                    text = { Text("카드가 성공적으로 등록되었습니다.") },
                    confirmButton = {
                        CustomTextButton(
                            onClick = {
                                uploadCompleteDialogVisible = false
                                navController.navigate("photo") // 확인 버튼 클릭 시 이동
                            },
                            text = "확인"
                        )
                    }
                )
        }
    // "사진을 선택해주세요" 다이얼로그
    if (showImageSelectDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = {
                showImageSelectDialog = false
            },
            title = { Text("알림") },
            text = { Text("사진을 선택해주세요.") },
            confirmButton = {
                CustomTextButton(
                    onClick = {
                        showImageSelectDialog = false
                    },
                    text = "확인"
                )
            }
        )
    }
}
