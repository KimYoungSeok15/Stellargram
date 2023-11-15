package com.ssafy.stellargram.ui.screen.postcard

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.ssafy.stellargram.ui.screen.mypage.MypageViewModel

@Composable
fun PostCardScreen(navController: NavController){
    val viewModel: PostCardViewModel = viewModel()

    viewModel.uploadCard(uri= "https://stellagram-bucket-a101.s3.ap-northeast-2.amazonaws.com/starcard_image/%EB%B9%84%EC%A6%88%EB%8B%88%EC%8A%A4%20%EA%B6%8C%ED%95%9C%EC%B2%98%EB%A6%AC1698644053869.png", content= "내내내용", photoAt= "", category= "galaxy", tool= "", observeSiteId="")
}