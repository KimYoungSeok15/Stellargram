package com.ssafy.stellargram.ui.screen.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.SiteInfo
import com.ssafy.stellargram.model.SiteInfoById
import com.ssafy.stellargram.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
    fun setSiteInfo(newInfo: SiteInfoById) {
        latitude = newInfo.latitude
        longitude = newInfo.longitude
        name = newInfo.name
        ratingSum = newInfo.ratingSum
        reviewCount = newInfo.reviewCount
    }

    // 관측포인트 정보 가져오기
    suspend fun getSiteInfo(observeSiteId: String) {
        try {
            val response =
                NetworkModule.provideRetrofitInstanceSite()
                    .getSiteInfoById(observeSiteId)
            if (response?.code == 200) {
                setSiteInfo(response.data)
            }
        } catch (e: Exception) {
            Log.d("관측포인트 정보 오류", e.toString())
        }


    }

    fun leaveChatRoomByClick(observeSiteId: String, navController: NavController) {
        viewModelScope.launch {
            try {
                Log.d("leave chat test",observeSiteId)
                val response =
                    NetworkModule.provideRetrofitInstanceChat().leaveChatRoom(observeSiteId)

                if (response?.code == 200) {
                    Log.d("leave chat room", "success")
                    navController.navigate("chatroomlist")

                }
            } catch (e: Exception) {
                Log.d("leave chat room error", e.toString())

            }
        }

    }


}
