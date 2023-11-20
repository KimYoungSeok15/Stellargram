package com.ssafy.stellargram.ui.screen.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.launch
import java.io.File
import java.nio.ByteBuffer
//import androidx.compose.material3.rememberScaffoldState
//import androidx.compose.material3.ScaffoldState
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import android.app.Application

private const val REQUEST_CAMERA_PERMISSION = 200

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class CameraActivity : ComponentActivity() {

//    private val TAG = "CameraActivity"
//    private lateinit var cameraManager: CameraManager
//    private lateinit var cameraId: String
//    private lateinit var cameraDevice: CameraDevice
//    private lateinit var cameraCaptureSession: CameraCaptureSession
//    private lateinit var backgroundHandler: Handler
//    private lateinit var backgroundThread: HandlerThread
//    private lateinit var imageReader: ImageReader
//    private lateinit var captureRequestBuilder: CaptureRequest.Builder
//
//    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
//        if (success) {
//            Toast.makeText(this, "Photo saved to gallery", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "Failed to save photo", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
//                return
//            }
//        }
//
//        setContent {
//            Surface(
//                modifier = Modifier.fillMaxSize(),
//                color = MaterialTheme.colorScheme.background
//            ) {
//                createCaptureSession()
//            }
//        }
//
//        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
//        try {
//            cameraId = cameraManager.cameraIdList[0]
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }
//
//        openCamera()
//    }
//
//    private fun openCamera() {
//        backgroundThread = HandlerThread("CameraBackground").apply { start() }
//        backgroundHandler = Handler(backgroundThread.looper)
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CAMERA
//        ) != PackageManager.PERMISSION_GRANTED
//            ) {
//            return
//        }
//
//        try {
//            cameraManager.openCamera(cameraId, stateCallback, backgroundHandler)
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }
//    }
//
//    private val stateCallback = object : CameraDevice.StateCallback() {
//        override fun onOpened(camera: CameraDevice) {
//            cameraDevice = camera
//            createCaptureSession()
//        }
//
//        override fun onDisconnected(camera: CameraDevice) {
//            cameraDevice.close()
//        }
//
//        override fun onError(camera: CameraDevice, error: Int) {
//            cameraDevice.close()
//        }
//    }
//
//    private fun createCaptureSession() {
//        val surfaceTexture = SurfaceTexture(1)
//        surfaceTexture.setDefaultBufferSize(1920, 1080)
//        val surface = Surface(surfaceTexture)
//
//        imageReader = ImageReader.newInstance(1920, 1080, ImageFormat.JPEG, 1)
//        imageReader.setOnImageAvailableListener(
//            { reader ->
//                val image = reader.acquireLatestImage()
//                val buffer: ByteBuffer = image.planes[0].buffer
//                val bytes = ByteArray(buffer.capacity())
//                buffer.get(bytes)
//                saveImage(bytes)
//                image.close()
//                closeCamera()
//            },
//            backgroundHandler
//        )
//
//        cameraDevice.createCaptureSession(listOf(surface, imageReader.surface),
//            object : CameraCaptureSession.StateCallback() {
//                override fun onConfigured(session: CameraCaptureSession) {
//                    cameraCaptureSession = session
//                    startPreview()
//                    startCapture()
//                }
//
//                override fun onConfigureFailed(session: CameraCaptureSession) {
//                    Log.e(TAG, "카메라 캡처 세션 구성 실패")
//                }
//            }, null)
//    }
//
//
//
//    private fun startPreview() {
//        captureRequestBuilder =
//            cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
//        captureRequestBuilder.addTarget(imageReader.surface)
//
//        val captureCallback = object : CameraCaptureSession.CaptureCallback() {}
//
//        cameraCaptureSession.setRepeatingRequest(
//            captureRequestBuilder.build(),
//            captureCallback,
//            backgroundHandler
//        )
//    }
//
//    private fun startCapture() {
//        val captureRequestBuilder =
//            cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
//
//        captureRequestBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, 400)
//        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_OFF)
//        captureRequestBuilder.set(
//            CaptureRequest.COLOR_CORRECTION_MODE,
//            CameraMetadata.COLOR_CORRECTION_MODE_TRANSFORM_MATRIX
//        )
//        captureRequestBuilder.set(CaptureRequest.COLOR_CORRECTION_GAINS, RATIONAL_WHITE_BALANCE_7600K)
//        captureRequestBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, 15000000000L)
//
//        captureRequestBuilder.addTarget(imageReader.surface)
//
//        val file = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")
//        val imageCaptureUri = Uri.fromFile(file)
//
//        saveImage(imageCaptureUri)
//
//        (application as CameraApplication).capturedImageUri = imageCaptureUri
//        captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, 0)
//        captureRequestBuilder.addTarget(SurfaceHolder(holder))
//
//        cameraCaptureSession.capture(
//            captureRequestBuilder.build(),
//            CameraCaptureSession.CaptureCallback(),
//            backgroundHandler
//        )
//    }
//
//
//
//    private fun closeCamera() {
//        cameraCaptureSession.close()
//        cameraDevice.close()
//        imageReader.close()
//        backgroundThread.quitSafely()
//        try {
//            backgroundThread.join()
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
//    }
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CAMERA_PERMISSION) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openCamera()
//            } else {
//                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
//                finish()
//            }
//        }
//    }
//
//    private fun saveImage(bytes: ByteArray) {
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
//            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
//        }
//
//        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//        uri?.let {
//            contentResolver.openOutputStream(it)?.use { outputStream ->
//                outputStream.write(bytes)
//            }
//            Toast.makeText(this, "Photo saved to gallery", Toast.LENGTH_SHORT).show()
//        } ?: run {
//            Toast.makeText(this, "Failed to save photo", Toast.LENGTH_SHORT).show()
//        }
//    }
}

@Composable
fun Modifier.CameraSettingsDrawer(
    onIsoChanged: (Int) -> Unit,
    onWhiteBalanceChanged: (Int) -> Unit,
    onShutterSpeedChanged: (Long) -> Unit
): Modifier {
    return this
}