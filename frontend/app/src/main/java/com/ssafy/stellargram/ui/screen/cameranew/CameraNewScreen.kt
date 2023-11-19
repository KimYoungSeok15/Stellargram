package com.ssafy.stellargram.ui.screen.cameranew

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
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
        Box(modifier = Modifier.padding(it).fillMaxSize()) {
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
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraScope = rememberCoroutineScope()
    val context = LocalContext.current
    val cameraX by remember { mutableStateOf<CameraXNew>(CameraXImpl()) }
    val previewView = remember { mutableStateOf<PreviewView?>(null) }
    val facing = cameraX.getFacingState().collectAsState()

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
    Box(Modifier.fillMaxSize().clipToBounds()) {
        previewView.value?.let { preview ->
            AndroidView(
                modifier = Modifier.matchParentSize()
                    .align(Alignment.Center),
                factory = { preview }) {}
        }

//        Column(Modifier.align(Alignment.BottomCenter)) {
//            Column(
//
//            ) {
//                Button(
//                    modifier = Modifier
//                        .padding(10.dp),
//                    onClick = {
//                        cameraX.flipCameraFacing()
//                    }
//                ) {
//                    Text(if (facing.value == CameraSelector.LENS_FACING_FRONT) "back" else "front")
//                }
//
//                Button(
//                    modifier = Modifier
//                        .padding(10.dp),
//                    onClick = {
//                        cameraX.takePicture(showSnackBar)
//                    }
//                ) {
//                    Text("takePicture")
//                }
//
//                Button(
//                    modifier = Modifier
//                        .padding(10.dp),
//                    onClick = {
//                        cameraX.setIso(sensibility = 3200)
//                    }
//                ) {
//                    Text("iso 테스트")
//                }
//
//                Button(
//                    modifier = Modifier
//                        .padding(10.dp),
//                    onClick = {
//                        cameraX.setExposureTime(exposureTime = 500000000) //0.5초
//                    }
//                ) {
//                    Text("노출시간 테스트")
//                }
//
//                Button(
//                    modifier = Modifier
//                        .padding(10.dp),
//                    onClick = {
//                        cameraX.setClear()
//                    }
//                ) {
//                    Text("옵션리셋 테스트")
//                }
//            }
//        }

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