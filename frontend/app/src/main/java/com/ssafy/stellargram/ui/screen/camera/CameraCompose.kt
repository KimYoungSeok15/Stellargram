package com.ssafy.stellargram.ui.screen.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.ssafy.stellargram.ui.screen.camera.Common.REQUIRED_PERMISSIONS
@Composable
fun CameraCompose(
    context: Context,
    cameraX: CameraX,
    onCaptureClick: () -> Unit,
) {
    var hasCamPermission by remember {
        mutableStateOf(
            REQUIRED_PERMISSIONS.all {
                ContextCompat.checkSelfPermission(context, it) ==
                        PackageManager.PERMISSION_GRANTED
            }
        )
    }

    var showISOButton by remember { mutableStateOf(false) }
    var selectedISOValue by remember { mutableStateOf(100) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { granted ->
            hasCamPermission = granted.size == 2
            if (hasCamPermission) {
                showISOButton = true
            }
        }
    )
    LaunchedEffect(key1 = true) {
        launcher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }
    Column(modifier = Modifier.fillMaxSize()) {
        if (hasCamPermission) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { cameraX.startCameraPreviewView() }
            )
        }
    }

    Row {
        Column(
            modifier = Modifier.width(100.dp), Arrangement.Bottom, Alignment.CenterHorizontally
        ) {
            if (showISOButton) {
                Slider(
                    value = selectedISOValue.toFloat(),
                    onValueChange = {
                        selectedISOValue = it.toInt()
                    },
                    valueRange = 0f..100f,
                    steps = 1,
                )
                Button(
                    onClick = {
                        onSetISO(context, selectedISOValue)
                    }
                ) {
                    Text(text = "Set ISO")
                }
            }
        }

        Column(
            modifier = Modifier.width(100.dp), Arrangement.Bottom, Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onCaptureClick
            ) {
                Text(text = "Capture")
            }
        }
    }
}

fun onSetISO(context: Context, isoValue: Int) {
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraId = cameraManager.cameraIdList[0]

    val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)

    val capabilities =
        cameraCharacteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
    if (capabilities?.contains(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_MANUAL_SENSOR) == true) {
        val cameraDeviceStateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                val captureRequestBuilder =
                    camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

                captureRequestBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, isoValue)

                // Additional settings can be added here

                val captureRequest = captureRequestBuilder.build()


            }

            override fun onDisconnected(p0: CameraDevice) {
                TODO("Not yet implemented")
            }

            override fun onError(p0: CameraDevice, p1: Int) {
                TODO("Not yet implemented")
            }
        }
    }
}
