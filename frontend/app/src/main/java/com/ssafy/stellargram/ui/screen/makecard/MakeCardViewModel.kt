package com.ssafy.stellargram.ui.screen.makecard

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ssafy.stellargram.data.remote.NetworkModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MakeCardViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    private val appContext: Context = application.applicationContext
    fun uploadCard(uri: String, content: String, photoAt: String, category: String, tool: String, observeSiteId: String) {
        viewModelScope.launch {
            try {
                val imageUri: Uri = Uri.parse(uri)
                val imageFilePart = prepareImageFilePart("imageFile", imageUri)
                val requestDto = createRequestDto(content, photoAt, category, tool, observeSiteId)

                val response = NetworkModule.provideRetrofitCards().uploadStarCard(imageFilePart, requestDto)
                Log.d("테스트", "$response")

                // 여기에 성공 시의 추가 작업을 수행할 수 있습니다.

            } catch (e: Exception) {
                // 오류 처리 코드를 작성할 수 있습니다.
                Log.e("테스트", e.message ?: "Unknown error")
            }
        }
    }

    private fun prepareImageFilePart(partName: String, uri: Uri): MultipartBody.Part {
        val file = File(absolutelyPath(uri))
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    private fun absolutelyPath(path: Uri): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = appContext.contentResolver.query(path, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result ?: ""
    }

    private fun createRequestDto(content: String, photoAt: String, category: String, tool: String, observeSiteId: String): RequestBody {
        val requestBody = Gson().toJson(RequestDto(content, photoAt, category, tool, observeSiteId))
            .toRequestBody("application/json".toMediaTypeOrNull())
        return requestBody
    }

    data class RequestDto(
        val content: String,
        val photoAt: String,
        val category: String,
        val tool: String,
        val observeSiteId: String
    )
}