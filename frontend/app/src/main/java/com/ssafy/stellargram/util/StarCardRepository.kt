package com.ssafy.stellargram.util

import android.net.Uri
import com.google.gson.Gson
import com.ssafy.stellargram.data.remote.ApiServiceForCards
import com.ssafy.stellargram.model.CardPostResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject


// -------------------------카드 등록 api 요청 (uploadStarCard) 하는 클래스--------------------------

// 사용 방법: val response = repository.uploadCard(imageUri, content, photo_at, category, tool, observeSiteId)


class StarCardRepository @Inject constructor(
    private val apiService: ApiServiceForCards
) {
    suspend fun uploadCard(uri: String, content: String, photo_at: String = "", category: String = "galaxy", tool: String = "", observeSiteId: String = ""): Response<CardPostResponse> {
        val imageUri:Uri = Uri.parse(uri)
        val imageFilePart = prepareImageFilePart("imageFile", imageUri)
        val requestDto = createRequestDto(content, photo_at, category, tool, observeSiteId)

        return apiService.uploadStarCard(imageFilePart, requestDto)
    }

    private fun prepareImageFilePart(partName: String, uri: Uri): MultipartBody.Part {
        val file = File(uri.path!!)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    // GSON을 안쓰는 방식
//    private fun createRequestDto(content: String, photo_at: String, category: String, tool: String, observeSiteId: String): RequestBody {
//        val jsonObject = JSONObject()
//        jsonObject.put("content", content)
//        jsonObject.put("photo_at", photo_at)
//        jsonObject.put("category", category)
//        jsonObject.put("tool", tool)
//        jsonObject.put("observeSiteId", observeSiteId)
//
//
//        return jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
//    }

    // GSON을 쓰는 방식
    private fun createRequestDto(content: String, photo_at: String, category: String, tool: String, observeSiteId: String): RequestBody {
        val requestBody = Gson().toJson(RequestDto(content, photo_at, category, tool, observeSiteId))
            .toRequestBody("application/json".toMediaTypeOrNull())
        return requestBody
    }

    // 데이터 클래스를 정의하여 사용
    data class RequestDto(
        val content: String,
        val photo_at: String,
        val category: String,
        val tool: String,
        val observeSiteId: String
    )
}