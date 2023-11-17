package com.ssafy.stellargram.ui.screen.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ssafy.stellargram.StellargramApplication
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.MemberCheckDuplicateRequest
import com.ssafy.stellargram.model.MemberCheckDuplicateResponse
import com.ssafy.stellargram.model.MemberSignUpRequest
import com.ssafy.stellargram.model.MemberSignUpResponse
import com.ssafy.stellargram.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
) : ViewModel() {

    private val repository = SignUpRepository(viewModelScope)
    var textIpt by  mutableStateOf("")
    var profileImageUrl by mutableStateOf(StellargramApplication.prefs.getString("profileImageUrl",""))
    var Nickname_isvalid by  mutableStateOf(true)
    var Nickname_isChecked by mutableStateOf(false)
    var dialogMessage by mutableStateOf("")

    fun IptisValid(){
        if (textIpt.isEmpty()){
            return
        }
        val memberCheckDuplicateRequest = MemberCheckDuplicateRequest(
            nickname = textIpt
        )
        viewModelScope.launch(Dispatchers.IO){
            repository.checkDuplicate(memberCheckDuplicateRequest){ response ->
                Log.d("SIGNUP","$response")
                try {
                    if (response.isSuccessful){
                        val checkStatus = response.body()
                        if(checkStatus != null){
                            Nickname_isvalid = checkStatus.data.status
                            Nickname_isChecked = Nickname_isvalid
                        }
                    }
                } catch (e :Exception){
                    Log.e("SIGNUP",e.toString())
                }
            }
        }
    }

    fun handleSubmit(navController: NavController){
        if(Nickname_isChecked && textIpt.isNotEmpty() && profileImageUrl.isNotEmpty()){
            val memberSignUpRequest = MemberSignUpRequest(nickname = textIpt, profileImageUrl = profileImageUrl)
            SignInSubmit(memberSignUpRequest, navController)
        } else {
            dialogMessage = "입력이 올바르지 않습니다."
            //TO DO - dialog 기능 추가 구현 예정
        }

    }

    private fun SignInSubmit(memberSignUpRequest: MemberSignUpRequest, navController: NavController){
        repository.submitSignIn(memberSignUpRequest){ response ->
            if(response.isSuccessful){
                val memberSignUpResponse = response.body()
                if(memberSignUpResponse != null){
                    StellargramApplication.prefs.setString("memberId",memberSignUpResponse.data.memberId.toString())
                    StellargramApplication.prefs.setString("nickname",memberSignUpResponse.data.nickname)
                    StellargramApplication.prefs.setString("profileImageUrl",memberSignUpResponse.data.profileImageUrl)
                    StellargramApplication.prefs.setString("followCount",memberSignUpResponse.data.followCount.toString())
                    StellargramApplication.prefs.setString("followingCount",memberSignUpResponse.data.followingCount.toString())
                    StellargramApplication.prefs.setString("cardCount",memberSignUpResponse.data.cardCount.toString())
                    afterSignIn(navController)
                }
            }
        }
    }

    private fun afterSignIn(navController: NavController){
        viewModelScope.launch(Dispatchers.Main){
            navController.navigate(Screen.Home.route)
        }
    }

}

class SignUpRepository(private val scope: CoroutineScope){
    fun checkDuplicate(
        getMemberCheckDuplicateRequest : MemberCheckDuplicateRequest,
        onComplete: (Response<MemberCheckDuplicateResponse>) -> Unit
    ){
        scope.launch(Dispatchers.IO) {
                Log.d("SIGNUP",getMemberCheckDuplicateRequest.toString())
                val response = NetworkModule.provideRetrofitInstance().getMemberCheckDuplicate(getMemberCheckDuplicateRequest)
                Log.d("SIGNUP",response.toString())
                onComplete(response)
        }
    }

    fun submitSignIn(
        postMemberSignUpRequest : MemberSignUpRequest,
        onComplete: (Response<MemberSignUpResponse>) -> Unit
    ){
        scope.launch(Dispatchers.IO) {
            val response = NetworkModule.provideRetrofitInstance().postMemberSignUP(postMemberSignUpRequest)
            onComplete(response)
        }
    }
}
