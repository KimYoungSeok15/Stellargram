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
    private val initName: String = ""
    private val initRatingSum: Int = 0
    private val initReviewCount: Int = -1

    // 관측포인트 정보
    private var latitude: Double = initLatitude
    private var longitude: Double = initLongitude
    private var name: String = initName
    private var ratingSum: Int = initRatingSum
    private var reviewCount: Int = initReviewCount

    // 현재 뷰모델에 관측포인트 정보 설정
    fun setSiteInfo(newInfo: SiteInfo) {
        latitude = newInfo.latitude
        longitude = newInfo.longitude
        name = newInfo.name
        ratingSum = newInfo.ratingSum
        reviewCount = newInfo.reviewCount
    }

    // 관측포인트 정보 가져오기
    suspend fun getSiteInfo() {
        if (latitude != initLatitude && longitude != longitude) {
            val response =
                NetworkModule.provideRetrofitInstanceSite().getSiteInfo(latitude, longitude)
            if (response?.code == 200) {
                setSiteInfo(response.data)
            }
        }
    }
}