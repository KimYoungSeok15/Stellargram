package com.ssafy.stellargram.ui.screen.camera

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class CameraActivity : ComponentActivity() {
    private val CAMERA_REQUEST_CODE = 1001

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // 권한이 허용된 경우
                openCamera()
            } else {
                // 권한이 거부된 경우
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                CameraApp()
            }
        }
    }

    @Composable
    fun CameraApp() {
        var capturedImage: ImageBitmap? by remember { mutableStateOf(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the captured image if available
            if (capturedImage != null) {
                Image(
                    bitmap = capturedImage!!,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { saveImageToGallery(capturedImage!!) },
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save to Gallery")
                }
            } else {
                // Display the camera button
                CameraButton()
            }
        }
    }

    @Composable
    fun CameraButton() {
        val context = LocalContext.current

        Button(
            onClick = {
                // 권한이 이미 허용되어 있는지 확인
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // 이미 권한이 허용된 경우
                    openCamera()
                } else {
                    // 권한을 요청
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            modifier = Modifier
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text("Open Camera")
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            // Start the camera activity
            startActivityForResult(
                cameraIntent,
                CAMERA_REQUEST_CODE
            )
        } else {
            // If the camera app is not available
            Toast.makeText(this, "Cannot open camera app", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Camera app has captured an image
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                // Convert Bitmap to ImageBitmap
                val capturedImage = imageBitmap.asImageBitmap()
                // Save the captured image to the gallery
                saveImageToGallery(capturedImage)
            }
        }
    }

    private fun saveImageToGallery(imageBitmap: ImageBitmap) {
        val context = this

        // Convert ImageBitmap to Bitmap
        val imageBitmapConfig: Bitmap.Config = Bitmap.Config.ARGB_8888
        val bitmap = Bitmap.createBitmap(imageBitmap.width, imageBitmap.height, imageBitmapConfig)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val shader = BitmapShader(
            imageBitmap.asAndroidBitmap(),
            Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        paint.shader = shader
        canvas.drawRoundRect(
            RectF(0f, 0f, imageBitmap.width.toFloat(), imageBitmap.height.toFloat()),
            0f,
            0f,
            paint
        )

        // Save the image to the gallery
        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "Image_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }
        )

        uri?.let { imageUri ->
            context.contentResolver.openOutputStream(imageUri).use { outputStream ->
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

//    private val cameraX: CameraX = CameraX(this, this)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            CameraCompose(this, cameraX = cameraX) {
//                if (allPermissionsGranted(this)) {
//                    cameraX.capturePhoto()
//                }
//            }
//
//        }
//    }
//}