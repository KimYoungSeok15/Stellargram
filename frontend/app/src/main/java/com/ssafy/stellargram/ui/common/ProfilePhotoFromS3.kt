package com.ssafy.stellargram.ui.common

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfilePhoto(imgUrl:String = "", imgSize:Int = 30, description: String = "이미지 설명") {
    GlideImage(
        model = imgUrl,
        contentDescription = description,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(imgSize.dp) // 이미지 크기
            .clip(CircleShape), // 동그라미 모양으로 잘라주기
    )
}