package com.ssafy.instargram.ui.screen.kakao

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KakaoViewModel @Inject constructor() : ViewModel() {
    private val TAG = "KAKAO LOGIN"

    fun handlelogin(token: OAuthToken?, error: Throwable?, navController: NavController) {
        if (error != null) {
            Log.e(TAG, "로그인 실패", error)
            // Handle login failure here
        } else if (token != null) {
            Log.i(TAG, "로그인 성공 ${token.accessToken}")
            // After successful login, request user information
//            retrieveUserInfo(token, navController)
        }
    }

}