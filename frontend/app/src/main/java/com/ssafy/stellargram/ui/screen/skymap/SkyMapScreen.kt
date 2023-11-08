package com.ssafy.stellargram.ui.screen.skymap

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ssafy.stellargram.module.DBModule
import com.ssafy.stellargram.module.ScreenModule
import kotlinx.coroutines.delay

@Composable
fun SkyMapScreen(navController : NavController, modifier: Modifier){
    val viewModel : SkyMapViewModel = viewModel()

    // TODO: 기기로부터 정보를 받는 거로 고쳐야 함.
    val longitude: Double = 127.039611
    val latitude: Double = 37.501254

    var offsetX: Double by remember { mutableStateOf(0.0) }
    var offsetY: Double by remember { mutableStateOf(0.0) }
    var theta: Double by remember { mutableStateOf(0.0)}
    var phi: Double by remember { mutableStateOf(0.0) }
    var isDragging: Boolean by remember { mutableStateOf(false) }
    var LST: Double by remember{mutableStateOf(viewModel.getMeanSiderealTime(longitude))}
    var i: Int by remember{mutableStateOf(0)}
    var screenWidth: Int by remember{mutableStateOf(0)}
    var screenHeight: Int by remember{mutableStateOf(0)}
    var starArray: Array<DoubleArray> by remember{ mutableStateOf(arrayOf()) }
    var nameMap: HashMap<Int, String> by remember{ mutableStateOf(hashMapOf()) }

    LaunchedEffect(Unit) {
        // star array가 늦게 계산되기도 하기 때문에 바로 홈화면에서 화면을 키게 될 경우 아무것도 불러오지 못함.
        while(starArray.size == 0 || screenHeight == 0 || screenWidth == 0){
            starArray = DBModule.gettingStarArray()
            nameMap = DBModule.gettingNameMap()
            screenHeight = ScreenModule.gettingHeight()
            screenWidth = ScreenModule.gettingWidth()
            delay(100L)
        }

        viewModel.createStarData(starArray, nameMap)


        while (true) {
            i++
            delay(1000L) // 1초마다 함수 호출
            LST = viewModel.getMeanSiderealTime(longitude)
            viewModel.getSight(longitude, latitude, LST, theta, phi, viewModel.starData.value)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = "array size: ${viewModel.starData.value.size}", color = Color.Black)
    }

}

