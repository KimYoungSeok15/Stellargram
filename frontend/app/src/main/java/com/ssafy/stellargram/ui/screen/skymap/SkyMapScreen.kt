package com.ssafy.stellargram.ui.screen.skymap

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.ssafy.stellargram.data.db.entity.Star
import com.ssafy.stellargram.module.DBModule
import com.ssafy.stellargram.module.DBModule.starMap
import com.ssafy.stellargram.module.ScreenModule
import com.ssafy.stellargram.ui.Screen
import com.ssafy.stellargram.util.Temperature
import kotlinx.coroutines.delay
import kotlin.math.ln
import kotlin.math.pow
import kotlin.random.Random
import kotlin.math.sqrt


@SuppressLint("MissingPermission", "MutableCollectionMutableState")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SkyMapScreen(navController : NavController,viewModel: SkyMapViewModel){


    val temperature = Temperature()
//    val constellationLine: ConstellationLine = ConstellationLine()
    val context = LocalContext.current




    var longitude by remember { mutableDoubleStateOf(127.039611)}
    var latitude by remember { mutableDoubleStateOf(37.501254) }



    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    )
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        // 위치 권한이 허용되지 않은 경우
        if (!locationPermissionState.allPermissionsGranted) {
            locationPermissionState.launchMultiplePermissionRequest()
        }
        else {
            // 허용된 경우
            fusedLocationClient.lastLocation.addOnSuccessListener {
                longitude = it.longitude
                latitude = it.latitude
            }
        }
    }

//    var offsetX: Double by remember { mutableStateOf(0.0) }
//    var offsetY: Double by remember { mutableStateOf(0.0) }
    var theta by remember { mutableDoubleStateOf(180.0)}
    var phi by remember { mutableDoubleStateOf(0.0) }
//    var isDragging: Boolean by remember { mutableStateOf(false) }
    var i by remember{ mutableLongStateOf(0L) }
    var screenWidth by remember{ mutableIntStateOf(0) }
    var screenHeight by remember{mutableIntStateOf(0)}
    var starArray: Array<DoubleArray> by remember{ mutableStateOf(arrayOf()) }
    var nameMap: HashMap<Int, String> by remember{ mutableStateOf(hashMapOf()) }
    var starMap: HashMap<Int, Star> by remember{ mutableStateOf(hashMapOf()) }
    var starSight: Array<DoubleArray> by remember{ mutableStateOf(arrayOf())}
    var starInfo: HashMap<Int, Int> by remember{ mutableStateOf(hashMapOf())}
    var constSight: Array<DoubleArray> by remember{ mutableStateOf(arrayOf())}
    var constLineSight: Array<DoubleArray> by remember{ mutableStateOf(arrayOf())}
//    var horizon: Array<DoubleArray> by remember{ mutableStateOf(arrayOf())}
//    var horizonSight: Array<DoubleArray> by remember{ mutableStateOf(arrayOf())}
    var zoom by remember{ mutableFloatStateOf(1.0f) }
    var clicked: Boolean by remember{ mutableStateOf(false)}
    var clickedIndex by remember{ mutableIntStateOf(1) }
    var clickedX by remember { mutableFloatStateOf(1.0f)  }
    var clickedY by remember { mutableFloatStateOf(1.0f)  }
    LaunchedEffect(Unit) {
        // star array가 늦게 계산되기도 하기 때문에 바로 홈화면에서 화면을 키게 될 경우 아무것도 불러오지 못함.
        while(starArray.isEmpty() || starInfo.isEmpty() || screenHeight == 0 || screenWidth == 0){
            starArray = DBModule.gettingStarArray()
            nameMap = DBModule.gettingNameMap()
            starMap = DBModule.gettingStarMap()
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
            starSight = viewModel.getAllStars(longitude, latitude, zoom.toDouble(), theta, phi, 5.0 + 2.5 * ln(zoom.toDouble()), screenHeight.toDouble(), screenWidth.toDouble())
            constSight = viewModel.getAllConstellations(longitude, latitude, zoom.toDouble(), theta, phi, screenHeight.toDouble(), screenWidth.toDouble())
            constLineSight = viewModel.getAllConstellationLines(longitude, latitude, zoom.toDouble(), theta, phi, screenHeight.toDouble() * 2.0, screenWidth.toDouble() * 2.0)
//            horizonSight = viewModel.horizonSight.value
            delay(1L) // 0.4초마다 함수 호출
//            Log.d("test", "${starSight.size} ${constSight.size} ${constLineSight.size}")
//            Log.d("create", "Elapsed Time: ${System.currentTimeMillis() - i - 1L} ms")
        }
    }

    var canvasRotate by remember { mutableFloatStateOf(0.0f)}
    var orientationValues by remember { mutableStateOf(Triple(0f, 0f, 0f)) }
    val orientation = remember { Orientation(context as Activity) }
    DisposableEffect(context) {
        orientation.startListening(object : Orientation.Listener {
            override fun onOrientationChanged(pitch: Float, roll: Float, yaw: Float) {
                if (viewModel.autoMode){
                    orientationValues = Triple(pitch, roll, yaw)
                    theta = -yaw.toDouble()
                    phi = pitch.toDouble()
//                canvasRotate = roll
                }
            }
        })
        onDispose {
            orientation.stopListening()
        }
    }

    Box{
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Black
                )
                .transformable(rememberTransformableState { zoomChange: Float, panChange: Offset, rotationChange: Float ->
                    if (zoom * zoomChange in 0.7f..10f) {
                        zoom = (zoom * zoomChange)
                    }
                    theta -= (panChange.x * zoom) / 10
                    phi += (panChange.y * zoom) / 10
//                    canvasRotate += rotationChange

                })
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
                .rotate(canvasRotate)

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
                                    Color(starColor - 130 * (256 * 256 * 256).toLong()),
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
//            Text(text = "${orientationValues.first}")
//            Text(text = "${orientationValues.second}")
//            Text(text = "${orientationValues.third}")
//            Text(text="$latitude , $longitude")
//            Log.d("123", "clickedIndex: ${clickedIndex}")
//            Log.d("123", "nameMap: ${nameMap[clickedIndex]}")
//            Log.d("123", "starInfo: ${starInfo[clickedIndex]}")
//            Log.d("123", "starMap: ${starMap[clickedIndex]}")
            if (clicked) {
                ClickableText(
                    text =
                        AnnotatedString(nameMap[clickedIndex].toString()),
                    style = TextStyle(color = Color.White,
                        textAlign = TextAlign.Center),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // Navigate to the StarDetail screen
                        navController.navigate(route = "${Screen.StarDetail.route}/${starMap[clickedIndex]!!.id}")
                    }
                )
            }
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