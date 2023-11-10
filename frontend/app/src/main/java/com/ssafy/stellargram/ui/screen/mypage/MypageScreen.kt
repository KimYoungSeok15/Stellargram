package com.ssafy.stellargram.ui.screen.mypage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.R
import com.ssafy.stellargram.ui.Screen
import com.ssafy.stellargram.ui.screen.kakao.KakaoViewModel
import java.io.File

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MypageScreen(navController: NavController) {
    val viewModel: MypageViewModel = viewModel()
    var activeTab by rememberSaveable { mutableIntStateOf(0) }

    // LaunchedEffect를 사용하여 API 요청 트리거
    LaunchedEffect(true) {
        viewModel.getMemberInfo("someText")
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier.padding(20.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val member = viewModel.memberResults.value.firstOrNull()

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(100.dp)
                ) {
                    GlideImage(
                        model = member?.profileImageUrl ?: "",
                        contentDescription = "프로필사진",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp) // 이미지 크기
                            .clip(CircleShape) // 동그라미 모양으로 잘라주기
                    )
                    Text(
                        text = member?.nickname ?: "",
                    )
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    member?.let {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = it.cardCount.toString(),
                                fontWeight= FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(text = "게시물")
                        }
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                text = it.followCount.toString(),
                                fontWeight= FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(text = "팔로우")
                        }
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                text = it.followingCount.toString(),
                                fontWeight= FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(text = "팔로잉")
                        }
                    }
                }
            }
            // TabLayout 함수를 호출하여 탭을 렌더링
            TabLayout(tabTitles = listOf("게시물", "팔로우", "팔로잉"), activeTab = activeTab) {
                // 각 탭에 따른 UI를 렌더링
                when (it) {
                    0 -> {
                        // 게시물 탭에 대한 내용 렌더링
                    }
                    1 -> {
                        // 팔로우 탭에 대한 내용 렌더링
                    }
                    2 -> {
                        // 팔로잉 탭에 대한 내용 렌더링
                    }
                }
            }
        }
    }
}