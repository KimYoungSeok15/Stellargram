package com.ssafy.stellargram.ui.screen.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ssafy.stellargram.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {

    var active by rememberSaveable { mutableStateOf(false) }
    val mainViewModel: MainViewModel = viewModel()

    val tabIndex by mainViewModel.tabIndex.observeAsState()

    // 검색 결과를 캐싱
    var cardResults by remember { mutableStateOf(mainViewModel.cardResults) }
    var memberResults by remember { mutableStateOf(mainViewModel.memberResults) }
    var starResults by remember { mutableStateOf(mainViewModel.starResults) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            modifier = Modifier.semantics { traversalIndex = -1f },
            query = mainViewModel.text,
            onQueryChange = { newText ->
                mainViewModel.text = newText
            },
            onSearch = {
                // 검색 이벤트 처리
                coroutineScope.launch {
                    getSearchResults(mainViewModel.text, mainViewModel) // 검색 결과를 불러옵니다.
                }
                active = false
            },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = { Text("검색") },
            leadingIcon = {
                IconButton(
                    onClick = {
                        if (!active) {
                            navController.popBackStack()
                        }
                        // leadingIcon 클릭 시 active 값을 false로 변경하여 검색 바 비활성화
                        active = false
                    },
                    modifier = Modifier.padding(13.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.back),
                        contentDescription = "Back", // 적절한 contentDescription 설정
                        modifier = Modifier.size(36.dp)
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        // trailingIcon 클릭 시 검색 이벤트를 실행
                        coroutineScope.launch {
                            getSearchResults(mainViewModel.text, mainViewModel) // 검색 결과를 불러옵니다.
                        }
                        active = false
                    },
                    modifier = Modifier.padding(13.dp)
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                }
            },
        ) {

        }

        TabLayout(viewModel = mainViewModel)
        // 검색 결과에 따라 적절한 UI를 렌더링
        when (tabIndex) {
            0 -> {
                ArticleScreen(viewModel = mainViewModel, cardResults, navController)
            }
            1 -> {
                AccountScreen(viewModel = mainViewModel, memberResults, navController)
            }
            2 -> {
                StarScreen(viewModel = mainViewModel, starResults, navController)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

    }
}

