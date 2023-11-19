package com.ssafy.stellargram.ui.screen.cameranew

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.StateFlow

interface CameraXNew {
    fun initialize(context: Context)
    fun startCamera(lifecycleOwner: LifecycleOwner)
    fun takePicture(showMessage: (String) -> Unit)
    fun flipCameraFacing()
    fun unBindCamera()
    fun getPreviewView() : PreviewView
    fun getFacingState() : StateFlow<Int>
    fun setIso(sensibility:Int)
    fun setExposureTime(exposureTime:Long)
    fun setClear()

}