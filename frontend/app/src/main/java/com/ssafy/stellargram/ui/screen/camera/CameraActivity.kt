package com.ssafy.stellargram.ui.screen.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import com.ssafy.stellargram.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Semaphore


@SuppressLint("RestrictedApi")
class CameraActivity : ComponentActivity() {

    private val cameraManager by lazy {
        getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    private lateinit var cameraId: String
    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private var imageReader: ImageReader? = null
    private var backgroundHandler: Handler? = null
    private var backgroundThread: HandlerThread? = null

    private val cameraOpenCloseLock = Semaphore(1)

    private lateinit var textureView: TextureView

    private var sensorOrientation: Int = 0

    private var isCaptureButtonEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textureView = findViewById(R.id.textureView)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            openCamera()
        }

        setupCaptureButton()
    }

    private fun openCamera() {
        val cameraStateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                sensorOrientation = cameraManager
                    .getCameraCharacteristics(cameraId)
                    .get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0
                createCameraPreviewSession(textureView, cameraDevice, CameraCaptureSession )
            }

            override fun onDisconnected(camera: CameraDevice) {
                cameraOpenCloseLock.release()
                cameraDevice?.close()
                cameraDevice = null
            }

            override fun onError(camera: CameraDevice, error: Int) {
                onDisconnected(camera)
                finish()
            }
        }

        try {
            cameraId = cameraManager.cameraIdList[0]
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            } else {
                cameraManager.openCamera(cameraId, cameraStateCallback, null)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    private fun updatePreview() {
        try {
            val captureRequestBuilder =
                cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

            captureRequestBuilder?.build()?.let {
                captureSession?.setRepeatingRequest(
                    it,
                    null,
                    backgroundHandler
                )
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun createCameraPreviewSession() {
        try {
            val texture = textureView.surfaceTexture
            if (texture != null) {
                texture.setDefaultBufferSize(1920, 1080)
            }

            val surface = Surface(texture)
            val captureRequestBuilder =
                cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder?.addTarget(surface)

            cameraDevice?.createCaptureSession(
                listOf(surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        captureSession = session
                        updatePreview()
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                    }
                },
                null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun closeCamera() {
        try {
            cameraOpenCloseLock.acquire()
            captureSession?.close()
            captureSession = null
            cameraDevice?.close()
            cameraDevice = null
            imageReader?.close()
            imageReader = null
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            cameraOpenCloseLock.release()
        }
    }

    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("CameraBackground").also { it.start() }
        backgroundHandler = backgroundThread?.looper?.let { Handler(it) }
    }

    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        try {
            backgroundThread?.join()
            backgroundThread = null
            backgroundHandler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()

        if (textureView.isAvailable) {
            openCamera()
        } else {
            textureView.surfaceTextureListener = surfaceTextureListener
        }
    }

    override fun onPause() {
        closeCamera()
        stopBackgroundThread()
        super.onPause()
    }

    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(
            surface: SurfaceTexture,
            width: Int,
            height: Int
        ) {
            openCamera()
            createCameraPreviewSession()
        }

        override fun onSurfaceTextureSizeChanged(
            surface: SurfaceTexture,
            width: Int,
            height: Int
        ) {

        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            return true
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
    }

    private fun setupCaptureButton() {
        val captureButton = findViewById<Button>(R.id.captureButton)
        captureButton.setOnClickListener {
            if (isCaptureButtonEnabled) {
                takePicture()
            }
        }
    }

    private fun setupCaptureButtonEnabled(enabled: Boolean) {
        isCaptureButtonEnabled = enabled
    }

    private fun takePicture() {
        if (cameraDevice == null) return

        val imageReaderListener = ImageReader.OnImageAvailableListener { reader ->
            val image = reader?.acquireLatestImage()
            saveImage(image)
            image?.close()
        }

        val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val characteristics = manager.getCameraCharacteristics(cameraDevice!!.id)
        val size = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            ?.getOutputSizes(ImageFormat.JPEG)?.maxByOrNull { it.width * it.height }

        imageReader = ImageReader.newInstance(
            size!!.width, size.height,
            ImageFormat.JPEG, 1
        )
        imageReader?.setOnImageAvailableListener(imageReaderListener, backgroundHandler)

        val captureBuilder =
            cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)?.apply {
                addTarget(imageReader!!.surface)
                set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)

                set(CaptureRequest.SENSOR_SENSITIVITY, 400)
                set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_OFF)
                set(CaptureRequest.CONTROL_AWB_MODE, 7600)
                set(CaptureRequest.SENSOR_EXPOSURE_TIME, 15000000000L)
            }

        val rotation = windowManager.defaultDisplay.rotation
        captureBuilder?.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation))

        val file = createImageFile()
        val readerSurface = imageReader?.surface
        val surfaces = listOf(readerSurface, Surface(textureView.surfaceTexture))

        cameraDevice?.createCaptureSession(
            surfaces,
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    try {
                        captureBuilder?.build()
                            ?.let { session.capture(it, null, backgroundHandler) }
                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                }
            },
            null
        )
    }

    private fun saveImage(image: Image?) {
        val buffer = image?.planes?.get(0)?.buffer
        val bytes = ByteArray(buffer?.remaining() ?: 0)
        buffer?.get(bytes)

        val file = createImageFile()
        FileOutputStream(file).use { output -> output.write(bytes) }

        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = MediaStore.Files.getContentUri("external")
        mediaScanIntent.data = contentUri
        mediaScanIntent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, file.name)
        sendBroadcast(mediaScanIntent)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun getOrientation(rotation: Int): Int {
        return (ORIENTATIONS.get(rotation) + sensorOrientation + 270) % 360
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1
        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }

        private fun createCameraPreviewSession(textureView: TextureView, cameraDevice: CameraDevice?, captureSession: CameraCaptureSession) {
            try {
                val texture = textureView.surfaceTexture
                if (texture != null) {
                    texture.setDefaultBufferSize(1920, 1080)
                }

                val surface = Surface(texture)
                val captureRequestBuilder =
                    cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequestBuilder?.addTarget(surface)

                cameraDevice?.createCaptureSession(
                    listOf(surface),
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(session: CameraCaptureSession) {
                            captureSession = session
                            updatePreview()
                            setupCaptureButtonEnabled(true)
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {
                            setupCaptureButtonEnabled(false)
                        }
                    },
                    null
                )
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
    }
}