package com.ssafy.stellargram.ui.screen.kakao

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.ssafy.stellargram.StellargramApplication
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.MemberCheckResponse
import com.ssafy.stellargram.model.MemberMeResponse
import com.ssafy.stellargram.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class KakaoViewModel @Inject constructor(
) : ViewModel() {
    private val TAG = "KAKAO LOGIN"
    private val repository = AuthRepository(viewModelScope) // Create a repository for data operations


    /**
     * 카카오 서버단의 로그인 처리 작업,
     *
     * 성공: SDK를 통해 user의 id와 프로필 이미지를 받아 기기에 저장, 다음 단계로 이동
     *
     * 실패: 랜딩 페이지로 복귀
     */
    fun handlelogin(token: OAuthToken?, error: Throwable?, navController: NavController) {
        if (error != null) {
            // Handle login failure here
            Log.e(TAG, "카카오 서버 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오 서버 로그인 성공 ${token.accessToken}")
            // After successful login, request user information
            getUserProfile()
            memberCheck(navController)
        }
    }

    /**
     * User Id가 서비스 서버에 존재 하는지 확인
     */
    private fun memberCheck(
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.memberCheckRequest(){
                afterCheck(it,navController)
            }
        }
    }
    /**
     * memberCheck이후 필요에 따라 화면을 이동
     */
    private fun afterCheck(
        response: Response<MemberCheckResponse>,
        navController: NavController
    ){
        viewModelScope.launch(Dispatchers.IO) {
            if (response.isSuccessful){
                val getMemberCheck = response.body()
                if(getMemberCheck != null){
                    if (getMemberCheck.data.status) {
                        getMe()
                        withContext(Dispatchers.Main) {
                            navController.navigate(Screen.Home.route)
                        }
                    } else { //회원가입 필요
                        withContext(Dispatchers.Main) {
                            navController.navigate(Screen.SignUp.route)
                        }
                    }
                }
            }
        }
    }

    private fun getMe(){
        repository.memberMeRequest { response ->
            if(response.isSuccessful){
                val memberMeResponse = response.body()
                if (memberMeResponse != null){
                    StellargramApplication.prefs.setString("memberId",memberMeResponse.data.memberId.toString())
                    StellargramApplication.prefs.setString("nickname",memberMeResponse.data.nickname)
                    StellargramApplication.prefs.setString("profileImageUrl",memberMeResponse.data.profileImageUrl)
                    StellargramApplication.prefs.setString("followCount",memberMeResponse.data.followCount.toString())
                    StellargramApplication.prefs.setString("followingCount",memberMeResponse.data.followingCount.toString())
                    StellargramApplication.prefs.setString("cardCount",memberMeResponse.data.cardCount.toString())
                }
            }
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
                StellargramApplication.prefs.setString("myId", oauthIdentifier)
                StellargramApplication.prefs.setString("profileImageUrl",profileImageUrl)
            }
        }
    }
}

class AuthRepository(private val scope: CoroutineScope) {
    val tag = "LOGIN"
    fun memberCheckRequest( // User 정보가 서비스 DB에 있는지 조회
        onComplete: (Response<MemberCheckResponse>) -> Unit
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                val response = NetworkModule.provideRetrofitInstance().getMemberCheck()
                Log.d(tag,response.toString())
                onComplete(response)
            } catch (e: Exception) {
                Log.e(tag,e.toString())
            }
        }
    }

    fun memberMeRequest(
        onComplete: (Response<MemberMeResponse>) -> Unit
    ){
        scope.launch(Dispatchers.IO) {
            try {
                val response = NetworkModule.provideRetrofitInstance().getMemberMe()
                Log.d(tag,response.toString())
                onComplete(response)
            } catch (e:Exception) {
                Log.e(tag,e.toString())
            }
        }
    }
}