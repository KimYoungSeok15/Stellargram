package com.ssafy.stellargram.ui.screen.skymap

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ssafy.stellargram.module.DBModule
import com.ssafy.stellargram.module.ScreenModule
import com.ssafy.stellargram.util.ConstellationLine
import com.ssafy.stellargram.util.Temperature
import kotlinx.coroutines.delay
import java.lang.Math.log
import java.lang.Math.pow
import java.lang.Math.sqrt
import kotlin.math.pow
import kotlin.random.Random


@Composable
fun SkyMapScreen(navController : NavController){


    val viewModel : SkyMapViewModel = viewModel()
    val temperature: Temperature = Temperature()
    val constellationLine: ConstellationLine = ConstellationLine()

    // TODO: 기기로부터 정보를 받는 거로 고쳐야 함.
    val longitude: Double = 127.039611
    val latitude: Double = 37.501254

    // TODO: zoom 확대하였을 때
    var offsetX: Double by remember { mutableStateOf(0.0) }
    var offsetY: Double by remember { mutableStateOf(0.0) }
    var theta: Double by remember { mutableStateOf(180.0)}
    var phi: Double by remember { mutableStateOf(0.0) }
    var isDragging: Boolean by remember { mutableStateOf(false) }
    var i: Long by remember{mutableStateOf(0L)}
    var screenWidth: Int by remember{mutableStateOf(0)}
    var screenHeight: Int by remember{mutableStateOf(0)}
    var starArray: Array<DoubleArray> by remember{ mutableStateOf(arrayOf()) }
    var nameMap: HashMap<Int, String> by remember{ mutableStateOf(hashMapOf()) }
    var starSight: Array<DoubleArray> by remember{ mutableStateOf(arrayOf())}
    var starInfo: HashMap<Int, Int> by remember{ mutableStateOf(hashMapOf())}
    var constSight: Array<DoubleArray> by remember{ mutableStateOf(arrayOf())}
    var constLineSight: Array<DoubleArray> by remember{ mutableStateOf(arrayOf())}
    var horizon: Array<DoubleArray> by remember{ mutableStateOf(arrayOf())}
    var horizonSight: Array<DoubleArray> by remember{ mutableStateOf(arrayOf())}
    var zoom: Float by remember{ mutableStateOf(1.0f)}
    var clicked: Boolean by remember{ mutableStateOf(false)}
    var clickedIndex: Int by remember{ mutableStateOf(1)}
    var clickedX by remember { mutableStateOf(1.0f)  }
    var clickedY by remember { mutableStateOf(1.0f)  }
    LaunchedEffect(Unit) {
        // star array가 늦게 계산되기도 하기 때문에 바로 홈화면에서 화면을 키게 될 경우 아무것도 불러오지 못함.
        while(starArray.isEmpty() || starInfo.isEmpty() || screenHeight == 0 || screenWidth == 0){
            starArray = DBModule.gettingStarArray()
            nameMap = DBModule.gettingNameMap()
            screenHeight = ScreenModule.gettingHeight()
            screenWidth = ScreenModule.gettingWidth()
            starInfo = DBModule.gettingStarInfo()
//            horizon = Array(3600){DoubleArray(6){0.0} }
//            for(i in 0 until 3600){
//                horizon[i][0] = cos(i.toDouble() * PI / 1800.0)
//                horizon[i][1] = sin(i.toDouble() * PI / 1800.0)
//            }
            delay(50L)
        }
        viewModel.createStarData(starArray, nameMap)
        viewModel.setScreenSize(screenWidth, screenHeight)

        while (true) {
            i = System.currentTimeMillis()
            starSight = viewModel.getAllStars(longitude, latitude, zoom.toDouble(), theta, phi, 5.0 + 2.5 * log(zoom.toDouble()), screenHeight.toDouble(), screenWidth.toDouble())
            constSight = viewModel.getAllConstellations(longitude, latitude, zoom.toDouble(), theta, phi, screenHeight.toDouble(), screenWidth.toDouble())
            constLineSight = viewModel.getAllConstellationLines(longitude, latitude, zoom.toDouble(), theta, phi, screenHeight.toDouble() * 2.0, screenWidth.toDouble() * 2.0)
//            horizonSight = viewModel.horizonSight.value
            delay(1L) // 0.4초마다 함수 호출
//            Log.d("test", "${starSight.size} ${constSight.size} ${constLineSight.size}")
//            Log.d("create", "Elapsed Time: ${System.currentTimeMillis() - i - 1L} ms")
        }
    }
    // TODO: launched effect 안 먹음
//    LaunchedEffect(theta, phi, zoom){
//        starSight = viewModel.getAllStars(longitude, latitude, zoom.toDouble(), theta, phi, 5.0, screenHeight.toDouble(), screenWidth.toDouble())
//        constSight = viewModel.getAllConstellations(longitude, latitude, zoom.toDouble(), theta, phi, screenHeight.toDouble(), screenWidth.toDouble())
//    }

    Box(){
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Black
                )
                .transformable(rememberTransformableState { zoomChange: Float, panChange: Offset, rotationChange: Float ->
                    zoom = (zoom * zoomChange)
                    theta -= (panChange.x * zoom) / 10
                    phi += (panChange.y * zoom) / 10

                })
                .clickable { }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            clickedX = it.x
                            clickedY = it.y
                            val newX = it.x - (size.width / 2)
                            val newY = it.y - (size.height / 2)
                            val ind = viewModel.gettingClickedStar(newY, newX, starSight)
                            if (ind == null) {
                                clicked = false
                            } else {
                                clicked = true
                                clickedIndex = ind
                            }
                        }
                    )
                }
//                .pointerInput(Unit){
//                    detectDragGestures(
//                        onDrag = {_, dragAmount ->
//                            theta -= (dragAmount.x * zoom) / 10
//                            phi += (dragAmount.y * zoom) / 10
////                            change.consume()
//                        }
//                    )
//                }
//                .pointerInput(Unit) {
//                   detectTransformGestures { _, pan, _zoom, rotate ->
//                        zoom = (zoom * _zoom)
//                   }
//                }
            ,onDraw = {
                constLineSight.forEach{line ->
                    val x1 = line[0]
                    val y1 = line[1]
                    val x2 = line[2]
                    val y2 = line[3]

                    if((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1) < 1000000){
                        drawLine(
                            color = Color(0xFFFFEB3B),
                            start = Offset((size.width * 0.5f) + y1.toFloat(),(size.height * 0.5f) + x1.toFloat() ),
                            end = Offset((size.width * 0.5f) + y2.toFloat(),(size.height * 0.5f) + x2.toFloat() ),
                            strokeWidth = 0.7f
                        )
                    }
                }
                starSight.forEach {star ->
                    if(star[4].toInt() != 0 ) {
                        val x = star[0].toFloat()
                        val y = star[1].toFloat()
                        val starColor = (temperature.colorMap[temperature.getTemperature(star[2])]
                            ?: 0) % (256 * 256 * 256) + Random.nextInt(240, 255) * (256 * 256 * 256)
                        val radius = sqrt(zoom.toDouble()).toFloat() * 10.0.pow(0.13 * (9.0 - star[3] + 0.2 * Random.nextFloat())).toFloat()
                        val center = Offset((size.width / 2) + y, (size.height / 2) + x)
//                        val name = nameMap[star[4].toInt()] ?: ""

                        drawCircle(
                            center = center, radius = radius,
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(starColor),
                                    Color(starColor - 30 * (256 * 256 * 256)),
                                    Color(starColor - 50 * (256 * 256 * 256)),
                                    Color(starColor - 70 * (256 * 256 * 256)),
                                    Color(starColor - 90 * (256 * 256 * 256)),
                                    Color(starColor - 110 * (256 * 256 * 256)),
                                    Color(starColor - 130 * (256 * 256 * 256)),
                                    Color.Black
                                ),
                                center = center,
                                radius = radius * 1.1f,
                                tileMode = TileMode.Repeated
                            )
                        )
                    }
                }
                constSight.forEach{line ->
                    val x1 = line[0]
                    val y1 = line[1]
                    val x2 = line[2]
                    val y2 = line[3]

                    if((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1) < 160000){
                        drawLine(
                            color = Color.White,
                            start = Offset((size.width * 0.5f) + y1.toFloat(),(size.height * 0.5f) + x1.toFloat() ),
                            end = Offset((size.width * 0.5f) + y2.toFloat(),(size.height * 0.5f) + x2.toFloat() ),
                            strokeWidth = 0.5f
                        )
                    }
                }
                if(clicked){
                    drawCircle(
                        color = Color.Cyan,
                        center = Offset(clickedX,clickedY),
                        radius = 30f,
                        style = Stroke()
                    )
                }

//                horizonSight.forEach {star ->
//                    val x = star[0].toFloat()
//                    val y = star[1].toFloat()
//                    val center = Offset((size.width / 2) + y,(size.height / 2) + x )
//                    drawCircle(center = center, radius = 5.0f, color = Color(0xFF4CAF50))
//                }
            }

        )

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ){
            if(clicked) Text("${nameMap[clickedIndex]}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.White)
//            Slider(
//                value = zoom,
//                onValueChange = {
//                    zoom = it
//                },
//                valueRange = 1.0f..10.0f,
//                steps = 1000,
//                modifier = Modifier
//                    .height(100.dp)
//                    .fillMaxWidth()
//            )
        }

    }

}