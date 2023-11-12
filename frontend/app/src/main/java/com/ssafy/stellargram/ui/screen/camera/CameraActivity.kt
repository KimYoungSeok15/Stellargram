package com.ssafy.stellargram.ui.screen.camera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ssafy.stellargram.ui.screen.camera.Common.allPermissionsGranted

class CameraActivity : ComponentActivity() {
    private var cameraX: CameraX = CameraX(this, this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CameraCompose(this, cameraX = cameraX) {
                if (allPermissionsGranted(this)) {
                    cameraX.capturePhoto()
                }
            }
        }
    }

}