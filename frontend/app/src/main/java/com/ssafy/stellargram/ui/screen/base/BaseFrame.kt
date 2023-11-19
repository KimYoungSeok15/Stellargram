package com.ssafy.stellargram.ui.screen.base

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ssafy.stellargram.R
import com.ssafy.stellargram.ui.Screen
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.StellargramApplication
import com.ssafy.stellargram.data.remote.NetworkModule

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun BaseFrame(
    navController: NavController = rememberNavController(),
    screen: Screen,
    content: @Composable BoxScope.() -> Unit = { example() },
)
{
    val viewModel = BaseViewModel()
    // 사용자의 id
    var memberID by remember { mutableLongStateOf(0) }
    val memberInfo = viewModel.memberInfo
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // 페이지 주인의 id
    val userId by remember(navBackStackEntry) {
        val arguments = navBackStackEntry?.arguments
        val defaultUserId = StellargramApplication.prefs.getString("memberId", "0").toLong()

        mutableStateOf(arguments?.getLong("id") ?: defaultUserId)
    }

    var isDialogOpen = viewModel.isDialogOpen

    LaunchedEffect(Unit){
        memberID = StellargramApplication.prefs.getString("memberId", "0").toLong()
    }
    Scaffold(
        topBar = {
                CenterAlignedTopAppBar(
                title = {
                    Text(text = screen.title, fontWeight = FontWeight.Bold)
                },
                modifier = Modifier,
                    navigationIcon = {
                        val navIconResource = when {
                            screen.route.startsWith("stardetail") -> R.drawable.back
                            screen.route == "skymap" -> R.drawable.menu
                            screen.route == "mypage" && userId == memberID -> R.drawable.writing
                            else -> null
                        }

                        // navigationIcon을 동적으로 설정
                        navIconResource?.let { iconRes ->
                            IconButton(
                                onClick = {
                                    // navigationIcon 클릭 이벤트 처리
                                    when {
                                        screen.route.startsWith("stardetail") -> {
                                            navController.popBackStack()
                                        }
                                        screen.route == "skymap" -> {
                                            // "skymap" 화면인 경우 다른 동작 수행
                                        }
                                        screen.route == "mypage" && userId == memberID -> {
                                            viewModel.openProfileModificationDialog()
                                        }
                                    }
                                },
                                modifier = Modifier.padding(13.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = "Back", // 적절한 contentDescription 설정
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    },

                actions = {
                    // IconButton 내에서 사용할 아이콘을 동적으로 선택합니다.
                    val iconResource = when (screen) {
                        is Screen.Home -> R.drawable.search
                        is Screen.SkyMap -> R.drawable.search
//                        is Screen.GoogleMap -> R.drawable.add
                        is Screen.MyPage -> R.drawable.chat1
                        else -> null
                    }

                    IconButton(
                        onClick = {
                            // 클릭 이벤트 처리
                            when (screen) {
                                is Screen.Home -> { navController.navigate("search") }
                                is Screen.SkyMap -> { navController.navigate("search") }
                                is Screen.GoogleMap -> {}
                                is Screen.MyPage -> { navController.navigate("chatroomlist") }
                                else -> null
                            }
                        },
                        modifier = Modifier.padding(13.dp)
                    ) {
                        // iconResource가 null이 아닌 경우에만 아이콘을 렌더링합니다.
                        iconResource?.let { iconRes ->
                            Image(
                                painter = painterResource(id = iconRes),
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                content = {
                    val items = listOf(
                        Screen.Home,
                        Screen.SkyMap,
                        Screen.Photo,
//                        Screen.Camera,
                        Screen.GoogleMap,
                        Screen.MyPage
                    )
                    items.forEach {
                        NavigationBarItem(
                            selected = navController.currentDestination?.route == it.route,
                            onClick = {
                                if (it == Screen.MyPage) {
                                    navController.navigate("${Screen.MyPage.route}/$memberID")
                                } else {
                                    navController.navigate(it.route)
                                }
                            },
                            icon = { Icon(painter = painterResource(id = it.icon), contentDescription = it.title) },
                            label = { it.title },
                            modifier = Modifier
                                .padding(20.dp)
                                .width(20.dp)
                                .height(36.dp)
                        )
                    }
                },
                containerColor = Color.Transparent
            )
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            content()
        }
    }
}



@Composable
fun example(){
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text =
            """
                    This is an example of a scaffold. It uses the Scaffold composable's parameters to create a screen with a simple top app bar, bottom app bar, and floating action button.

                    It also contains some basic inner content, such as this text.

                    You have pressed the floating action button 0 times.
                """.trimIndent(),
        )
    }
}
