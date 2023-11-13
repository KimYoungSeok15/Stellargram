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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.R
import com.ssafy.stellargram.model.Card
import com.ssafy.stellargram.model.Member
import com.ssafy.stellargram.model.Star
import com.ssafy.stellargram.ui.Screen
import com.ssafy.stellargram.ui.rememberAppNavigationController
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
                Tab(text = { Text(title) },
                    selected = tabIndex.value!! == index,
                    onClick = { viewModel.updateTabIndex(index) },
//                    icon = {
//                        when (index) {
//                            0 -> Icon(imageVector = Icons.Default.Home, contentDescription = null)
//                            1 -> Icon(imageVector = Icons.Default.Info, contentDescription = null)
//                            2 -> Icon(imageVector = Icons.Default.Settings, contentDescription = null)
//                        }
//                    }
                )
            }
        }

//        when (tabIndex.value) {
//            0 -> ArticleScreen(viewModel = viewModel)
//            1 -> AccountScreen(viewModel = viewModel)
//            2 -> StarScreen(viewModel = viewModel)
//        }
    }
}
// 게시물 탭
@Composable
fun ArticleScreen(viewModel: MainViewModel, cardResultsState: MutableState<List<Card>>, navController: NavController) {

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
fun AccountScreen(viewModel: MainViewModel, accountCardsState: MutableState<List<Member>>, navController:NavController) {

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
        AccountUI(accountCardState = accountCardsState, navController) // accountCardsState.value 대신 accountCardsState 전달
    }
}

// 별 탭
@Composable
fun StarScreen(viewModel: MainViewModel, starCardsState: MutableState<List<Star>>, navController:NavController) {
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

    private val _tabIndex: MutableLiveData<Int> = MutableLiveData(0)
    var text by mutableStateOf("")
    val tabIndex: LiveData<Int> = _tabIndex
    val tabs = listOf("게시물", "유저", "별")

    var isSwipeToTheLeft: Boolean = false
    private val draggableState = DraggableState { delta ->
        isSwipeToTheLeft= delta > 0
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
    fun getCardResults(text: String): List<Card> {
        // 카드 검색 로직
        // 현재는 더미데이터
        val results : List<Card>
        results = listOf<Card>(
            Card(
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
            ),
            Card(
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
                likeCount = 2,
                amILikeThis = true
            )
        )
        return results
    }

    fun getMemberResults(text: String): List<Member> {
        // 멤버 검색 로직
        // 현재는 더미데이터
        val results : List<Member>
        results = listOf<Member>(
            Member(
                memberId = 2,
                nickname = "닉네임2",
                profileImageUrl = "https://i.namu.wiki/i/hyYeK3WTj5JutQxaxAHHjFic9oAQ8kN4jdZo_MBGkzboWMtsr9pQN6JWeWgU9c8rmDon6XLlLhxuVrPbc6djcQ.gif",
                isFollow = true,
                followCount = 0,
                followingCount = 0,
                cardCount = 0
            ),
            Member(
                memberId = 1,
                nickname = "닉네임",
                profileImageUrl = "https://i.namu.wiki/i/hyYeK3WTj5JutQxaxAHHjFic9oAQ8kN4jdZo_MBGkzboWMtsr9pQN6JWeWgU9c8rmDon6XLlLhxuVrPbc6djcQ.gif",
                isFollow = false,
                followCount = 0,
                followingCount = 0,
                cardCount = 0
            )
        )
        return results
    }

    fun getStarResults(text: String): List<Star> {
        // 별 검색 로직
        // 현재는 더미데이터
        val results : List<Star>
        results = listOf<Star>(
            Star(
            name = "Vega",
            constellation = "Lyra",
            rightAscension = "18h 36m 56.19s",
            declination = "+38° 46′ 58.8″",
            apparentMagnitude = "0.03",
            absoluteMagnitude = "0.58",
            distanceLightYear = "25",
            spectralClass = "A0Vvar"
            ),
            Star(
                name = "Vega",
                constellation = "Lyra",
                rightAscension = "18h 36m 56.19s",
                declination = "+38° 46′ 58.8″",
                apparentMagnitude = "0.03",
                absoluteMagnitude = "0.58",
                distanceLightYear = "25",
                spectralClass = "A0Vvar"
            )
        )
        return results
    }
//    init {
//        // 초기화 시 API 요청을 통해 검색 결과를 가져와서 캐싱
//        cardResults.value = getCardResults(text)
//        memberResults.value = getMemberResults(text)
//        starResults.value = getStarResults(text)
//    }
}
//----------------------------------------이상 탭관련----------------------------------------

//---------------------------------------검색결과-------------------------------------------
suspend fun getSearchResults(text: String, mainViewModel: MainViewModel): List<Any> = coroutineScope {
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
fun ArticleUI(cardsState: MutableState<List<Card>>, navController:NavController) {
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
                        model = card.memberImagePath,
                        contentDescription = "123",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(30.dp) // 이미지 크기
                            .clip(CircleShape) // 동그라미 모양으로 잘라주기
                    )
                    Text(
                        text = card.memberNickName,
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .width(150.dp)
                    )
                }
                val followText = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = if (card.amILikeThis) Color(0xFFFF4040) else Color(0xFF9DC4FF))) {
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
                model = card.imagePath,
                contentDescription = "Card Image",
                modifier = Modifier.fillMaxSize()
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
fun AccountUI(accountCardState: MutableState<List<Member>>, navController:NavController) {
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
            val starCard = starCardsState.value[index]
            // Row를 클릭 가능하게 변경
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("${Screen.StarDetail.route}/${starCard.name}")
                    }
                    .padding(0.dp, 10.dp)
            ) {
                // 별자리 정보 표시
                Text(
                    text = starCard.name,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(0.dp, 8.dp)
                )
            }
        }
    }
}