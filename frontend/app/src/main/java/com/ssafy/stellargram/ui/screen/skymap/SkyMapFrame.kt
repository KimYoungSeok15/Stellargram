package com.ssafy.stellargram.ui.screen.skymap

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssafy.stellargram.ui.Screen
import com.ssafy.stellargram.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkyMapFrame(
    navController: NavController = rememberNavController(),
    screen: Screen,
    content: @Composable BoxScope.() -> Unit
)
{
    val viewModel : SkyMapViewModel = viewModel()
    var expandedSetting by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
                CenterAlignedTopAppBar(
                title = {
                    Text(text = screen.title, fontWeight = FontWeight.Bold)
                },
                modifier = Modifier,
                navigationIcon = {
                    IconButton(
                        onClick = { expandedSetting = true },
                        modifier = Modifier.padding(13.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.menu),
                            contentDescription = "Back",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    if (expandedSetting) {
                        DropdownMenu(
                            expanded = expandedSetting,
                            onDismissRequest = { expandedSetting = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                        ) {
                            // Add menu items here

                            DropdownMenuItem(
                                onClick = { viewModel.autoMode = !viewModel.autoMode },
                                text= { Text("AutoMode",
                                    color=
                                    if (viewModel.autoMode)
                                        Color(0xFF2196F3)
                                    else Color(0xFF673AB7)
                                )},
                            )
//                            DropdownMenuItem(
//                                onClick = { /* Handle item click */ },
//                                text= {
//                                    Text(text = "Item 2")
//                                }
//                            )

                            // Add more items as needed
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate("search") },
                        modifier = Modifier.padding(13.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "Search",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        },
        bottomBar = { NavigationBar(
            content = {
                val items = listOf(
                    Screen.Home,
                    Screen.SkyMap,
//                    Screen.Camera,
                    Screen.Photo,
                    Screen.GoogleMap,
                    Screen.MyPage
                )
                items.forEach{
                    NavigationBarItem(
                        selected =  navController.currentDestination?.route == it.route ,
                        onClick = { navController.navigate(it.route) },
                        icon = { Icon(painter = painterResource(id = it.icon), contentDescription = it.title)},
                        label = { it.title },
                        modifier = Modifier
                            .padding(20.dp)
                            .width(20.dp)
                            .height(36.dp)
                    )
                }
            },
            containerColor = Color.Transparent
        ) }
    ) {
        Box(modifier = Modifier.padding(it)){
            SkyMapScreen(navController,viewModel)
        }

    }
}