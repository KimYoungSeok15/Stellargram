package com.ssafy.stellargram.ui.screen.skymap

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ssafy.stellargram.module.DBModule
import com.ssafy.stellargram.module.ScreenModule
import com.ssafy.stellargram.util.ConstellationLine
import com.ssafy.stellargram.util.Temperature
import kotlinx.coroutines.delay
import java.lang.Math.PI
import java.lang.Math.cos
import java.lang.Math.min
import java.lang.Math.pow
import java.lang.Math.sin

@Composable
fun SkyMapScreen(navController : NavController, modifier: Modifier){
    val viewModel : SkyMapViewModel = viewModel()
    val temperature: Temperature = Temperature()
    val constellationLine: ConstellationLine = ConstellationLine()

    // TODO: 기기로부터 정보를 받는 거로 고쳐야 함.
    val longitude: Double = 127.039611
    val latitude: Double = 37.501254

    var offsetX: Double by remember { mutableStateOf(0.0) }
    var offsetY: Double by remember { mutableStateOf(0.0) }
    var theta: Double by remember { mutableStateOf(180.0)}
    var phi: Double by remember { mutableStateOf(0.0) }
    var isDragging: Boolean by remember { mutableStateOf(false) }
    var LST: Double by remember{mutableStateOf(viewModel.getMeanSiderealTime(longitude))}
    var i: Long by remember{mutableStateOf(0L)}
    var screenWidth: Int by remember{mutableStateOf(0)}
    var screenHeight: Int by remember{mutableStateOf(0)}
    var starArray: Array<DoubleArray> by remember{ mutableStateOf(arrayOf()) }
    var constArray: Array<DoubleArray> by remember{ mutableStateOf(arrayOf())}
    var nameMap: HashMap<Int, String> by remember{ mutableStateOf(hashMapOf()) }
    var starSight: List<DoubleArray> by remember{ mutableStateOf(listOf())}
    var starInfo: HashMap<Int, Int> by remember{ mutableStateOf(hashMapOf())}
    var constSight: Array<DoubleArray> by remember{ mutableStateOf(arrayOf())}
    var horizon: Array<DoubleArray> by remember{ mutableStateOf(arrayOf())}
    var horizonSight: Array<DoubleArray> by remember{ mutableStateOf(arrayOf())}

    LaunchedEffect(Unit) {
        // star array가 늦게 계산되기도 하기 때문에 바로 홈화면에서 화면을 키게 될 경우 아무것도 불러오지 못함.
        while(starArray.isEmpty() || constArray.isEmpty() || starInfo.isEmpty() || screenHeight == 0 || screenWidth == 0){
            starArray = DBModule.gettingStarArray()
            constArray = DBModule.gettingConstellation()
            nameMap = DBModule.gettingNameMap()
            screenHeight = ScreenModule.gettingHeight()
            screenWidth = ScreenModule.gettingWidth()
            starInfo = DBModule.gettingStarInfo()
            horizon = Array(3600){DoubleArray(6){0.0} }
            for(i in 0 until 3600){
                horizon[i][0] = cos(i.toDouble() * PI / 1800.0)
                horizon[i][1] = sin(i.toDouble() * PI / 1800.0)
            }
            delay(50L)
        }
        viewModel.createStarData(starArray, nameMap)
        viewModel.setScreenSize(screenWidth, screenHeight)
        viewModel.settingConstellation(constArray)

        while (true) {
            i = System.currentTimeMillis()
            LST = viewModel.getMeanSiderealTime(longitude)
            viewModel.getSight(theta, phi, viewModel.getAllStars(longitude, latitude, LST, viewModel.starData.value), 0)
            viewModel.getSight(theta, phi, viewModel.getAllStars(latitude, latitude, LST, viewModel.constellation.value), 1)
            viewModel.getSight(theta, phi, horizon, 2)
            starArray = viewModel.starSight.value
            starSight = viewModel.getVisibleStars(4.5, 0.0, 0.0)
            constSight = viewModel.constellationSight.value
            horizonSight = viewModel.horizonSight.value
            delay(1000L) // 1초마다 함수 호출
            Log.d("create", "${System.currentTimeMillis() - i}")
        }
    }
    Box(){
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Black
                ),
            onDraw = {
                for(i in 0 until min(constellationLine.start.size, viewModel.starSight.value.size)){
                    val ind1 = starInfo[constellationLine.start[i]]
                    val ind2 = starInfo[constellationLine.end[i]]
                    val star1 = viewModel.starSight.value[ind1?:0]
                    val star2 = viewModel.starSight.value[ind2?:0]
                    Log.d("star1", "${star1[0]}, ${star1[1]}, ${star1[2]}, ${star1[3]}, ${star1[4]}")
                    val x1 = star1[0].toFloat()
                    val y1 = star1[1].toFloat()
                    val x2 = star2[0].toFloat()
                    val y2 = star2[1].toFloat()
                    if((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) > 160000.0) continue
                    val center1 = Offset( (size.width / 2) + y1,(size.height / 2) + x1)
                    val center2 = Offset((size.width / 2) + y2, (size.height / 2) + x2)
                    drawLine(
                        color = Color(0xFF6DB8F5),
                        start = center1,
                        end = center2,
                        strokeWidth = 0.7f
                    )
                }
                starSight.forEach {star ->
                    val x = star[0].toFloat()
                    val y = star[1].toFloat()
                    val starColor = temperature.colorMap[temperature.getTemperature(star[2])]?:0
                    val radius = pow(10.0, 0.25 * (5.5 - star[3])).toFloat()
                    val center = Offset((size.width / 2) + y, (size.height / 2) + x)
                    val name = nameMap[star[4].toInt()]?:""
                    //TODO: gradient 작동 안 함.

//                drawCircle(center = center, radius = 3.0f * radius,
//                    brush = Brush.radialGradient(colors = listOf(Color(starColor), Color.Black), radius = 3.0f * radius))
                    drawCircle(center = center, radius = radius, color = Color(starColor))
                    Log.d("calc", name)
                }
                for(i in 0 until constSight.size / 2){
                    val x1 = constSight[2 * i][0]
                    val y1 = constSight[2 * i][1]
                    val x2 = constSight[2 * i + 1][0]
                    val y2 = constSight[2 * i + 1][1]

                    if((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1) > 160000.0) continue
                    drawLine(
                        color = Color.White,
                        start = Offset((size.width / 2) + y1.toFloat(),(size.height / 2) + x1.toFloat() ),
                        end = Offset((size.width / 2) + y2.toFloat(),(size.height / 2) + x2.toFloat() ),
                        strokeWidth = 0.5f
                    )
                }

                horizonSight.forEach {star ->
                    val x = star[0].toFloat()
                    val y = star[1].toFloat()
                    Log.d("coor", "${x} ${y}")
                    val center = Offset((size.width / 2) + y,(size.height / 2) + x )
                    drawCircle(center = center, radius = 5.0f, color = Color(0xFF4CAF50))
                }
            }
        )
        Slider(
            value = theta.toFloat(),
            onValueChange = {
                theta = it.toDouble()
            },
            valueRange = 0.0f..360.0f,
            steps = 1000,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(200.dp)
                .fillMaxWidth()
        )
    }

}