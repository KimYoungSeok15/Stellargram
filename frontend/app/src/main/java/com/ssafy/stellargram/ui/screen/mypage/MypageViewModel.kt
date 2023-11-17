package com.ssafy.stellargram.ui.screen.mypage

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.R
import com.ssafy.stellargram.StellargramApplication
import com.ssafy.stellargram.data.remote.ApiServiceForCards
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.Card
import com.ssafy.stellargram.model.Member
import com.ssafy.stellargram.model.MemberMeResponse
import com.ssafy.stellargram.model.MemberResponse
import com.ssafy.stellargram.model.Star
import com.ssafy.stellargram.util.StarCardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.internal.aggregatedroot.codegen._com_ssafy_stellargram_StellargramApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor() : ViewModel() {
    private val _memberResults = mutableStateOf<List<Member>>(emptyList())
    private val _tabIndex: MutableLiveData<Int> = MutableLiveData(0)
    val tabs = listOf("게시물", "즐겨찾기", "좋아요")
    val tabIndex: LiveData<Int> = _tabIndex
    val memberResults: State<List<Member>> get() = _memberResults
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


    // Add these methods for managing tabs
    fun updateTabIndex(index: Int) {
        _tabIndex.value = index
    }

    // API 호출을 트리거하고 결과를 업데이트하는 함수
    fun getMemberInfo(id: Long) {
        viewModelScope.launch {
            _memberResults.value = try {
                withContext(Dispatchers.IO) {
                    val response = NetworkModule.provideRetrofitInstance().getMember(userId = id)
                    Log.d("마이페이지", response.toString())

                    if (response.isSuccessful) {
                        val memberResponse = response.body()

                        memberResponse?.data?.let { data ->
                            val member = Member(
                                memberId = data.memberId,
                                nickname = data.nickname,
                                profileImageUrl = data.profileImageUrl,
                                followCount = data.followCount,
                                followingCount = data.followingCount,
                                cardCount = data.cardCount,
                                isFollow = data.isFollow // TODO: ifFollow도 응답을 받으면 UI로 팔로우, 팔로잉을 넣어주자
                            )
                            return@withContext listOf(member)
                        } ?: emptyList()
                    } else {
                        Log.e("마이페이지", "API 호출 실패: ${response.code()} - ${response.message()}")
                        emptyList()
                    }
                }
            } catch (e: Exception) {
                Log.e("마이페이지", "API 호출 중 예외 발생: ${e.message}")
                emptyList()
            }
        }
    }
    // 사용자가 팔로우 하는 사람들의 목록을 가져온다. 보고있는 페이지의 주인이 아니다.
    suspend fun getFollowingList(id: Long): List<Long> {
        return try {
            val response = NetworkModule.provideRetrofitInstance().getFollowingList(memberId = id)

            if (response.isSuccessful) {
                val followersResponse = response.body()

                followersResponse?.data?.let { data ->
                    // memberId만 추출하여 리스트에 저장
                    return data.members.map { follower ->
                        follower.memberId
                    }
                } ?: emptyList()
            } else {
                Log.e("마이페이지", "팔로잉 목록 조회 실패: ${response.code()} - ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("마이페이지", "팔로잉 목록 조회 중 예외 발생: ${e.message}")
            emptyList()
        }

    }


    // 팔로우 또는 언팔로우 클릭시 실행하는 부분
    fun handleFollowButtonClick(memberId: Long, isFollowing: Boolean) {
        viewModelScope.launch {
            try {
                if (isFollowing) {
                    // 언팔로우 API 호출
                    val response = NetworkModule.provideRetrofitInstance().unfollowUser(followingId = memberId)
                    if (response.isSuccessful) {
                        // 성공적으로 언팔로우했을 때의 로직
                        updateLikeCardState(memberId, isFollowing)
                    } else {
                        // 언팔로우 실패 시의 로직
                    }
                } else {
                    // 팔로우 API 호출
                    val response = NetworkModule.provideRetrofitInstance().followUser(followingId = memberId)
                    if (response.isSuccessful) {
                        // 성공적으로 팔로우했을 때의 로직
                        updateLikeCardState(memberId, isFollowing)
                    } else {
                        // 팔로우 실패 시의 로직
                    }
                }
            } catch (e: Exception) {
                // 예외 발생 시의 로직
            }
        }
    }

    // 팔로우 <-> 언팔로우 버튼 switch
    fun updateLikeCardState(memberId: Long, isFollowing: Boolean) {
        // likeCards에서 memberId를 찾아서 상태 업데이트
        val updatedLikeCards = likeCards.value.map { card ->
            if (card.memberId == memberId) {
                card.copy(isFollowing = !isFollowing)
            } else {
                card
            }
        }
        likeCards.value = updatedLikeCards
    }

    fun handleLikeButtonClick(cardId: Int, isLiked: Boolean) {
        viewModelScope.launch {
            try {
                // 좋아요 취소 API 호출
                val response = NetworkModule.provideRetrofitCards().likeCard(cardId = cardId)
                if (response.isSuccessful) {
                    // 성공적으로 좋아요 취소했을 때의 로직
                    updateCardLikeState(cardId, isLiked)
                } else {
                    // 좋아요 취소 실패 시의 로직
                }
            } catch (e: Exception) {
                // 예외 발생 시의 로직
            }
        }
    }

    private fun updateCardLikeState(cardId: Int, isLiked: Boolean) {
        // API 호출 성공 시 카드 상태 업데이트 로직
        val updatedMyCards = myCards.value.map { card ->
            if (card.cardId == cardId) {
                card.copy(amILikeThis = !isLiked, likeCount = if (!isLiked) card.likeCount + 1 else card.likeCount - 1)
            } else {
                card
            }
        }

        val updatedLikeCards = likeCards.value.map { card ->
            if (card.cardId == cardId) {
                card.copy(amILikeThis = !isLiked, likeCount = if (!isLiked) card.likeCount + 1 else card.likeCount - 1)
            } else {
                card
            }
        }

        myCards.value = updatedMyCards
        likeCards.value = updatedLikeCards
    }

    // 실제 데이터들이 들어갈 변수
    var myCards = mutableStateOf<List<Card>>(emptyList())
    var favStars = mutableStateOf<List<Star>>(emptyList())
    var likeCards = mutableStateOf<List<Card>>(emptyList())

    // 내 게시물 가져오기
    suspend fun fetchUserCards(id: Long): List<Card> {
        Log.d("마이페이지", "시작")
        return try {
            val response = NetworkModule.provideRetrofitCards().getCards(memberId = id)
            Log.d("마이페이지", "디버깅 : $response")
            if (response.isSuccessful) {
                val cardsResponse = response.body()
                Log.d("마이페이지", "내 카드들 : ${response.body()?.data?.starcards}")
                cardsResponse?.data?.let { data ->
                    // 필요한 데이터를 추출하고 Card 객체를 생성합니다.
                    return data.starcards.map { starCardData ->
                        Card(
                            cardId = starCardData.cardId,
                            memberId = starCardData.memberId,
                            memberNickname = starCardData.memberNickname,
                            memberProfileImageUrl = starCardData.memberProfileImageUrl,
                            observeSiteId = starCardData.observeSiteId,
                            imagePath = starCardData.imagePath,
                            imageUrl = starCardData.imageUrl,
                            content = starCardData.content,
                            photoAt = starCardData.photoAt,
                            category = starCardData.category,
                            tools = starCardData.tools,
                            likeCount = starCardData.likeCount,
                            amILikeThis = starCardData.amILikeThis,
                            isFollowing = false, // 쓰이지 않음
                        )
                    }
                } ?: emptyList()
            } else {
                Log.d("마이페이지", "API 호출 실패: ${response.code()} - ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("마이페이지", "API 호출 실패 $e")
            emptyList()
        }
    }

    fun getFavStars(): List<Star> {
        // shared preferences에서 id 값들을 받아 id에 맞는 별들을 리스트에 저장(예정)
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

    suspend fun fetchLikeCards(id: Long): List<Card> {
        return try {
            val response = NetworkModule.provideRetrofitCards().getLikeCards(memberId = id)

            if (response.isSuccessful) {
                val cardsResponse = response.body()
                Log.d("마이페이지", "내가 좋아하는 카드들 : ${response.body()?.data?.starcards}")
                cardsResponse?.data?.let { data ->
                    // 필요한 데이터를 추출하고 Card 객체를 생성합니다.
                    return data.starcards.map { starCardData ->
                        Card(
                            cardId = starCardData.cardId,
                            memberId = starCardData.memberId,
                            memberNickname = starCardData.memberNickname,
                            memberProfileImageUrl = starCardData.memberProfileImageUrl,
                            observeSiteId = starCardData.observeSiteId,
                            imagePath = starCardData.imagePath,
                            imageUrl = starCardData.imageUrl,
                            content = starCardData.content,
                            photoAt = starCardData.photoAt,
                            category = starCardData.category,
                            tools = starCardData.tools,
                            likeCount = starCardData.likeCount,
                            amILikeThis = starCardData.amILikeThis,
                            isFollowing = true, // 초기값은 false
                        )
                    }
                } ?: emptyList()
            } else {
                // API 에러 응답을 처리합니다.
                // 에러를 로그에 남기거나 사용자에게 메시지를 표시합니다.
                emptyList()
            }
        } catch (e: Exception) {
            // 예외를 처리합니다 (예: 네트워크 오류).
            // 예외를 로그에 남기거나 사용자에게 메시지를 표시합니다.
            emptyList()
        }
    }
}

@Composable
fun TabLayout(viewModel: MypageViewModel) {
    val tabIndex = viewModel.tabIndex.observeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tabIndex.value!!) {
            viewModel.tabs.forEachIndexed { index, title ->
                Tab(
                    //  text = { Text(title) }, 텍스트를 띄우고 싶으면 주석 off
                    selected = tabIndex.value!! == index,
                    onClick = { viewModel.updateTabIndex(index) },
                    icon = {
                        when (index) {
                            0 -> Image(
                                    painterResource(id = R.drawable.camera),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            1 -> Image(
                                    painterResource(id = R.drawable.star),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            2 -> Image(
                                    painterResource(id = R.drawable.heart),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                        }
                    }
                )
            }
        }
    }
}

// ArticleScreen, AccountScreen, StarScreen 함수 업데이트
@Composable
fun MyCardsScreen(viewModel: MypageViewModel, myCards: MutableState<List<Card>>, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 370.dp)
            .draggable(
                state = viewModel.dragState.value!!,
                orientation = Orientation.Horizontal,
                onDragStarted = { },
                onDragStopped = {
                    viewModel.updateTabIndexBasedOnSwipe(it)
                }),
    ) {
        MyCardsUI(cardsState = myCards, navController = navController,viewModel = viewModel)
    }
}

@Composable
fun FavStarsScreen(viewModel: MypageViewModel, favStars: MutableState<List<Star>>, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 370.dp)
            .draggable(
                state = viewModel.dragState.value!!,
                orientation = Orientation.Horizontal,
                onDragStarted = { },
                onDragStopped = {
                    viewModel.updateTabIndexBasedOnSwipe(it)
                }),
    ) {
        FavStarsUI(starsState = favStars, navController =  navController, viewModel = viewModel)
    }
}


@Composable
fun LikeCardsScreen(viewModel: MypageViewModel, likeCards: MutableState<List<Card>>, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 370.dp)
            .draggable(
                state = viewModel.dragState.value!!,
                orientation = Orientation.Horizontal,
                onDragStarted = { },
                onDragStopped = {
                    viewModel.updateTabIndexBasedOnSwipe(it)
                }),
    ) {
        LikeCardsUI(cardsState = likeCards, navController = navController, viewModel=viewModel)
    }
}

suspend fun getResults(viewModel: MypageViewModel, id:Long, followingList:List<Long>): List<Any> = coroutineScope {
    val myCardsDeferred = async { viewModel.fetchUserCards(id=id) }
    val favStarsDeferred = async { viewModel.getFavStars() }
    val likeCardsDeferred = async { viewModel.fetchLikeCards(id=id) }

    val myCards = myCardsDeferred.await()
    val favStars = favStarsDeferred.await()
    val likeCards = likeCardsDeferred.await()

    viewModel.myCards.value = myCards
    viewModel.favStars.value = favStars
    viewModel.likeCards.value = likeCards

    // 좋아하는 카드마다 그 카드의 작가를 팔로잉 중인지 여부 판단
    likeCards.forEach { card ->
        card.isFollowing = followingList.any { it == card.memberId }
    }

    val results = mutableListOf<Any>()
    results.addAll(myCards)
    results.addAll(favStars)
    results.addAll(likeCards)

    Log.d("마이페이지","$results")
    results
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MyCardsUI(cardsState: MutableState<List<Card>>, navController: NavController, viewModel:MypageViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 50.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        cardsState.value.forEach { card ->
            Row(
                modifier = Modifier
                    .padding(12.dp, 10.dp, 12.dp, 0.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 회원 정보 표시 (이미지, 닉네임)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable
                    {
                        // 해당 유저 마이페이지로 이동. 단, 현재 페이지일 경우 무효
                    },
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
            }

            // 사진 표시
            GlideImage(
                model = card.imageUrl,
                contentDescription = "Card Image",
                contentScale = ContentScale.FillWidth,
            )

            // 좋아요 아이콘 및 텍스트
            val likeIcon = if (card.amILikeThis) {
                painterResource(id = R.drawable.filledheart)
            } else {
                painterResource(id = R.drawable.emptyheart)
            }
            Row(
                modifier = Modifier.padding(12.dp, 4.dp)
            ) {
                Image(
                    painter = likeIcon,
                    contentDescription = null, // 이미지 설명
                    modifier = Modifier.size(24.dp).focusable(false)
                        .clickable (
                            interactionSource = remember{ MutableInteractionSource() },
                            indication = null){
                            viewModel.handleLikeButtonClick(cardId = card.cardId, isLiked = card.amILikeThis)
                        }
                )
                Text(
                    text = "좋아요 ${card.likeCount}",
                    style = TextStyle(fontSize = 20.sp),
                    modifier = Modifier.padding(start = 8.dp)
                        .clickable (
                            interactionSource = remember{ MutableInteractionSource() },
                            indication = null){
//                            viewModel.handleLikersButtonClick(cardId = card.cardId)
                        }
                )
            }

            // 카드 내용 표시
            Text(
                text = card.content,
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 20.dp)
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FavStarsUI(starsState: MutableState<List<Star>>, navController: NavController, viewModel:MypageViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        starsState.value.forEach { star ->
            // 각 Star에 대한 정보 표시
            Text(
                text = "${star.name}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top=12.dp)
                )
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                GlideImage(
                    model = "https://image.librewiki.net/c/c5/Vega.jpg",
                    contentDescription = "설명",
                    modifier = Modifier.padding(vertical = 12.dp),
                    contentScale = ContentScale.FillWidth
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(0.dp, 4.dp)
                        .clickable (
                            interactionSource = remember{ MutableInteractionSource() },
                            indication = null){
                            // TODO: 실행할 함수 여기서 호출.
                        },
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(R.drawable.like),
                        contentDescription = null, // 이미지 설명
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "즐겨찾기 취소",
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 첫 번째 Column (별자리, 적경, 적위 등)
                Column {
                    Text(text = "별자리", fontSize = 20.sp)
                    Text(text = "적경", fontSize = 20.sp)
                    Text(text = "적위", fontSize = 20.sp)
                    Text(text = "겉보기등급", fontSize = 20.sp)
                    Text(text = "절대등급", fontSize = 20.sp)
                    Text(text = "거리", fontSize = 20.sp)
                    Text(text = "항성 분류", fontSize = 20.sp)
                }

                // 두 번째 Column (details.constellation, details.rightAscension 등)
                Column {
                    Text(text = star.constellation, fontSize = 20.sp)
                    Text(text = star.rightAscension, fontSize = 20.sp)
                    Text(text = star.declination, fontSize = 20.sp)
                    Text(text = star.apparentMagnitude, fontSize = 20.sp)
                    Text(text = star.absoluteMagnitude, fontSize = 20.sp)
                    Text(text = star.distanceLightYear, fontSize = 20.sp)
                    Text(text = star.spectralClass, fontSize = 20.sp)
                }
            }

            // 설명
            Text(
                text = "베가(Vega)는 거문고자리 알파별(α Lyrae, α Lyr)로, 알타이르, 데네브와 함께 여름의 대삼각형을 이루는 0등급 별이다. 직녀성이라고도 잘 알려져 있다.",
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LikeCardsUI(cardsState: MutableState<List<Card>>, navController:NavController, viewModel: MypageViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 50.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        cardsState.value.forEach { card ->
            Row(
                modifier = Modifier
                    .padding(12.dp, 10.dp, 12.dp, 0.dp)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 회원 정보 표시 (이미지, 닉네임)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable
                    {
                        // 해당 유저 마이페이지로 이동. 단, 현재 페이지일 경우 무효
                    },
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
                // 팔로우 관련 - 내 카드면 팔로우버튼 없고, 남의 카드면 팔로잉 중일 경우 언팔로우가 뜨도록
                val isMyCard = card.memberId == StellargramApplication.prefs.getString("memberId","").toLong()
                val isFollowing = card.isFollowing

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
                            viewModel.handleFollowButtonClick(card.memberId, isFollowing)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Spacer(modifier = Modifier.height(0.dp))
                }
            }

            // 사진 표시
            GlideImage(
                model = card.imageUrl,
                contentDescription = "Card Image",
                contentScale = ContentScale.FillWidth,
            )

            // 좋아요 아이콘 및 텍스트
            val likeIcon = if (card.amILikeThis) {
                painterResource(id = R.drawable.filledheart)
            } else {
                painterResource(id = R.drawable.emptyheart)
            }
            Row(
                modifier = Modifier.padding(12.dp, 4.dp)
            ) {
                Image(
                    painter = likeIcon,
                    contentDescription = null, // 이미지 설명
                    modifier = Modifier.size(24.dp)
                        .clickable (
                            interactionSource = remember{ MutableInteractionSource() },
                            indication = null){
                            viewModel.handleLikeButtonClick(cardId = card.cardId, isLiked = card.amILikeThis)
                        }
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
                modifier = Modifier.padding(12.dp, 0.dp, 12.dp, 20.dp)
            )
        }
    }
}

