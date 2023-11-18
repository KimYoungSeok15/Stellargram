package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.SiteInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SiteViewModel @Inject constructor() : ViewModel() {

    // 관측포인트 정보
    var latitude: Double by mutableDoubleStateOf(36.289)
    var longitude: Double by mutableDoubleStateOf(127.389)
    var name: String by mutableStateOf("관측소 이름")
    var ratingSum: Int by mutableIntStateOf(47)
    var reviewCount: Int by mutableIntStateOf(10)

    // 현재 뷰모델에 관측포인트 정보 설정
    fun setSiteInfo(newInfo: SiteInfo) {
        latitude = newInfo.latitude
        longitude = newInfo.longitude
        name = newInfo.name
        ratingSum = newInfo.ratingSum
        reviewCount = newInfo.reviewCount
    }

    // 관측포인트 정보 가져오기
    suspend fun getSiteInfo(latitude: Double, longitude: Double) {
        val response =
            NetworkModule.provideRetrofitInstanceSite()
                .getSiteInfo(latitude + 0.0001, longitude + 0.0001)
        if (response?.code == 200) {
            setSiteInfo(response.data)
        }

    }


}
