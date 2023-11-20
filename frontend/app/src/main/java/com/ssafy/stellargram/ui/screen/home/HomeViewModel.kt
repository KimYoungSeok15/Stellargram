package com.ssafy.stellargram.ui.screen.home
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.R
import com.ssafy.stellargram.StellargramApplication
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.BestCard
import com.ssafy.stellargram.model.Card
import com.ssafy.stellargram.model.CardsData
import com.ssafy.stellargram.model.CardsResponse
import com.ssafy.stellargram.model.FollowCancelResponse
import com.ssafy.stellargram.model.MemberCheckResponse
import com.ssafy.stellargram.model.Response
import com.ssafy.stellargram.model.StarCards
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

// 기상청에 맞게 위도 경도를 수정하기 위한 상수들
const val COEFFICIENT_TO_RADIAN = Math.PI / 180.0
const val GRID_UNIT_COUNT = 6371.00877 / 5.0
const val REF_X = 43.0
const val REF_Y = 136.0
const val REF_LON_RAD = 126.0 * COEFFICIENT_TO_RADIAN
const val REF_LAT_RAD = 38.0 * COEFFICIENT_TO_RADIAN
const val PROJ_LAT_1_RAD = 30.0 * COEFFICIENT_TO_RADIAN
const val PROJ_LAT_2_RAD = 60.0 * COEFFICIENT_TO_RADIAN


// api에 담을 nx, ny가 담긴 변수
data class CoordinatesXy(val nx: Int, val ny: Int)
data class CoordinatesLatLon(val lat: Double, val lon: Double)

// 경도 위도 -> nx, ny 변환 or 반대도 가능한 함수
class CoordinateConverter {
    private val sn = ln(cos(PROJ_LAT_1_RAD) / cos(PROJ_LAT_2_RAD)) / ln(tan(Math.PI * 0.25 + PROJ_LAT_2_RAD * 0.5) / tan(Math.PI * 0.25 + PROJ_LAT_1_RAD * 0.5))
    private val sf = tan(Math.PI * 0.25 + PROJ_LAT_1_RAD * 0.5).pow(sn) * cos(PROJ_LAT_1_RAD) / sn
    private val ro = GRID_UNIT_COUNT * sf / tan(Math.PI * 0.25 + REF_LAT_RAD * 0.5).pow(sn)

    // 위경도 -> xy
    internal fun convertToXy(lat: Double, lon: Double): CoordinatesXy {
        val ra = GRID_UNIT_COUNT * sf / tan(Math.PI * 0.25 + lat * COEFFICIENT_TO_RADIAN * 0.5).pow(sn)
        val theta: Double = lon * COEFFICIENT_TO_RADIAN - REF_LON_RAD
        val niceTheta = if (theta < -Math.PI) {
            theta + 2 * Math.PI
        } else if (theta > Math.PI) {
            theta - 2 * Math.PI
        } else theta

        return CoordinatesXy(
            nx = floor(ra * sin(niceTheta * sn) + REF_X + 0.5).toInt(),
            ny = floor(ro - ra * cos(niceTheta * sn) + REF_Y + 0.5).toInt()
        )
    }

    //xy -> 위경도
    internal fun convertToLatLon(nx: Double, ny: Double): CoordinatesLatLon {
        val diffX: Double = nx - REF_X
        val diffY: Double = ro - ny + REF_Y
        val distance = sqrt(diffX * diffX + diffY * diffY)
        val latSign: Int = if (sn < 0) -1 else 1
        val latRad = 2 * atan((GRID_UNIT_COUNT * sf / distance).pow(1.0 / sn)) - Math.PI * 0.5

        val theta: Double = if (abs(diffX) <= 0) 0.0 else {
            if (abs(diffY) <= 0) {
                if (diffX < 0) -Math.PI * 0.5 else Math.PI * 0.5
            } else atan2(diffX, diffY)
        }

        val lonRad = theta / sn + REF_LON_RAD

        return CoordinatesLatLon(
            lat = (latRad * latSign) / COEFFICIENT_TO_RADIAN,
            lon = lonRad / COEFFICIENT_TO_RADIAN
        )
    }
}

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

}

// 현재 주소를 위경도를 통해 받아오는 함수
fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    var addressText = ""
    try {
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            // 주소를 원하는 형식으로 조합하거나 필요한 부분만 추출할 수 있습니다.
            addressText = address.thoroughfare ?: "주소 정보 없음"
        } else {
            addressText = "주소 정보 없음"
        }
    } catch (e: IOException) {
        Log.d("Location1", "위치정보 받아오기 실패")
        e.printStackTrace()
        addressText = "주소 정보 없음"
    }
    Log.d("Location1", "getAddressFromLocation: $addressText")
    return addressText
}

// 천문현상 가로 자동스크롤링 UI
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoScrollingText(text:String) {
    var text : String = text
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


// 오늘의 추천 사진 UI
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TodaysPicture(viewModel: HomeViewModel) {
    var cardData by remember { mutableStateOf<StarCards?>(null) }

    // 추천카드 정보 가져오기
    LaunchedEffect(Unit) {
        try {
            val response = NetworkModule.provideRetrofitCards().recommendCard()

            if (response.isSuccessful) {
                val cardResponse = response.body()
                cardData = cardResponse?.data
                Log.d("추천", "$cardData")
            } else {
                Log.e("API Error", "API request failed with code: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("Network Error", "Failed to fetch data: ${e.message}", e)
        }
    }

    // 제목 및 스타일 설정
    val title = "오늘의 사진"
    val titleTextStyle = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)

    // 팔로우 또는 언팔로우 클릭시 실행하는 부분
    fun handleFollowButtonClick(memberId: Long, isFollowing: Boolean) {
        Log.d("메인","디버그 $memberId $isFollowing")
        try {
            if (cardData?.starcard != null) {
                if (memberId != StellargramApplication.prefs.getString("memberId", "").toLong()) {
                    if (isFollowing) {
                        // Unfollow action
                        viewModel.viewModelScope.launch {
                            val response = unfollowUser(memberId)
                            if (response.isSuccessful) {
                                cardData = cardData?.copy(
                                    starcard = cardData?.starcard?.copy(isFollowing = false)
                                )
                            } else {
                                // Handle unfollow failure
                            }
                        }
                    } else {
                        // Follow action
                        viewModel.viewModelScope.launch {
                            val response = followUser(memberId)
                            if (response.isSuccessful) {
                                cardData = cardData?.copy(
                                    starcard = cardData?.starcard?.copy(isFollowing = true)
                                )
                            } else {
                                // Handle follow failure
                            }
                        }
                    }
                } else {
                    // Handle if memberId is not the same as the logged-in user's memberId
                }
            }
        } catch (e: Exception) {
            // Handle exception
        }
    }

    // 좋아요 버튼 클릭시 실행하는 부분
    fun handleLikeButtonClick(card: BestCard?, isLiked: Boolean) {
        try {
            if (card != null && cardData?.starcard != null) {
                viewModel.viewModelScope.launch {
                    try {
                        val response = NetworkModule.provideRetrofitCards().likeCard(cardId = card.cardId)
                        if (response.isSuccessful) {
                            cardData = cardData?.copy(
                                starcard = cardData?.starcard?.copy(
                                    likeCount = if (!isLiked) cardData?.starcard?.likeCount?.plus(1) else cardData?.starcard?.likeCount?.minus(1),
                                    amILikeThis = !isLiked
                                )
                            )
                        } else {
                            // Handle failure
                        }
                    } catch (e: Exception) {
                        // Handle exception
                    }
                }
            }
        } catch (e: Exception) {
            // Handle exception
        }
    }

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

            val card = cardData?.starcard
            Row(
                modifier = Modifier.padding(0.dp, 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 회원 정보 표시 (이미지, 닉네임)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (card != null) {
                        GlideImage(
                            model = card.memberProfileImageUrl,
                            contentDescription = "123",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(30.dp) // 이미지 크기
                                .clip(CircleShape), // 동그라미 모양으로 잘라주기
                        )
                    }
                    if (card != null) {
                        Text(
                            text = card.memberNickname,
                            style = TextStyle(fontSize = 20.sp),
                            modifier = Modifier
                                .padding(start = 8.dp).width(150.dp),
                            overflow = TextOverflow.Ellipsis // 넘칠 경우 "..."으로 표시
                        )
                    }
                }
                val text = buildAnnotatedString {
                    if (card != null) {
                        withStyle(style = SpanStyle(color = if (card.isFollowing) Color(0xFFFF4040) else Color(0xFF9DC4FF))) {
                            if (card.isFollowing) {
                                append("언팔로우")
                            } else {
                                append("팔로우")
                            }
                        }
                    }
                }
                ClickableText(
                    text = text,
                    style = TextStyle(fontSize = 18.sp, textAlign = TextAlign.End),
                    softWrap = true,
                    overflow = TextOverflow.Clip,
                    onClick = { offset ->
                        if (card != null) {
                            handleFollowButtonClick(card.memberId, card.isFollowing)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

                // 사진 표시
            if (card != null) {
                GlideImage(
                    model = card.memberProfileImageUrl,
                    contentDescription = "123",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth,
                )
            }

            // 좋아요 아이콘 및 텍스트
            val likeIcon = if (card?.amILikeThis == true) {
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
                        .clickable (
                            interactionSource = remember{ MutableInteractionSource() },
                            indication = null)
                        {
                            if (card != null) {
                                handleLikeButtonClick(card = card, isLiked = card.amILikeThis)
                            }
                        }
                )
                if (card != null) {
                    Text(
                        text = "좋아요 ${card.likeCount}",
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            // 카드 내용 표시
            if (card != null) {
                Text(
                    text = card.content,
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier.padding(0.dp, 8.dp)
                )
            }
        }
    }
    if (cardData == null) {
        // 데이터를 아직 가져오지 않았을 때의 UI 처리
        // 예: 로딩 스피너 또는 메시지 표시
    } else {
        // cardsData를 사용하여 UI를 그리는 코드
        // ...
    }
}


suspend fun unfollowUser(memberId: Long): retrofit2.Response<FollowCancelResponse> {
    return NetworkModule.provideRetrofitInstance().unfollowUser(followingId = memberId)
}

suspend fun followUser(memberId: Long): retrofit2.Response<MemberCheckResponse> {
    return NetworkModule.provideRetrofitInstance().followUser(followingId = memberId)
}

