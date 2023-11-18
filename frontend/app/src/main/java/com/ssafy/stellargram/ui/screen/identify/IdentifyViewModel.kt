package com.ssafy.stellargram.ui.screen.identify

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.ImageCropper
import com.mr0xf00.easycrop.crop
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.IdentifyPhotoData
import com.ssafy.stellargram.model.IdentifyResponse
import com.ssafy.stellargram.model.IdentifyStarInfo
import com.ssafy.stellargram.module.DBModule
import com.ssafy.stellargram.ui.theme.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class IdentifyViewModel @Inject constructor(private val app: Application) : AndroidViewModel(app) {
    // ---------- 선택된 카드 인덱스 관리 ----------
    // -2 : 선택안함. 오리진캡쳐, -1 : 전체칠사진, 0이상 : 인식된 별1개 사진
    var selectedIndex: Int by mutableIntStateOf(-2)

    // ---------- 크롭 관련 ----------
    // 크롭 관련 변수
    val imageCropper = ImageCropper()
    private val _selectedImage = MutableStateFlow<ImageBitmap?>(null)
    val selectedImage = _selectedImage.asStateFlow()
    private val _cropError = MutableStateFlow<CropError?>(null)
    val cropError = _cropError.asStateFlow()

    // 인식된 전체 별 이미지 저장
    private var _paintedAllImage = MutableStateFlow<ImageBitmap?>(null)
    val paintedAllImage = _paintedAllImage.asStateFlow()

    // 선택된 별 1개 표시된 이미지 저장
    private val _paintedImage = MutableStateFlow<ImageBitmap?>(null)
    val paintedImage = _paintedImage.asStateFlow()

    // 인식중인지 표시하는 Boolean
    var isIdentifying: Boolean by mutableStateOf(false) // 인식 중이면 true

    // 인식 실패했을 때의 Boolean
    var isFailed: Boolean by mutableStateOf(false) // 실패했다면 true

    fun cropErrorShown() {
        _cropError.value = null
    }

    // 크롭하는 함수
    fun setSelectedImage(uri: Uri) {
        viewModelScope.launch {
            when (val result = imageCropper.crop(uri, app)) {
                CropResult.Cancelled -> {}
                is CropError -> _cropError.value = result
                is CropResult.Success -> {
                    _selectedImage.value = result.bitmap
                    selectedIndex = -2
                }
            }
        }
    }

    // ---------- 별 인식 관련 ----------
    // 인식된 별 정보 목록
    private val privateStarInfoList: MutableList<IdentifyStarInfo> =
        mutableStateListOf()
    val starInfoList: List<IdentifyStarInfo> = privateStarInfoList

    // 인식된 사진의 메타정보
    var photoDec: Double by mutableDoubleStateOf(0.0) // 사진 정가운데의 적위
    var photoRA: Double by mutableDoubleStateOf(0.0) // 사진 정가운데의 적경
    var matchCount: Int by mutableIntStateOf(0) // 사진에서 인식된 별의 갯수
    var time_extract: Double by mutableDoubleStateOf(0.0) // 별 검출 시간
    var time_solve: Double by mutableDoubleStateOf(0.0) // 검출된 별에 대해 인식 시간

    // 이미지 메타정보 저장 리셋
    fun clearPhotoMeta() {
        photoDec = 0.0
        photoRA = 0.0
        matchCount = 0
        time_extract = 0.0
        time_solve = 0.0
        privateStarInfoList.clear()
    }

    // 별 식별 요청 함수
    fun identifyStarsFromBitmap(imageBitmap: ImageBitmap) {
        viewModelScope.launch {
            try {
                // 인식중 표시 켜기
                isIdentifying = true

                // 실패여부 false로 초기화
                isFailed = false

                // 선택 인덱트 -2(선택안함)으로 초기화
                selectedIndex = -2

                // 파일로 변환
                var newFile = saveFileFromIBitmap(imageBitmap)
                val context = getApplication<Application>().applicationContext
//                val tempFile: File = File.createTempFile("temp_image", null, context.cacheDir)

                // 변환된 파일이 있으면
                if (newFile != null) {
                    val requestFile =
                        newFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val multipartFile =
                        MultipartBody.Part.createFormData("file", newFile.name, requestFile)
                    // 파일에 대해 인식 요청 날리기
                    val response =
                        NetworkModule.provideRetrofitInstanceIdentify()
                            .getIdentifyData(file = multipartFile)
                    // 요청 받은 경우 200이면
                    if (response.isSuccessful) {
                        val identifyPhotoData = response.body()!!
                        // 사진 메타정보가 인식됐다면
                        if (identifyPhotoData.Dec != null) {
                            photoDec = identifyPhotoData.Dec!!
                            photoRA = identifyPhotoData.RA!!
                            matchCount = identifyPhotoData.Matches!!
                            time_extract = identifyPhotoData.T_extract!!
                            time_solve = identifyPhotoData.T_solve!!

                            // 별이 인식 됐다면 목록 리셋 후 저장
                            if (identifyPhotoData.matched != null) {


                                privateStarInfoList.clear()
                                privateStarInfoList.addAll(identifyPhotoData.matched!!)
                                for (data in privateStarInfoList) {
                                    var thisName = DBModule.nameMap[data.id]
                                    data.name = thisName
                                }
                                paintAllStarToNewImage(privateStarInfoList)
                            }
                        }
                        // 인식이 실패했다면 true 표시
                        else {
                            isFailed = true
                        }
                    }
                }
                // 인식중 표시
                isIdentifying = false

            } catch (e: Exception) {
                Log.e("file send error", e.toString())
                isIdentifying = false
                isFailed = false
            }

        }

    }

    // 비트맵 객체를 전송을 위한 파일로 변환
    private fun saveFileFromIBitmap(imageBitmap: ImageBitmap): File? {
//        val saveDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//            .toString() + name
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {

            // 갤러리에 저장하는 코드. 현재 저장하지 않고 임시파일로 생성하여 전송
//            val rootPath =
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                    .toString()
//            val dirName = "/" +  "stellagram"
//            val savePath = File(rootPath + dirName)
//            savePath.mkdirs()

            // 파일명
            val fileName = System.currentTimeMillis().toString()

            // 임시파일 객체 생성
            val context = getApplication<Application>().applicationContext
            val tempFile: File = File.createTempFile(fileName, null, context.cacheDir)

            // 비트맵 파일을 파일로 변환해서 tempFile에 담기
            try {
                val out = FileOutputStream(tempFile)
                imageBitmap.asAndroidBitmap().compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()

                // 변환된 임시파일 반환
                return tempFile
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    // ---------- 카드 클릭시 별 표시 ----------
    val range: Int = 3
    val starPaintColor = Color.YELLOW

    fun paintOneStarToNewImage(star: IdentifyStarInfo) {
        // 선택된 이미지가 없으면 수행하지 않음
        if (selectedImage.value == null) return

        // 찍는 점 범위의 반지름? 찍는 중심점으로부터 얼마나 멀리까지 찍는지
        val paintSize: Int = range * 2 + 1

        // 기존 이미지와 새 이미지
        val originBitmap = selectedImage.value!!.asAndroidBitmap()
        var newBitmap: Bitmap = originBitmap.copy(originBitmap.config, true)

        // 이미지 너비 높이
        val widthX = newBitmap.width
        val heightY = newBitmap.height

        // 채우는 범위 조각 만들기
        val pixels = IntArray(paintSize * paintSize) { starPaintColor }

        // 경계조건
        if (star.pixelx in 0..widthX && star.pixely in 0..heightY) {
            // 음수 시작점 보정
            val startX = if (star.pixelx - range < 0) 0 else star.pixelx
            val startY = if (star.pixely - range < 0) 0 else star.pixely
            // 칠하기
            newBitmap.setPixels(pixels, 0, paintSize, startX, startY, paintSize, paintSize)
        }
        _paintedImage.value = newBitmap.asImageBitmap()

    }

    fun paintAllStarToNewImage(starInfoList: List<IdentifyStarInfo>) {
        // 선택된 이미지가 없으면 수행하지 않음
        if (selectedImage.value == null) return

        // 인식된 별이 없으면 수행하지 않음
        if (starInfoList.size == 0) return

        // 찍는 점 범위의 반지름? 찍는 중심점으로부터 얼마나 멀리까지 찍는지
        val paintSize: Int = range * 2 + 1

        // 기존 이미지와 새 이미지
        val originBitmap = selectedImage.value!!.asAndroidBitmap()
        val newBitmap = originBitmap.copy(originBitmap.config, true)

        // 이미지 너비 높이
        val widthX = newBitmap.width
        val heightY = newBitmap.height

        // 채우는 범위 조각 만들기
        val pixels = IntArray(paintSize * paintSize) { starPaintColor }

        // 인식된 별들에 대해
        for (star in starInfoList) {
            //경계조건
            if (star.pixelx in 0..widthX && star.pixely in 0..heightY) {
                // 음수 시작점 보정
                val startX = if (star.pixelx - range < 0) 0 else star.pixelx
                val startY = if (star.pixely - range < 0) 0 else star.pixely
                // 칠하기
                newBitmap.setPixels(pixels, 0, paintSize, startX, startY, paintSize, paintSize)
            }
        }
        // 생성한 이미지 저장
        _paintedAllImage.value = newBitmap.asImageBitmap()
    }


}