package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ssafy.stellargram.ui.Screen
import com.ssafy.stellargram.ui.rememberAppNavigationController

@Composable
fun ChatRoomCard(
    roomId: Int,
    personnel: Int,
    observeSite: String,
    navController: NavController
) {
//    var navController: NavController = rememberAppNavigationController()

    var roomCardModifier: Modifier =
        Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable { onClickCard(roomId, navController) }
//    Button(onClick = { navController.navigate(route = Screen.ChatRoom.route + "/${roomId}") }) {
    Column(modifier = roomCardModifier) {
        Row {
            Text(text = observeSite)
            Text(text = personnel.toString() + "명")
        }
    }
//    }


}

fun onClickCard(roomId: Int, navController: NavController) {
    navController.navigate(route = Screen.ChatRoom.route + "/"+roomId.toString())
}