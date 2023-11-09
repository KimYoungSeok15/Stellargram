package com.ssafy.stellargram.ui.screen.skymap

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ssafy.stellargram.module.DBModule
import com.ssafy.stellargram.module.ScreenModule
import com.ssafy.stellargram.util.Temperature
import kotlinx.coroutines.delay
import java.lang.Math.pow

@Composable
fun SkyMapScreen(navController : NavController, modifier: Modifier){
    val viewModel : SkyMapViewModel = viewModel()
    val temperature: Temperature = Temperature()

    // TODO: 기기로부터 정보를 받는 거로 고쳐야 함.
    val longitude: Double = 127.039611
    val latitude: Double = 37.501254

    var offsetX: Double by remember { mutableStateOf(0.0) }
    var offsetY: Double by remember { mutableStateOf(0.0) }
    var theta: Double by remember { mutableStateOf(180.0)}
    var phi: Double by remember { mutableStateOf(0.0) }
    var isDragging: Boolean by remember { mutableStateOf(false) }
    var LST: Double by remember{mutableStateOf(viewModel.getMeanSiderealTime(longitude))}
    var i: Int by remember{mutableStateOf(0)}
    var screenWidth: Int by remember{mutableStateOf(0)}
    var screenHeight: Int by remember{mutableStateOf(0)}
    var starArray: Array<DoubleArray> by remember{ mutableStateOf(arrayOf()) }
    var nameMap: HashMap<Int, String> by remember{ mutableStateOf(hashMapOf()) }
    var starSight: List<DoubleArray> by remember{ mutableStateOf(List(10){DoubleArray(5){0.0}})}

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
        viewModel.setScreenSize(screenWidth, screenHeight)

        while (true) {
            i++
            LST = viewModel.getMeanSiderealTime(longitude)
            viewModel.getSight(longitude, latitude, LST, theta, phi, viewModel.starData.value)
            starSight = viewModel.getVisibleStars(4.5, 0.0, 0.0)
            delay(1000L) // 1초마다 함수 호출
        }
    }
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .background(Color.Black),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ){
//        val temp = temperature.getTemperature(starSight[0][2])
//        val starColor = temperature.colorMap[temp]
//        Box(
//            modifier = Modifier
//                .background(
//                    color = Color(starColor?:0)
//                )
//        ){
//            Column {
//                Text(text = "array[0]: ${starSight[0][0]} ${starSight[0][1]} ${starSight[0][2]} ${starSight[0][3]} ${starSight[0][4]}"
//                    , color = Color.Black)
//                Text(text = "name: ${nameMap[starSight[0][4].toInt()]}")
//
//            }
//        }
//
//        val temp2 = temperature.getTemperature(starSight[1][2])
//        val starColor2 = temperature.colorMap[temp2]
//        Box(
//            modifier = Modifier
//                .background(
//                    color = Color(starColor2?:0)
//                )
//        ){
//            Column {
//                Text(text = "array[1]: ${starSight[1][0]} ${starSight[1][1]} ${starSight[1][2]} ${starSight[1][3]} ${starSight[1][4]}"
//                    , color = Color.Black)
//                Text(text = "name: ${nameMap[starSight[1][4].toInt()]}")
//
//            }
//        }
//    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Black
            ),
        onDraw = {
            starSight.forEach {star ->
                val x = star[0].toFloat()
                val y = star[1].toFloat()
                val starColor = temperature.colorMap[temperature.getTemperature(star[2])]?:0
                val radius = pow(10.0, 0.25 * (5.5 - star[3])).toFloat()
                val center = Offset((size.width / 2) + x, (size.height / 2) - y)
                val name = nameMap[star[4].toInt()]?:""
                //TODO: gradient 작동 안 함.

//                drawCircle(center = center, radius = 3.0f * radius,
//                    brush = Brush.radialGradient(colors = listOf(Color(starColor), Color.Black), radius = 3.0f * radius))
                drawCircle(center = center, radius = radius, color = Color(starColor))
                Log.d("calc", name)
            }

            //TODO: 별자리 선 그려야 함.
        }
    )

}