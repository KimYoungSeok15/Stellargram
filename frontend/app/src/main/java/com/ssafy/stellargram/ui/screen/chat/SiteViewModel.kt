package com.ssafy.stellargram.ui.screen.chat

import androidx.lifecycle.ViewModel
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.SiteInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.http.Path
import javax.inject.Inject

@HiltViewModel
class SiteViewModel @Inject constructor() : ViewModel() {

    // 관측포인트 정보 초기값
    private val initLatitude: Double = 1000.0
    private val initLongitude: Double = 1000.0
    private val initName: String = "관측소 이름"
    private val initRatingSum: Int = 5
    private val initReviewCount: Int = 2

    // 관측포인트 정보
    private var latitudePrivate: Double = initLatitude
    private var longitudePrivate: Double = initLongitude
    private var namePrivate: String = initName
    private var ratingSumPrivate: Int = initRatingSum
    private var reviewCountPrivate: Int = initReviewCount

    val latitude: Double = latitudePrivate
    val longitude: Double = longitudePrivate
    val name: String = namePrivate
    val ratingSum: Int = ratingSumPrivate
    val reviewCount: Int = reviewCountPrivate

    // 현재 뷰모델에 관측포인트 정보 설정
    fun setSiteInfo(newInfo: SiteInfo) {
        latitudePrivate = newInfo.latitude
        longitudePrivate = newInfo.longitude
        namePrivate = newInfo.name
        ratingSumPrivate = newInfo.ratingSum
        reviewCountPrivate = newInfo.reviewCount
    }

    // 관측포인트 정보 가져오기
    suspend fun getSiteInfo(latitude: Double, longitude: Double) {
        if (latitude != initLatitude && longitude != longitude) {
            val response =
                NetworkModule.provideRetrofitInstanceSite().getSiteInfo(latitude, longitude)
            if (response?.code == 200) {
                setSiteInfo(response.data)
            }
        }
    }


}