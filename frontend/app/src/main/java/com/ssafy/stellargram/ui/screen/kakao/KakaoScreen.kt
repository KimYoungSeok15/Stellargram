package com.ssafy.stellargram.ui.screen.kakao

import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kakao.sdk.user.UserApiClient

@Composable
fun KakaoScreen(navController: NavController){
    val viewModel: KakaoViewModel = viewModel() // Create an instance of AuthViewModel
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                setLoginClickListener(navController, viewModel) // Pass the ViewModel instance
            }
        }
    )
}

fun WebView.setLoginClickListener(navController: NavController ,viewModel: KakaoViewModel) {
    // Check if KakaoTalk login is available
    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            viewModel.handlelogin(token, error, navController)
        }
    } else {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            viewModel.handlelogin(token, error, navController)
        }
    }
}