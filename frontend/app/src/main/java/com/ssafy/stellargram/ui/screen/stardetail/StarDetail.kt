package com.ssafy.stellargram.ui.screen.stardetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.model.Star
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StarDetailScreen(navController: NavController, id: Int) {
    val starViewModel: StarDetailViewModel = viewModel()
    var starDetails by remember { mutableStateOf<Star?>(null) }

    // 뷰모델을 초기화하고 데이터를 가져오는 블록
    LaunchedEffect(true) {
        // 비동기 작업을 수행하여 별자리 세부 정보를 가져옵니다.
        val fetchedStarDetails = withContext(Dispatchers.IO) {
            starViewModel.getStarResults(name)
        }
        // 가져온 데이터를 UI에 업데이트
        starDetails = fetchedStarDetails.firstOrNull()
    }

    // LazyColumn으로 변경
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(text = "${starDetails?.name}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            GlideImage(
                model = "https://image.librewiki.net/c/c5/Vega.jpg",
                contentDescription = "설명",
                modifier = Modifier.padding(0.dp, 20.dp)
            )
        }

        // 최상위 Row 추가
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 첫 번째 Column (별자리, 적경, 적위 등)
                Column {
                    starDetails?.let { details ->
                        Text(text = "별자리", fontSize = 20.sp)
                        Text(text = "적경", fontSize = 20.sp)
                        Text(text = "적위", fontSize = 20.sp)
                        Text(text = "겉보기등급", fontSize = 20.sp)
                        Text(text = "절대등급", fontSize = 20.sp)
                        Text(text = "거리", fontSize = 20.sp)
                        Text(text = "항성 분류", fontSize = 20.sp)
                    }
                }

                // 두 번째 Column (details.constellation, details.rightAscension 등)
                Column {
                    starDetails?.let { details ->
                        Text(text = details.constellation, fontSize = 20.sp)
                        Text(text = details.rightAscension, fontSize = 20.sp)
                        Text(text = details.declination, fontSize = 20.sp)
                        Text(text = details.apparentMagnitude, fontSize = 20.sp)
                        Text(text = details.absoluteMagnitude, fontSize = 20.sp)
                        Text(text = details.distanceLightYear, fontSize = 20.sp)
                        Text(text = details.spectralClass, fontSize = 20.sp)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            // 설명
            Text(
                text = "베가(Vega)는 거문고자리 알파별(α Lyrae, α Lyr)로, 알타이르, 데네브와 함께 여름의 대삼각형을 이루는 0등급 별이다. 직녀성이라고도 잘 알려져 있다. " +
                        "베가(Vega)는 거문고자리 알파별(α Lyrae, α Lyr)로, 알타이르, 데네브와 함께 여름의 대삼각형을 이루는 0등급 별이다. 직녀성이라고도 잘 알려져 있다.",
                modifier = Modifier.padding(0.dp, 20.dp)
            )
        }
    }
}
