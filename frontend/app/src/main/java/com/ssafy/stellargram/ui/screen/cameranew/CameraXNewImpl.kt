package com.ssafy.stellargram.ui.screen.cameranew

import android.content.Context
import android.hardware.camera2.CaptureRequest
import android.os.Environment
import android.util.Log
import android.view.Surface.ROTATION_270
import android.view.Surface.ROTATION_90
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2CameraControl
import androidx.camera.camera2.interop.CaptureRequestOptions
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.window.WindowManager
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs

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

//        Log.d(
//            "레이아웃 높이", previewView.layoutParams.height.toString()
//        )
//
//        Log.d(
//            "레이아웃 너비", previewView.layoutParams.width.toString()
//        )

//        val layoutParams = previewView.layoutParams as FrameLayout.LayoutParams
//        layoutParams.height = previewView.width * aspectRatio.numerator / aspectRatio.denominator

        previewView.scaleType = PreviewView.ScaleType.FIT_CENTER
        previewView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        previewView.surfaceProvider


        val windowManager = WindowManager(context)

        // Get screen metrics used to setup camera for full screen resolution
        val metrics = windowManager.getCurrentWindowMetrics().bounds
        Log.d("스크린 metrics", "Screen metrics: ${metrics.width()} x ${metrics.height()}")

        val screenAspectRatio = aspectRatio(metrics.width(), metrics.height())

//        previewView.layoutParams = ViewGroup.LayoutParams(
//            metrics.width(),
//            metrics.height()
//        )

        Log.d(
            "레이아웃 높이", previewView.layoutParams.height.toString()
        )

        Log.d(
            "레이아웃 너비", previewView.layoutParams.width.toString()
        )


//        previewView.layoutParams.height=metrics.height()
//        previewView.layoutParams.width=metrics.width()

        preview =
            Preview.Builder()
                .setTargetRotation(ROTATION_270)
//                .setTargetRotation(ROTATION_90)
//                .setTargetAspectRatio(screenAspectRatio)
                .build()


//        preview.resolutionInfo.toString()

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

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(_facing.value)
            .build()

//        val resolutionSelector: ResolutionSelector = ResolutionSelector.Builder()
//            .setAllowedResolutionMode(aspectRatio())
//            .setDefaultResolution(Size(1920, 1080))
//            .build()

        cameraProvider.addListener({
            CoroutineScope(Dispatchers.Main).launch {
//                if (camera != null) {
//                    // Must remove observers from the previous camera instance
//                    camera!!.cameraInfo.cameraState.removeObservers(lifecycleOwner)
//                }


                camera = provider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                preview.setSurfaceProvider(previewView.surfaceProvider)

                // ResolutionSelector를 사용하여 원하는 해상도를 선택합니다.
                val resolutionSelector = ResolutionSelector.Builder()
//                    .setAspectRatioStrategy()
//                    .setTargetResolution(Size(1920, 1080)) // 원하는 해상도를 설정합니다.
                    .build()

//                camera.cameraInfo.torchState.observe(lifecycleOwner) { state ->
//                    camera.cameraControl.enableTorch(state)
//                    camera.cameraControl.setLinearZoom(resolutionSelector)
//                }

                Log.d("camera 정보", camera.cameraInfo.intrinsicZoomRatio.toString())
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
    override fun setIso(sensibility: Int) {
        val captureRequestOptions = CaptureRequestOptions.Builder()
            .setCaptureRequestOption(
                CaptureRequest.CONTROL_AE_MODE,
                CaptureRequest.CONTROL_AE_MODE_OFF
            )
            // TODO: 입력값으로 바꿀 것
            .setCaptureRequestOption(CaptureRequest.SENSOR_SENSITIVITY, sensibility)
//            .setCaptureRequestOption(CaptureRequest.SENSOR_SENSITIVITY, 3200)

            .build()

        val camera2control = Camera2CameraControl.from(camera.cameraControl)

        camera2control.captureRequestOptions = captureRequestOptions

    }

    // 카메라 노출시간 설정. 카메라가 열린 이후 실행할 것
    override fun setExposureTime(exposureTime: Long) {
        val captureRequestOptions = CaptureRequestOptions.Builder()
            .setCaptureRequestOption(
                CaptureRequest.CONTROL_AE_MODE,
                CaptureRequest.CONTROL_AE_MODE_OFF
            )
            // TODO: 입력값으로 바꿀 것
            .setCaptureRequestOption(CaptureRequest.SENSOR_EXPOSURE_TIME,exposureTime) // 단위 나노초 3000000000 3초
//            .setCaptureRequestOption(
//                CaptureRequest.SENSOR_EXPOSURE_TIME,
//                500000000
//            ) // 단위 나노초 3000000000 3초
            .build()

        val camera2control = Camera2CameraControl.from(camera.cameraControl)

        camera2control.captureRequestOptions = captureRequestOptions
    }

    // 설정값 초기화
    override fun setClear() {
        val camera2control = Camera2CameraControl.from(camera.cameraControl)

        camera2control.clearCaptureRequestOptions()
    }
}

private fun aspectRatio(width: Int, height: Int): Int {
    val previewRatio = max(width, height).toDouble() / min(width, height)
    if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
        return AspectRatio.RATIO_4_3
    }
    return AspectRatio.RATIO_16_9


}

private const val TAG = "CameraXBasic"
private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val PHOTO_TYPE = "image/jpeg"
private const val RATIO_4_3_VALUE = 4.0 / 3.0
private const val RATIO_16_9_VALUE = 16.0 / 9.0