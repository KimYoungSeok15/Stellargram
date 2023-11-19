package com.ssafy.stellargram.ui.screen.cameranew

import android.content.ContentValues
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CaptureRequest
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2CameraControl
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.camera2.interop.CaptureRequestOptions
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalCamera2Interop::class)
class CameraXImpl : CameraXNew {

    private val _facing = MutableStateFlow(CameraSelector.LENS_FACING_BACK)
    private val _flash = MutableStateFlow(false)

    private lateinit var previewView: PreviewView
    private lateinit var preview: Preview
    private lateinit var cameraProvider: ListenableFuture<ProcessCameraProvider>
    private lateinit var provider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var context: Context
    private lateinit var executor: ExecutorService

    private lateinit var imageCapture: ImageCapture

    // 초기화
    @OptIn(ExperimentalCamera2Interop::class)
    override fun initialize(context: Context) {
        previewView = PreviewView(context)

        preview =
            Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }

        cameraProvider = ProcessCameraProvider.getInstance(context)
        provider = cameraProvider.get()
        imageCapture = ImageCapture.Builder().build()
        executor = Executors.newSingleThreadExecutor()
        this.context = context

    }

    // 카메라 시작하기
    @OptIn(ExperimentalCamera2Interop::class)
    override fun startCamera(
        lifecycleOwner: LifecycleOwner,
    ) {
        unBindCamera()

        val cameraSelector = CameraSelector.Builder().requireLensFacing(_facing.value).build()

        cameraProvider.addListener({
            CoroutineScope(Dispatchers.Main).launch {
                camera = provider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            }
        }, executor)
    }

    // 사진 찍기
    override fun takePicture(
        showMessage: (String) -> Unit
    ) {
        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/stellagram")
        if (!path.exists()) path.mkdirs();
        val photoFile = File(
            path, SimpleDateFormat(
                "yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA
            ).format(System.currentTimeMillis()) + ".jpg"
        )
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
            imageCapture.takePicture(outputFileOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(error: ImageCaptureException) {
                        error.printStackTrace()
                    }

                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        showMessage(
                            "Capture Success!! Image Saved at  \n [${Environment.getExternalStorageDirectory().absolutePath}/${Environment.DIRECTORY_PICTURES}/cameraX]"
                        )
                    }
                })
        }
    }

    // 후면 전면 카메라 전환
    override fun flipCameraFacing() {
        if (_facing.value == CameraSelector.LENS_FACING_BACK) {
            _flash.value = false
            _facing.value = CameraSelector.LENS_FACING_FRONT
        } else {
            _facing.value = CameraSelector.LENS_FACING_BACK
        }
    }

    // 카메라 해제
    override fun unBindCamera() {
        provider.unbindAll()
    }

    // 카메라 프리뷰 가져오기
    override fun getPreviewView(): PreviewView = previewView
    
    // 카메라 
    override fun getFacingState(): StateFlow<Int> = _facing.asStateFlow()
    
    // 카메라 노출값 설정. 카메라가 열린 이후 실행할 것
    override fun setIso(sensibility:Int) {
        val captureRequestOptions = CaptureRequestOptions.Builder()
            .setCaptureRequestOption(
                CaptureRequest.CONTROL_AE_MODE,
                CaptureRequest.CONTROL_AE_MODE_OFF
            )
            // TODO: 입력값으로 바꿀 것
//            .setCaptureRequestOption(CaptureRequest.SENSOR_SENSITIVITY, sensibility)
            .setCaptureRequestOption(CaptureRequest.SENSOR_SENSITIVITY, 3200)

            .build()

        val camera2control = Camera2CameraControl.from(camera.cameraControl)

        camera2control.captureRequestOptions = captureRequestOptions

    }

    // 카메라 노출시간 설정. 카메라가 열린 이후 실행할 것
    override fun setExposureTime(exposureTime:Int) {
        val captureRequestOptions = CaptureRequestOptions.Builder()
            .setCaptureRequestOption(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_OFF)
            // TODO: 입력값으로 바꿀 것
//            .setCaptureRequestOption(CaptureRequest.SENSOR_EXPOSURE_TIME,exposureTime) // 단위 나노초 3000000000 3초
            .setCaptureRequestOption(CaptureRequest.SENSOR_EXPOSURE_TIME,500000000) // 단위 나노초 3000000000 3초
            .build()

        val camera2control = Camera2CameraControl.from(camera.cameraControl)

        camera2control.captureRequestOptions = captureRequestOptions
    }

    // 설정값 초기화
    override fun setClear(){
        val camera2control = Camera2CameraControl.from(camera.cameraControl)

        camera2control.clearCaptureRequestOptions()
    }
}