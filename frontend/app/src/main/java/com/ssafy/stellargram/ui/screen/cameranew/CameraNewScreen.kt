package com.ssafy.stellargram.ui.screen.cameranew

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.ssafy.stellargram.R
import com.ssafy.stellargram.ui.Screen
import com.ssafy.stellargram.ui.common.CustomButtonLined
import com.ssafy.stellargram.ui.common.CustomTextButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun CameraNewScreen(navController: NavController) {
    val savedCameraState = MutableStateFlow<CameraState>(CameraState.PermissionNotGranted)
    val cameraState = savedCameraState.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = {},
        snackbarHost = { SnackbarHost(snackBarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { it ->
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            when (cameraState.value) {
                is CameraState.PermissionNotGranted -> {
                    RequestPermission() {
                        savedCameraState.value = it
                    }
                }

                is CameraState.Success -> {
                    CameraOpenScreen { message ->
                        CoroutineScope(Dispatchers.Default).launch {
                            snackBarHostState.showSnackbar(message)
                        }
                    }
                }
            }

        }


    }
}

// 카메라 열린 스크린

@Composable
private fun CameraOpenScreen(showSnackBar: (String) -> Unit) {

    val isoInitial = 8.0F
    val exposureTimeInitial = 10.0F

    var iso by remember { mutableFloatStateOf(isoInitial) }
    var exposureTime by remember { mutableFloatStateOf(exposureTimeInitial) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraScope = rememberCoroutineScope()
    val context = LocalContext.current
    val cameraX by remember { mutableStateOf<CameraXNew>(CameraXImpl()) }
    val previewView = remember { mutableStateOf<PreviewView?>(null) }
    val facing = cameraX.getFacingState().collectAsState()


    var isSelectedISO by remember { mutableStateOf(false) }
    var isSelectedSpeed by remember { mutableStateOf(false) }

    val sliderModifier = Modifier.height(50.dp)

    LaunchedEffect(Unit) {
        cameraX.initialize(context = context)
        previewView.value = cameraX.getPreviewView()
    }
    DisposableEffect(facing.value) {
        cameraScope.launch(Dispatchers.Main) {
            cameraX.startCamera(lifecycleOwner = lifecycleOwner)
        }
        onDispose {
            cameraX.unBindCamera()
        }
    }
    Box(Modifier.fillMaxSize()) {
        previewView.value?.let { preview ->
            AndroidView(
                modifier = Modifier
                    .matchParentSize()
                    .align(Alignment.Center),
                factory = { preview }) {}
        }


        Row(
            Modifier
                .fillMaxSize()
//                .align(Alignment.BottomCenter)
        ) {

            Column(
                Modifier
                    .width(intrinsicSize = IntrinsicSize.Max)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
//                CustomTextButton(
//                    text = "사진 고르기",
//                    onClick = onPick,
//                    margin = 10.dp,
//                    isBold = false
//                )
                Column(Modifier.width(intrinsicSize = IntrinsicSize.Max)) {
                    CustomButtonLined(text = "ISO",isSelected=isSelectedISO, onClick = {
                        isSelectedISO = !isSelectedISO
                        isSelectedSpeed = false

                    })
                    CustomButtonLined(text = "Speed",isSelected=isSelectedSpeed, onClick = {
                        isSelectedISO = false
                        isSelectedSpeed = !isSelectedSpeed
                    })
                    CustomButtonLined(text = "초기화", onClick = {
                        isSelectedISO = false
                        isSelectedSpeed = false

                        iso = isoInitial
                        exposureTime = exposureTimeInitial

                    })
                }
                Column(Modifier.width(intrinsicSize = IntrinsicSize.Max), Arrangement.Center,Alignment.CenterHorizontally) {
                    if (isSelectedISO) {
                        Text(text = (iso.toInt() * 100).toString())
                    }

                    if (isSelectedSpeed) {
                        Text(text = (exposureTime.toInt()).toString() + "초")

                    }
                    IconButton(
                        onClick = {
                            // 클릭 이벤트 처리
                            cameraX.setExposureTime(exposureTime = exposureTime.toLong() * 1000000000) //1초
                            cameraX.takePicture(showSnackBar)
                            cameraX.setClear()
                            cameraX.setIso(sensibility = iso.toInt() * 100)
                        },
                        modifier = Modifier.padding(13.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_images_active),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )

                    }
                }

            }

            Column(Modifier.fillMaxHeight(), verticalArrangement = if(isSelectedISO||isSelectedSpeed)Arrangement.SpaceBetween else Arrangement.Top) {
                if(isSelectedISO){
                    Column {
                        Text(text = "빛에 대한 민감도에요")
                        Text(text = "이 값을 높이면 사진이 밝아지며, 노이즈가 발생해요")
                        Text(text = "어두울 때 높히고, 밝을 때 낮추는 게 좋아요")
                    }
                }

                if(isSelectedSpeed){
                    Column {
                        Text(text = "셔터가 열려있는 시간이에요")
                        Text(text = "ISO와 마찬가지로 사진의 밝기를 조절해요")
                        Text(text = "값을 높이면 사진이 밝아지며 잔상이 남아요")
                    }
                }

                if(!isSelectedSpeed && !isSelectedISO){
                    Column {
                        Text(text = "사진이 너무 어둡다면")
                        Text(text = "ISO를 높이거나, 셔터스피드를 길게 하세요")
                        Text(text = "사진이 너무 밝다면")
                        Text(text = "ISO를 낮추거나, 셔터스피드를 짧게 하세요")
                    }

                }
                // 슬라이드 바
                if (isSelectedISO) {
                    // iso 100단위 조절
                    Slider(
                        value = iso,
                        onValueChange = {
                            iso = it
                            cameraX.setIso(sensibility = iso.toInt() * 100)
                        },
                        valueRange = 1.0f..25.0f,
                        steps = 23,
                        modifier = sliderModifier
                    )
                }
                if (isSelectedSpeed) {
                    // 노출시간 1초단위 조절
                    Slider(
                        value = exposureTime,
                        onValueChange = {
                            exposureTime = it
                        },
                        valueRange = 1.0f..30.0f,
                        steps = 28,
                        modifier = sliderModifier
                    )
                }


            }


        }


    }
}


// 권한 요청
@Composable
private fun RequestPermission(
    setState: (CameraState) -> Unit
) {
    Log.d("permission", "is opened")
    val context = LocalContext.current
    val audioLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
            Log.d("permission", "try grant")

            if (granted) {
                setState(CameraState.Success)
            }
        }
    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
            audioLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        LaunchedEffect(Unit) {
            cameraLauncher.launch(Manifest.permission.CAMERA)
        }
    } else {
        Log.d("permission", "granted successfully")

        setState(CameraState.Success)
    }
}