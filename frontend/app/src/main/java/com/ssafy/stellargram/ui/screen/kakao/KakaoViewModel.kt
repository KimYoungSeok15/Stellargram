package com.ssafy.stellargram.ui.screen.kakao

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.ssafy.stellargram.StellargramApplication
import com.ssafy.stellargram.data.remote.ApiService
import com.ssafy.stellargram.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KakaoViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    private val TAG = "KAKAO LOGIN"


    /**
     * 카카오 서버단의 로그인 처리 작업,
     *
     * 성공: SDK를 통해 user의 id와 프로필 이미지를 받아 기기에 저장, 다음 단계로 이동
     *
     * 실패: 랜딩 페이지로 복귀
     */
    fun handlelogin(token: OAuthToken?, error: Throwable?, navController: NavController) {
        if (error != null) {
            Log.e(TAG, "카카오 서버 로그인 실패", error)
            navController.navigate(Screen.Landing.route)
            // Handle login failure here
        } else if (token != null) {
            Log.i(TAG, "카카오 서버 로그인 성공 ${token.accessToken}")
            // After successful login, request user information
            getUserProfile()
            navController.navigate(Screen.Home.route)
        }
    }

    /**
     * 유저의 고유 id(INTEGER)와 프로필 url(String) 반환
     */
    private fun getUserProfile() {
        UserApiClient.instance.me { user, error ->
            if(error != null){
                // Handle error
            } else if (user != null){
                val oauthIdentifier = user.id.toString()
                val profileImageUrl = user.properties?.get("profile_image") ?: ""
                StellargramApplication.prefs.setString("myId","$oauthIdentifier")
                Log.d(TAG, profileImageUrl)
            }
        }
    }

    /**
     * User Id가 서비스 서버에 존재 하는지 확인
     */
    private fun checkUserInfoExist() {
        val response = apiService
    }

    /**
     * 제공 받은 유저 정보로 회원 가입 진행
     */
    private fun postSignUp() {

    }

}