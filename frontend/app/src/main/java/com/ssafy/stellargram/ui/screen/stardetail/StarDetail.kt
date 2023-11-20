package com.ssafy.stellargram.ui.screen.stardetail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.R
import com.ssafy.stellargram.StellargramApplication
import com.ssafy.stellargram.module.DBModule
import com.ssafy.stellargram.ui.common.CustomTextButton

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StarDetailScreen(navController: NavController, id: Int) {
    val starViewModel: StarDetailViewModel = viewModel()
    var star = DBModule.starMap[id]
    val myId = StellargramApplication.prefs.getString("memberId","").toLong()
    var dialogVisible by remember { mutableStateOf(false) }

    val imageUrl: String
    val description: String
    when (id) {
        32263 -> {
            imageUrl = "https://www.astronomy.com/wp-content/uploads/sites/2/2023/03/ASYSK1221_03.jpg?fit=600%2C771"
            description = "시리우스(Sirius)는 큰개자리 알파별(α Canis Majoris, α CMa)로, 밤 하늘에서 가장 밝은 별이다. 우리말로는 천랑성(天狼星)이라고 한다. 알파 센타우리와 함께 일반적으로 가장 잘 알려진 항성이다. 밤 하늘에서 가장 밝은 별이기 때문에 고대 이집트 시대부터 중요한 관찰 대상이었다. 특히 고대 이집트 문명에서는 해가 뜨기 전 새벽 시리우스가 동쪽하늘에서 떠오르는 시기에 나일강이 범람한다는 관계에서 알 수 있다."
        }
        11734 -> {
            imageUrl = "https://mblogthumb-phinf.pstatic.net/MjAxNzAyMjRfMjcy/MDAxNDg3OTEwMzM4MDg4.D3qbWLYlbLUrjJEl8r6-feFuGbfoKdhI0GudrjjHkqUg.rE-8LqrXLQ5AOfw6SULN3owzEWBSK9UHuTaAIYio_acg.JPEG.mozmov/Polaris-01w.jpg?type=w800"
            description = "폴라리스(Polaris)는 작은곰자리에서 가장 밝은 별(알파성)로, 현재의 북극성이기도 하다. 서기 3000년 경 이후에는 북극성에서 벗어난다. 하나의 별처럼 보이지만 사실은 다중성으로, 초거성 폴라리스 Aa가 다른 별들을 거느리고 있다."
        }
        else -> {
            imageUrl = "https://image.librewiki.net/c/c5/Vega.jpg"
            description = ""
        }
    }
    val likeStarIds = starViewModel.likeStarIds
    LaunchedEffect(Unit) {
        val favoriteStars = starViewModel.getFavoriteStars()
        starViewModel.updateLikeStarIds(favoriteStars)
    }

    // LazyColumn으로 변경
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(star != null){
            item {
                Text(text = "${DBModule.nameMap[id]}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                GlideImage(
                    model = imageUrl,
                    contentDescription = "설명",
                    modifier = Modifier.padding(0.dp, 20.dp),
                    contentScale = ContentScale.FillWidth
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 4.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (likeStarIds.contains(id)) {
                                starViewModel.unfavoriteStar(id)
                                dialogVisible = true
                            } else {
                                starViewModel.favoriteStar(id)
                                dialogVisible = true
                            }
                        },
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = if (likeStarIds.contains(id)) painterResource(R.drawable.like) else painterResource(R.drawable.unfilledstar),
                        contentDescription = null, // 이미지 설명
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = if (likeStarIds.contains(id)) "즐겨찾기 취소" else "즐겨찾기",
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            // 최상위 Row 추가
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 첫 번째 Column (별자리, 적경, 적위 등)
                    Column {
                        star?.let { details ->
                            Text(text = "별자리", fontSize = 20.sp)
                            Text(text = "적경(deg)", fontSize = 20.sp)
                            Text(text = "적위(deg)", fontSize = 20.sp)
                            Text(text = "겉보기등급(mag)", fontSize = 20.sp)
                            Text(text = "절대등급(mag)", fontSize = 20.sp)
                            Text(text = "거리(pc)", fontSize = 20.sp)
                            Text(text = "항성 분류", fontSize = 20.sp)
                        }
                    }

                    // 두 번째 Column (details.constellation, details.rightAscension 등)
                    Column {
                        star?.let { details ->
                            Text(text = details.con?:"", fontSize = 20.sp)
                            Text(text = details.ra.toString(), fontSize = 20.sp)
                            Text(text = details.dec.toString(), fontSize = 20.sp)
                            Text(text = details.mag.toString(), fontSize = 20.sp)
                            Text(text = details.absmag.toString(), fontSize = 20.sp)
                            Text(text = details.dist.toString(), fontSize = 20.sp)
                            Text(text = details.spect?:"", fontSize = 20.sp)
                        }
                    }
                }
            }

            item {
                // 설명
                Text(
                    text = description,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
        }
        else{
            item {
                //TODO: id에 해당하는 데이터 없을 때 data not found page를 띄워주세요!
                Text(text = "해당 별에 대한 정보가 없습니다!")
            }
        }
    }
    if (dialogVisible) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = {
                dialogVisible = false
            },
            text = { Text(text = if (likeStarIds.contains(id)) "즐겨찾기 완료" else "즐겨찾기 취소 완료",
                        fontSize = 20.sp)
                   },
            confirmButton = {
                CustomTextButton(
                    onClick = {
                        dialogVisible = false
                    },
                    text = "확인"
                )
            }
        )
    }
}
