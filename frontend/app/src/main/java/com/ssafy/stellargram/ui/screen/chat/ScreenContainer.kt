package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ssafy.stellargram.model.Constant

@Composable
fun ScreenContainer(
    customChild: @Composable ColumnScope.() -> Unit,
    isChatScreen: Boolean = true,
    latitude: Double = 1000.0,
    longitude: Double = 1000.0,
) {
    // css. 스크린 컨테이너 modifier
    val screenModifier: Modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 30.dp)

    // css. 흰 박스 modifier
    val whiteRoundModifier: Modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(Constant.boxCornerSize.dp))
        .background(Color.White)
        .padding(10.dp)

    // 스크린 전체
    Column(modifier = screenModifier, verticalArrangement = Arrangement.SpaceAround) {

        // 관측포인트 컨테이너
        SiteInfoBox(
            modifierIn = whiteRoundModifier,
            isChatScreen = isChatScreen,
            longitude = longitude,
            latitude = latitude
        )

        // 사이
        Spacer(modifier = Modifier.height(10.dp))

        // 하위 내용 컨테이너
        Column(modifier = whiteRoundModifier) {
            // 전달된 하위 내용
            customChild()
        }
    }
}