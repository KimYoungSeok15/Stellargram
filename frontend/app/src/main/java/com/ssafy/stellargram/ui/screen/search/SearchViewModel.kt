package com.ssafy.stellargram.ui.screen.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.R
import com.ssafy.stellargram.data.db.entity.Star
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.Card
import com.ssafy.stellargram.model.CardResponse
import com.ssafy.stellargram.model.Member
import com.ssafy.stellargram.model.MemberSearchRequest
import com.ssafy.stellargram.module.DBModule
import com.ssafy.stellargram.ui.Screen
import com.ssafy.stellargram.util.SearchStarByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

// --------------------------------------탭관련-------------------------------------------
// 탭 컨테이너
@Composable
fun TabLayout(viewModel: MainViewModel) {
    val tabIndex = viewModel.tabIndex.observeAsState()
    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex.value!!) {
            viewModel.tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex.value!! == index,
                    onClick = { viewModel.updateTabIndex(index) },
                )
            }
        }
    }
}

// 게시물 탭
@Composable
fun ArticleScreen(
    viewModel: MainViewModel,
    cardResultsState: MutableState<List<Card>>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .draggable(
                state = viewModel.dragState.value!!,
                orientation = Orientation.Horizontal,
                onDragStarted = { },
                onDragStopped = {
                    viewModel.updateTabIndexBasedOnSwipe(it)
                }),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ArticleUI(cardsState = cardResultsState, navController)
    }
}

// 계정 탭
@Composable
fun AccountScreen(
    viewModel: MainViewModel,
    accountCardsState: MutableState<List<Member>>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .draggable(
                state = viewModel.dragState.value!!,
                orientation = Orientation.Horizontal,
                onDragStarted = { },
                onDragStopped = {
                    viewModel.updateTabIndexBasedOnSwipe(it)
                }),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AccountUI(
            accountCardState = accountCardsState,
            navController
        ) // accountCardsState.value 대신 accountCardsState 전달
    }
}

// 별 탭
@Composable
fun StarScreen(
    viewModel: MainViewModel,
    starCardsState: MutableState<List<Star>>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .draggable(
                state = viewModel.dragState.value!!,
                orientation = Orientation.Horizontal,
                onDragStarted = { },
                onDragStopped = {
                    viewModel.updateTabIndexBasedOnSwipe(it)
                }),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StarUI(starCardsState = starCardsState, navController)
    }
}

// 탭의 뼈대와 동작을 정의
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    // name으로 star를 찾기위한 함수를 쓰기 위해 선언.
    val searchStars: SearchStarByName = SearchStarByName()

    private val _tabIndex: MutableLiveData<Int> = MutableLiveData(0)
    var text by mutableStateOf("")
    val tabIndex: LiveData<Int> = _tabIndex
    val tabs = listOf("게시물", "유저", "별")

    var isSwipeToTheLeft: Boolean = false
    private val draggableState = DraggableState { delta ->
        isSwipeToTheLeft = delta > 0
    }

    private val _dragState = MutableLiveData<DraggableState>(draggableState)
    val dragState: LiveData<DraggableState> = _dragState

    fun updateTabIndexBasedOnSwipe(delta: Float) {
        if (delta > 0 && _tabIndex.value!! > 0) {
            // Swipe to the right
            _tabIndex.value = _tabIndex.value!! - 1
        } else if (delta < 0 && _tabIndex.value!! < tabs.size - 1) {
            // Swipe to the left
            _tabIndex.value = _tabIndex.value!! + 1
        }
    }


    fun updateTabIndex(i: Int) {
        _tabIndex.value = i
    }

    var cardResults = mutableStateOf<List<Card>>(emptyList())
    var memberResults = mutableStateOf<List<Member>>(emptyList())
    var starResults = mutableStateOf<List<Star>>(emptyList())
    suspend fun getCardResults(text: String): List<Card> {
        val result: MutableList<Card> = mutableStateListOf()

        try {
            // 카드 검색 로직
            // TODO: category관련 상의할 것. all은 없나? 일단 galaxy 고정하기로
            val response =
                NetworkModule.provideRetrofitCards().searchStarCards(text, "galaxy")

            if (response?.code == 200) {
                val cardList: List<CardResponse> = response.data.starcards

                for (card in cardList) {
                    // 각 카드에 대해 유저id로 검색
                    val responseMember =
                        NetworkModule.RetrofitGetMemberInfo().getMember(card.memberId)

                    if (responseMember.isSuccessful) {
                        val newCardInfo: Card = Card(
                            cardId = card.cardId,
                            memberId = card.memberId,
                            memberNickname = responseMember.body()!!.data.nickname,
                            memberProfileImageUrl = responseMember.body()!!.data.profileImageUrl,
                            observeSiteId = card.observeSiteId,
                            imagePath = card.imagePath,
                            imageUrl = card.imageUrl,
                            content = card.content,
                            photoAt = card.photoAt,
                            category = card.category,
                            tools = card.tools,
                            likeCount = card.likeCount,
                            amILikeThis = card.amILikeThis,
                            isFollowing = false,
                        )

                        result.add(newCardInfo)
                    }
                }
            } else if (response.code == 500) {
                Log.e("SEARCH CARD", "500 error")

            }
        } catch (e: Exception) {
            Log.e("SEARCH CARD", e.toString())
        }

        return result
    }

    suspend fun getMemberResults(text: String): List<Member> {

        var result: List<Member> = emptyList()

        try {
            // 멤버 검색 로직
            val response = NetworkModule.RetrofitGetMemberInfo()
                .findMember(MemberSearchRequest(searchNickname = text))

            if (response?.code == 200) {
                result = response.data.members

            } else if (response.code == 500) {
                Log.e("SEARCH CARD", "500 error")

            }
        } catch (e: Exception) {
            Log.e("SEARCH CARD", e.toString())
        }
        return result
    }

    fun getStarResults(text: String): List<Star> {
        // 별 검색 로직
        // text를 포함하고 있는 모든 별들을 반환.
        val results = searchStars.searchByName(text)
        return results.toList()
    }
}
//----------------------------------------이상 탭관련----------------------------------------

//---------------------------------------검색결과-------------------------------------------
suspend fun getSearchResults(text: String, mainViewModel: MainViewModel): List<Any> =
    coroutineScope {
        val cardDeferred = async { mainViewModel.getCardResults(text) }
        val memberDeferred = async { mainViewModel.getMemberResults(text) }
        val starDeferred = async { mainViewModel.getStarResults(text) }

        val cardResults = cardDeferred.await()
        val memberResults = memberDeferred.await()
        val starResults = starDeferred.await()

        mainViewModel.cardResults.value = cardResults
        mainViewModel.memberResults.value = memberResults
        mainViewModel.starResults.value = starResults

        val results = mutableListOf<Any>()
        results.addAll(cardResults)
        results.addAll(memberResults)
        results.addAll(starResults)

        results
    }

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ArticleUI(cardsState: MutableState<List<Card>>, navController: NavController) {
    // 각 검색 결과를 표시하는 UI 컴포넌트
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(cardsState.value.size) { index ->
            val card = cardsState.value[index]
            Row(
                modifier = Modifier
                    .padding(0.dp, 10.dp)
                    .fillMaxSize()
                    .clickable {},
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 회원 정보 표시 (이미지, 닉네임)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    GlideImage(
                        model = card.memberProfileImageUrl,
                        contentDescription = "123",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(30.dp) // 이미지 크기
                            .clip(CircleShape) // 동그라미 모양으로 잘라주기
                    )
                    Text(
                        text = card.memberNickname,
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .width(150.dp)
                    )
                }
                val followText = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = if (card.amILikeThis) Color(0xFFFF4040) else Color(
                                0xFF9DC4FF
                            )
                        )
                    ) {
                        append(if (card.amILikeThis) "언팔로우" else "팔로우")
                    }
                }
                ClickableText(
                    text = followText,
                    style = TextStyle(fontSize = 18.sp, textAlign = TextAlign.End),
                    onClick = { offset ->
                        // 팔로우 또는 언팔로우 이벤트 처리
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 사진 표시
            GlideImage(
                model = card.imageUrl,
                contentDescription = "Card Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth,
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
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AccountUI(accountCardState: MutableState<List<Member>>, navController: NavController) {
    // 계정 탭의 검색 결과를 표시하는 UI 컴포넌트
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 100.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(accountCardState.value.size) { index ->
            val accountCard = accountCardState.value[index]
            // 계정 정보 및 UI 표시
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .clickable {},
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    model = accountCard.profileImageUrl,
                    contentDescription = "Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = accountCard.nickname,
                    style = TextStyle(fontSize = 20.sp),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .width(150.dp)
                )
                if (!accountCard.isFollow) {
                    val text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color(0xFF9DC4FF))) {
                            append("팔로우")
                        }
                    }
                    ClickableText(
                        text = text,
                        style = TextStyle(fontSize = 18.sp, textAlign = TextAlign.End),
                        onClick = { offset ->
                            // 팔로우 클릭 이벤트 처리
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    val text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color(0xFFFF4040))) {
                            append("언팔로우")
                        }
                    }
                    ClickableText(
                        text = text,
                        style = TextStyle(fontSize = 18.sp, textAlign = TextAlign.End),
                        onClick = { offset ->
                            // 언팔로우 클릭 이벤트 처리
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StarUI(starCardsState: MutableState<List<Star>>, navController: NavController) {
    // 별 탭의 검색 결과를 표시하는 UI 컴포넌트
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 100.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(starCardsState.value.size) { index ->
            val star = starCardsState.value[index]
            // Row를 클릭 가능하게 변경
            Log.d("search", "ID: ${star}")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("${Screen.StarDetail.route}/${star.id}")
                    }
                    .padding(0.dp, 10.dp)
            ) {
                // 별 정보 표시
                Text(
                    text = DBModule.nameMap[star.id] ?: "Invalid Star",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(0.dp, 8.dp)
                )
            }
        }
    }
}