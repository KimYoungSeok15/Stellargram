package com.ssafy.stellargram.ui.screen.home
import androidx.compose.foundation.DefaultMarqueeSpacing
import androidx.compose.foundation.DefaultMarqueeVelocity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.maps.android.compose.Circle
import com.ssafy.stellargram.R
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.Card
import com.ssafy.stellargram.model.CardsData
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoScrollingText() {
    var text = "231022-02:37금환일식 / 231029-08:48오리온자리유성우극대(ZHR=20)"
    Box(Modifier.fillMaxWidth()) {
        Row (

        ) {
            Text(
                text, Modifier
                    .basicMarquee(
                        iterations = Int.MAX_VALUE,
                        animationMode = MarqueeAnimationMode.Immediately,
                        spacing = MarqueeSpacing(20.dp),
                        velocity = 60.dp
                    )
            )
        }
    }
}

suspend fun GetTodaysPicture(): CardsData? {
    return try {
        val response = NetworkModule.provideRetrofitInstanceCards().getCards()
        if (response.code == 200) {
            response.data
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TodaysPicture() {
    var dummyCardsData by remember { mutableStateOf<CardsData?>(null) }

    LaunchedEffect(Unit) {
        // API에서 카드 정보 가져오기 (실제 API 호출)
        // val data = GetTodaysPicture()
        // dummyCardsData = data

        // 더미 데이터 (임시 하드코딩)
        val card = Card(
            cardId = 5,
            memberId = 99,
            memberNickName = "Hyundolee199543413413431",
            memberImagePath = "https://i.namu.wiki/i/hyYeK3WTj5JutQxaxAHHjFic9oAQ8kN4jdZo_MBGkzboWMtsr9pQN6JWeWgU9c8rmDon6XLlLhxuVrPbc6djcQ.gif",
            observeSiteId = "144",
            imagePath = "https://vinsweb.org/wp-content/uploads/2020/04/AtHome-NightSky-1080x810-1.jpg",
            content = "사진에 대한 설명123123사진에 대한 설명123123사진에 대한 설명123123사진에 대한 설명123123사진에 대한 설명123123",
            photoAt = "2023-10-27T01:49:22",
            category = "GALAXY",
            tools = "엄청 좋은 카메라",
            likeCount = 156,
            amILikeThis = false
        )

        val dummyCards = mutableListOf(card)
        dummyCardsData = CardsData(dummyCards)
    }

    // 제목 및 스타일 설정
    val title = "오늘의 사진"
    val titleTextStyle = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)

    Box(Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp) // 원하는 간격 설정
        ) {
            // 제목 표시
            Row(
                modifier = Modifier.padding(0.dp, 10.dp)
            ) {
                Text(
                    title,
                    style = titleTextStyle,
                )
            }

            // 카드 목록 표시
            dummyCardsData?.starcards?.forEach { card ->
                Row(
                    modifier = Modifier.padding(0.dp, 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // 회원 정보 표시 (이미지, 닉네임)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        GlideImage(
                            model = card.memberImagePath,
                            contentDescription = "123",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(30.dp) // 이미지 크기
                                .clip(CircleShape), // 동그라미 모양으로 잘라주기
                        )
                        Text(
                            text = card.memberNickName,
                            style = TextStyle(fontSize = 20.sp),
                            modifier = Modifier
                                .padding(start = 8.dp).width(150.dp),
                            overflow = TextOverflow.Ellipsis // 넘칠 경우 "..."으로 표시
                        )
                    }
                    val text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color(0xFF9DC4FF))) {
                            append("팔로우")
                        }
                    }
                    ClickableText(
                        text = text,
                        style = TextStyle(fontSize = 18.sp, textAlign = TextAlign.End),
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                        onClick = { offset ->
                            // Handle text click here
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                // 사진 표시
                GlideImage(
                    model = card.memberImagePath,
                    contentDescription = "123",
                    modifier = Modifier.fillMaxSize(),
                )

                // 좋아요 아이콘 및 텍스트
                val likeIcon = if (card.amILikeThis) {
                    painterResource(id = R.drawable.filledheart)
                } else {
                    painterResource(id = R.drawable.emptyheart)
                }
                Row(
                    modifier = Modifier.padding(0.dp, 4.dp)
                ) {
                    Image(
                        painter = likeIcon,
                        contentDescription = null, // 이미지 설명
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "좋아요 ${card.likeCount}",
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // 카드 내용 표시
                Text(
                    text = card.content,
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(0.dp, 8.dp)
                )
            }
        }
        if (dummyCardsData == null) {
            // 데이터를 아직 가져오지 않았을 때의 UI 처리
            // 예: 로딩 스피너 또는 메시지 표시
        } else {
            // cardsData를 사용하여 UI를 그리는 코드
            // ...
        }
    }
}