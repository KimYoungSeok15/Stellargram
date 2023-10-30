package com.ssafy.stellargram.ui.screen.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ssafy.stellargram.ui.Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseFrame(
    navController: NavController,
    content: @Composable BoxScope.() -> Unit = { example() })
{
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title= { Text(text = "title") },
                modifier = Modifier
            )
        },
        bottomBar = { NavigationBar(
            content = {
                val items = listOf(
                    Screen.Home,
                    Screen.GoogleMap,
                    Screen.MyPage,
                    Screen.CameraX,
                    Screen.SkyMap
                )
                items.forEach{
                    NavigationBarItem(
                        selected =  navController.currentDestination?.route == it.route ,
                        onClick = { navController.navigate(it.route) },
                        icon = { Icon(painter = painterResource(id = it.icon), contentDescription = it.title ) },
                        label = { it.title }
                    )
                }
            }
        ) }
    ) {
        Box(modifier = Modifier.padding(it)){
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
