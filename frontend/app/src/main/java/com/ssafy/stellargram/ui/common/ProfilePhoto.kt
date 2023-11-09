package com.ssafy.stellargram.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.R
import com.ssafy.stellargram.ui.theme.profileSize

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfilePhoto(imgUrl: String?, imgSize: Int = profileSize, nickname: String = "유저의 닉네임") {

    // 수정자 선언
    var profileModifier: Modifier = Modifier
        .size(imgSize.dp) // 이미지 크기
        .clip(CircleShape) // 동그라미 모양으로 잘라주기

    // url 있으면 이미지, 없으면 기본 아이콘 반환
    if (imgUrl != null) {
        GlideImage(
            model = imgUrl,
            contentDescription = nickname,
            contentScale = ContentScale.Crop,
            modifier = profileModifier
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.account_active),
            contentDescription = "no image", // 이미지 설명
            modifier = profileModifier
        )
    }
}