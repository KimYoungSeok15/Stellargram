package com.ssafy.stellargram.ui.screen.mypage

import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ssafy.stellargram.model.Card
import com.ssafy.stellargram.model.Member
import com.ssafy.stellargram.model.Star
import com.ssafy.stellargram.ui.screen.search.AccountUI
import com.ssafy.stellargram.ui.screen.search.ArticleUI
import com.ssafy.stellargram.ui.screen.search.MainViewModel
import com.ssafy.stellargram.ui.screen.search.StarUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor() : ViewModel() {
    private val _memberResults = mutableStateOf<List<Member>>(emptyList())
    val tabs = listOf("게시물", "즐겨찾기", "좋아요")
    val memberResults: State<List<Member>> get() = _memberResults

    // Add these properties for managing tabs
    private val _tabIndex = mutableStateOf(0)

    // API 호출을 트리거하고 결과를 업데이트하는 함수
    fun getMemberInfo(text: String) {
        viewModelScope.launch {
            _memberResults.value = fetchMemberInfoInternal(text)
        }
    }

    // Add these methods for managing tabs
    fun updateTabIndex(index: Int) {
        _tabIndex.value = index
    }

    fun updateTabIndexBasedOnSwipe(delta: Float) {
        if (delta > 0 && _tabIndex.value!! > 0) {
            // Swipe to the right
            _tabIndex.value = _tabIndex.value!! - 1
        } else if (delta < 0 && _tabIndex.value!! < tabs.size - 1) {
            // Swipe to the left
            _tabIndex.value = _tabIndex.value!! + 1
        }
    }

    // 실제 API 호출이 이루어지는 함수
    private suspend fun fetchMemberInfoInternal(text: String): List<Member> {
        return withContext(Dispatchers.IO) {
            // 여기에 실제 API 호출을 하고 결과를 반환하세요.
            getMemberInfoFromApi(text)
        }
    }

    // 실제 API 호출을 대체하는 함수 (더미 데이터 형태로 구현)
    private fun getMemberInfoFromApi(text: String): List<Member> {
        // 실제 API 로직으로 대체
        return listOf(
            Member(
                memberId = 2,
                nickname = "Karina",
                profileImageUrl = "https://i.namu.wiki/i/hyYeK3WTj5JutQxaxAHHjFic9oAQ8kN4jdZo_MBGkzboWMtsr9pQN6JWeWgU9c8rmDon6XLlLhxuVrPbc6djcQ.gif",
                isFollow = true,
                followCount = 123,
                followingCount = 321,
                cardCount = 2
            )
        )
    }
}

@Composable
fun TabLayout(viewModel: MypageViewModel, dragState: MutableState<DraggableState>, content: @Composable (Int, MutableState<DraggableState>) -> Unit) {
    val tabIndex = remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tabIndex.value) {
            viewModel.tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex.value == index,
                    onClick = { tabIndex.value = index },
                    modifier = Modifier.draggable(
                        state = dragState.value,
                        orientation = Orientation.Horizontal,
                        onDragStarted = {},
                        onDragStopped = { /* Handle drag stop if needed */ }
                    )
                )
            }
        }

        content(tabIndex.value, dragState)
    }
}

// ArticleScreen, AccountScreen, StarScreen 함수 업데이트
@Composable
fun ArticleScreen(viewModel: MypageViewModel, dragState: MutableState<DraggableState>, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 370.dp)
            .draggable(
                state = dragState.value,
                orientation = Orientation.Horizontal,
                onDragStarted = {},
                onDragStopped = { viewModel.updateTabIndexBasedOnSwipe(it) }
            ),
    ) {
        Text(text = "1")
        Text(text = "1")
    }
}

@Composable
fun AccountScreen(viewModel: MypageViewModel, dragState: MutableState<DraggableState>, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 370.dp)
            .draggable(
                state = dragState.value,
                orientation = Orientation.Horizontal,
                onDragStarted = {},
                onDragStopped = { viewModel.updateTabIndexBasedOnSwipe(it) }
            ),
    ) {
        Text(text = "2")
        Text(text = "2")
    }
}


@Composable
fun StarScreen(viewModel: MypageViewModel, dragState: MutableState<DraggableState>, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 370.dp)
            .draggable(
                state = dragState.value,
                orientation = Orientation.Horizontal,
                onDragStarted = {},
                onDragStopped = { viewModel.updateTabIndexBasedOnSwipe(it) }
            ),
    ) {
        Text(text = "3")
        Text(text = "3")
    }
}
