package com.ssafy.stellargram.ui.screen.chat;

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.ssafy.stellargram.ui.common.CustomTextButton
import com.ssafy.stellargram.ui.common.CustomTextButtonDark
import com.ssafy.stellargram.ui.theme.Constant
import com.ssafy.stellargram.ui.theme.Purple40
import com.ssafy.stellargram.ui.theme.Purple80

//@Preview(showBackground = true)
@Composable
fun SiteInfoBox(
    latitude: Double = 1000.0,
    longitude: Double = 1000.0,
    isChatScreen: Boolean = true,
    modifierIn: Modifier,
    observeSiteId:String="",
    navConroller:NavController
) {
    // 관측소 뷰모델 생성
    val viewModel: SiteViewModel = hiltViewModel()

    // 관측소 데이터 받아오기
    LaunchedEffect(key1 = true) {
        viewModel.getSiteInfo(observeSiteId)
    }

    // 별점 계산
    var rating: Float =
        ((Math.round((viewModel.ratingSum.toDouble() / viewModel.reviewCount.toDouble()) * 10)
            .toDouble()) / 10).toFloat()

    Column(
        modifier = modifierIn,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 관측소 이름
        Text(
            text = viewModel.name,
            style = TextStyle(fontSize = Constant.middleText.sp),
            color = Color.Black
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            // 점수
            Text(text = rating.toString(), color = Color.Black)

            // 별찍기
            RatingBar(
                value = rating,
                style = RatingBarStyle.Fill(activeColor = Purple40, inActiveColor = Purple80),
                onValueChange = {},
                onRatingChanged = {},
//                modifier = starModifier,
                spaceBetween = 4.dp,
                size = Constant.middleText.dp,

                )

            // 리뷰 수
            Text(text = "(${viewModel.reviewCount})", color = Color.Black)

//            // 채팅스크린이라면 리뷰 버튼
//            if (isChatScreen) {
//                CustomTextButtonDark(text = "탈퇴", onClick = { viewModel.leaveChatRoomByClick(observeSiteId,navConroller) })
//            }
//            // 리뷰스크린이라면 채팅버튼
//            else {
//                CustomTextButtonDark(text = "채팅", onClick = { /*TODO*/ })
//            }
        }
    }
}
