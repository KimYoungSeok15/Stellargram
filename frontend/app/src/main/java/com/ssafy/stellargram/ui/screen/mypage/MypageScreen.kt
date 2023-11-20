package com.ssafy.stellargram.ui.screen.mypage

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.kakao.sdk.user.UserApiClient
import com.ssafy.stellargram.R
import com.ssafy.stellargram.StellargramApplication
import com.ssafy.stellargram.ui.Screen
import com.ssafy.stellargram.ui.screen.kakao.KakaoViewModel
import com.ssafy.stellargram.ui.screen.search.MainViewModel
import com.ssafy.stellargram.util.StarCardRepository
import java.io.File
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.key
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.window.Dialog
import com.ssafy.stellargram.ui.common.CustomTextButton
import com.ssafy.stellargram.ui.screen.base.BaseViewModel


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MypageScreen(navController: NavController, id:Long) {
    val viewModel: MypageViewModel = viewModel()
    val baseViewModel: BaseViewModel = viewModel()
    val tabIndex by viewModel.tabIndex.observeAsState()
    // 현재 사용자의 Id. 페이지 주인의 Id 아님.
    val userId = StellargramApplication.prefs.getString("memberId","").toLong()
    var cards by remember { mutableStateOf(viewModel.myCards) }
    var favStars by remember { mutableStateOf(viewModel.favStars) }
    var likeCards by remember { mutableStateOf(viewModel.likeCards) }

    var modalList = viewModel.modalList.value  // 모달에 들어갈 내용
    var isDialogOpen = baseViewModel.isDialogOpen // 프로필 수정 모달이 열려있는지
    var dialogVisible = viewModel.isActualDialogVisible
    var isLikeStar = viewModel.isLikeStar


    // LaunchedEffect를 사용하여 API 요청 트리거
    LaunchedEffect(true) {
        viewModel.getMemberInfo(id)
        val following = viewModel.getFollowingList(userId)  // Access followingList from ViewModel
        viewModel.updateFollowingList(following)
        getResults(viewModel = viewModel, id = id, followingList = following)
        val favoriteStars = viewModel.getFavoriteStars()
        viewModel.updateLikeStarIds(favoriteStars)
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
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(text = "게시물")
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable (
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ){
                                viewModel.getModalFollower(id)

                            }
                        ) {
                            Text(
                                text = it.followCount.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(text = "팔로워")
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable (
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ){
                                viewModel.getModalFollowing(id)
                            }
                        ) {
                            Text(
                                text = it.followingCount.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(text = "팔로잉")
                        }
                    }
                }
            }
            // TabLayout 함수를 호출하여 탭을 렌더링
            TabLayout(viewModel = viewModel)
                when (tabIndex) {
                    0 -> MyCardsScreen(viewModel, cards, navController)
                    1 -> FavStarsScreen(viewModel, favStars, navController)
                    2 -> LikeCardsScreen(viewModel, likeCards, navController)
                }
        }
    }
    // 모달 (좋아요 or 팔로워 or 팔로잉)
    if (viewModel.isDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                // 닫기 버튼을 클릭하면 Dialog를 닫음
                viewModel.isDialogVisible = false
            },
            title = {
                Text(viewModel.dialogTitle)
            },
            text = {
                // LazyColumn to display the list of likers
                LazyColumn {
                    val followingList = viewModel.followingList.value
                    items(modalList.size) { index ->
                        val member = modalList[index]
                        if (member != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // 회원 정보 표시 (이미지, 닉네임)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable {
                                        // 해당 유저 마이페이지로 이동. 단, 현재 페이지일 경우 무효
                                        val isMyCard = member.memberId == StellargramApplication.prefs.getString("memberId","").toLong()
                                        if (!isMyCard) {
                                            navController.navigate(Screen.MyPage.route + "/${member.memberId}")
                                        }
                                    }
                                ) {
                                    GlideImage(
                                        model = member.profileImageUrl,
                                        contentDescription = "Profile Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(30.dp) // 이미지 크기
                                            .clip(CircleShape) // 동그라미 모양으로 잘라주기
                                    )
                                    Text(
                                        text = member.nickname,
                                        style = TextStyle(fontSize = 20.sp),
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .width(150.dp)
                                    )
                                }
                                // 팔로우 관련 - 내 카드면 팔로우버튼 없고, 남의 카드면 팔로잉 중일 경우 언팔로우가 뜨도록
                                val isMyCard = member.memberId == StellargramApplication.prefs.getString("memberId","").toLong()
                                val isFollowing = followingList.contains(member.memberId)
                                Log.d("마이페이지", "isFollowing: ${member.memberId} $followingList ${isFollowing}")

                                val followText = buildAnnotatedString {
                                    if (!isMyCard) {
                                        withStyle(style = SpanStyle(color = if (isFollowing) Color(0xFFFF4040) else Color(0xFF9DC4FF))) {
                                            append(if (isFollowing) "언팔로우" else "팔로우")
                                        }
                                    }
                                }

                                if (!isMyCard) {
                                    ClickableText(
                                        text = followText,
                                        style = TextStyle(fontSize = 18.sp, textAlign = TextAlign.End),
                                        onClick = { offset ->
                                            // 팔로우 또는 언팔로우 이벤트 처리
                                            viewModel.handleFollowButtonClick(member.memberId, isFollowing)
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                } else {
                                    Spacer(modifier = Modifier.height(0.dp))
                                }
                            }
                        }
                    }
                }
            }
,
            confirmButton = {
                // 닫기 버튼
                CustomTextButton(
                    onClick = {
                        viewModel.isDialogVisible = false
                    },
                    text = "닫기"
                )
            }
        )
    }
    // 즐겨찾기 완료 모달
    if (dialogVisible) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = {
                dialogVisible = false
            },
            text = { Text(text = if (isLikeStar.value) "즐겨찾기 취소 완료" else "즐겨찾기 완료",
                fontSize = 20.sp)
            },
            confirmButton = {
                CustomTextButton(
                    onClick = {
                        viewModel.isActualDialogVisible = false
                    },
                    text = "확인"
                )
            }
        )
    }
//    // 프로필수정 모달
//    if (isDialogOpen) {
//        Log.d("프로필수정","11111")
//        val memberInfo = viewModel.memberResults.value
//        // Show the dialog
//        Dialog(onDismissRequest = { baseViewModel.closeProfileModificationDialog() }) {
//            Card(
//                modifier = Modifier
//                    .height(375.dp)
//                    .padding(16.dp),
//                shape = RoundedCornerShape(16.dp),
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Text(
//                        text = "프로필수정",
//                        modifier = Modifier.padding(16.dp),
//                    )
//                    GlideImage(
//                        model = memberInfo.firstOrNull()?.profileImageUrl,
//                        contentDescription = "프로필사진",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .size(100.dp) // 이미지 크기
//                            .clip(CircleShape), // 동그라미 모양으로 잘라주기
//                    )
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center,
//                    ) {
//                        TextButton(
//                            onClick = { baseViewModel.closeProfileModificationDialog() },
//                            modifier = Modifier.padding(8.dp),
//                        ) {
//                            Text("확인")
//                        }
//                        TextButton(
//                            onClick = { baseViewModel.closeProfileModificationDialog() },
//                            modifier = Modifier.padding(8.dp),
//                        ) {
//                            Text("취소")
//                        }
//                    }
//                }
//            }
//        }
//    }

}


