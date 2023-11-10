package com.ssafy.stellargram.ui.screen.mypage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.stellargram.model.Member
import com.ssafy.stellargram.ui.screen.search.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor() : ViewModel() {
    private val _memberResults = mutableStateOf<List<Member>>(emptyList())
    val memberResults: State<List<Member>> get() = _memberResults

    // API 호출을 트리거하고 결과를 업데이트하는 함수
    fun getMemberInfo(text: String) {
        viewModelScope.launch {
            _memberResults.value = fetchMemberInfoInternal(text)
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
fun TabLayout(tabTitles: List<String>, activeTab: Int, onTabSelected: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = activeTab) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = activeTab == index,
                    onClick = {
                        onTabSelected(index)
                    }
                )
            }
        }
    }
}