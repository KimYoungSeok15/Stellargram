package com.ssafy.stellargram.ui.screen.home

import android.content.Context
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.LocationManager
import android.location.LocationListener
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.WeatherItem
import com.ssafy.stellargram.model.WeatherResponse
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.ssafy.stellargram.R
import com.ssafy.stellargram.StellargramApplication
import com.ssafy.stellargram.data.remote.ApiServiceForAstronomicalEvents
import com.ssafy.stellargram.model.AstronomicalEventResponse
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController) {
    var weatherData by remember { mutableStateOf<List<WeatherItem>>(emptyList()) } // 날씨 정보
    var address by remember { mutableStateOf("") } // 현재 주소
    val coroutineScope = rememberCoroutineScope()  // 비동기 영역 선언
    val context = LocalContext.current
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var combinedEvents by remember { mutableStateOf("") }  // 이벤트 띄워주는 문구
    var isLocationUpdateComplete by remember { mutableStateOf(false) } // 위치정보를 불러왔을때만 API요청을 보내도록
    var refreshClickCount by remember { mutableStateOf(0) }  // 기상청 api 새로고침 할 때 쓰는 변수
    var memberID by remember { mutableLongStateOf(0) }

    // 현재 시간을 가져옴
    val currentTime = Date()
    val calendar = Calendar.getInstance()
    calendar.time = currentTime
    val minute = calendar.get(Calendar.MINUTE)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(currentTime)
    val currentMonth = SimpleDateFormat("MM", Locale.getDefault()).format(currentTime)
    val timeString = if (hour < 12) {
        "오전 $hour:${String.format("%02d", minute)}"
    } else if (hour == 12) {
        "오후 $hour:${String.format("%02d", minute)}"
    } else {
        "오후 ${hour - 12}:${String.format("%02d", minute)}"
    }

    // 현재 '분'이 30 이하인 경우에는 31분을 뺀다 ( 전시간 예보를 받는다 )
    if (minute <= 30) {
        calendar.add(Calendar.MINUTE, -31)
    }

    // baseDate를 yyyyMMdd 형식으로 설정
    val baseDate = SimpleDateFormat("yyyyMMdd").format(calendar.time)

    // baseTime을 hhmm 형식으로 설정
    val baseTime = SimpleDateFormat("HHmm").format(calendar.time)
    var coordinatesXy: CoordinatesXy? by remember { mutableStateOf(null) }
    Log.d("Location1","날짜가져옴, 위치 가져오기전")
    // 현재 위경도를 받아오고, 변환 함수에 넣어 nx, ny를 coordinatesXy에 저장
    val locationListener = LocationListener { location ->
        Log.d("Location1","위치정보 받아오기 시도시작")
        val latitude = location.latitude
        val longitude = location.longitude
        val currentAddress = getAddressFromLocation(context, latitude, longitude)
        address = currentAddress // address 변수 업데이트
        Log.d("Location1", "위치정보 받기완료 Latitude: $latitude, Longitude: $longitude")

        val converter = CoordinateConverter()
        val coordinates = converter.convertToXy(latitude, longitude)
        coordinatesXy = coordinates
    }


    // 기기에서 위치정보 권한을 허락 받았을 경우 vs 못 받았을 경우
    if (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        // 허용된 경우
        DisposableEffect(Unit){
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                10.0f,
                locationListener
            )
            onDispose {
                locationManager.removeUpdates(locationListener)
            }
        }
    } else {
        // 위치 권한이 허용되지 않은 경우
        // 사용자에게 위치 권한을 요청할 수 있음 ( 추후 구현 )
    }

    LaunchedEffect(Unit,memberID){
        memberID = StellargramApplication.prefs.getString("memberId","0").toLong()
    }

    // 현재 위치 업데이트가 완료되면 isLocationUpdateComplete 값을 true로 설정
    // -> 이렇게하면 위치정보를 받아온 최초 1회만 API 요청을 보낸다. (refresh 버튼 클릭할 경우에는 제외)
    LaunchedEffect(key1 = coordinatesXy) {
        coordinatesXy?.let {
            isLocationUpdateComplete = true
        }
    }


    // API 호출 및 데이터 가져오기
    LaunchedEffect(key1 = refreshClickCount, key2 = isLocationUpdateComplete) {
        if (isLocationUpdateComplete) {
            coordinatesXy?.let { coordinates ->
                Log.d("Location1", "날짜: $baseDate 시간: $baseTime")
                Log.d("weather response", "기상청API 요청 시작 전")
                CoroutineScope(Dispatchers.IO).launch {
                    val response = NetworkModule.provideRetrofitInstanceWeather().getWeatherData(
                        serviceKey = "6PIByXLX9AWtK2AOiuXIwPy7yp6W6IsXetSFkmgg6zuMUkeuSar2gkZzmq2CICLoIT9AqbQLMFOieAktc1uUoQ==",
                        pageNo = 1,
                        numOfRows = 1000,
                        dataType = "JSON",
                        baseDate = baseDate,
                        baseTime = baseTime,
                        nx = coordinates.nx, // 사용자의 위치에 따라서 coordinatesXy의 값 사용
                        ny = coordinates.ny
                    ).execute()
                    Log.d("weather response", response.toString())
                    if (response.isSuccessful) {
                        val weatherResponse: WeatherResponse? = response.body()
                        // 파싱
                        val gson = Gson()
                        val itemType = object : TypeToken<List<WeatherItem>>() {}.type
                        Log.d("weather response", "Gson 시작전")
                        val items = gson.fromJson<List<WeatherItem>>(
                            gson.toJson(weatherResponse?.response?.body?.items?.item),
                            itemType
                        )
                        Log.d("weather response", "Gson 시작후")
                        // 필터링할 카테고리 목록
                        val targetCategories = setOf("T1H", "SKY", "PTY")
                        
                    // targetCategories에 속하는 카테고리만 필터링
                    var filteredItems = emptyList<WeatherItem>()
                    if (items != null){
                        filteredItems = items.filter { it.category in targetCategories }
                    }

                        // 각 카테고리별 첫 번째 아이템 가져오기
                        val firstItems =
                            filteredItems.groupBy { it.category }.mapValues { it.value.first() }

                        // firstItems에는 각 카테고리별 첫 번째 아이템이 들어가게 됩니다.
                        weatherData = firstItems.values.toList()
                        Log.d("Location1", weatherData[0].toString())
                        Log.d("Location1", weatherData[1].toString())
                        Log.d("Location1", weatherData[2].toString())
                    }
                    Log.d("이벤트", "시작")
                    val apiService = NetworkModule.RetrofitClient.getInstance()
                        .create(ApiServiceForAstronomicalEvents::class.java)
                    val astronomicalEventsResponse: AstronomicalEventResponse? = try {
                        val response = apiService.getAstronomicalEvents(
                            solYear = currentYear,
                            solMonth = currentMonth,
                            serviceKey = "6PIByXLX9AWtK2AOiuXIwPy7yp6W6IsXetSFkmgg6zuMUkeuSar2gkZzmq2CICLoIT9AqbQLMFOieAktc1uUoQ==",
                            numOfRows = 100
                        )
                        val responseBody = response.body()
                        responseBody
                    } catch (e: Exception) {
                        Log.d("이벤트", "$e")
                        combinedEvents = "천문 이벤트 없음" // 정보 받아오기 실패시
                        null
                    }

                    astronomicalEventsResponse?.let {
                        combinedEvents = buildString {
                            it.body?.items?.item?.forEachIndexed { index, item ->
                                // 각 아이템에서 원하는 정보를 추출합니다
                                val locdateSubstring = item.locdate?.substring(2) // 3번째 글자부터 시작
                                val astroTime = item.astroTime
                                val astroEvent =
                                    if (item.astroTitle!!.isNotBlank()) item.astroTitle else item.astroEvent

                                // 정보를 하나의 문자열로 결합합니다
                                append("$locdateSubstring $astroTime $astroEvent")

                                // 이벤트 사이에 구분자를 추가합니다 (마지막 이벤트 제외)
                                if (index < it.body.items.item.size - 1) {
                                    append(" / ")
                                }
                            }
                        }
                        Log.d("이벤트", "조합된 이벤트: $combinedEvents")
                        Log.d("사진", StellargramApplication.prefs.getString("memberId","없음"))
                    }
                }
            }
        }
    }

    // Composable에서 받은 날씨 데이터 표시
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .background(Color.Transparent)
            ) {
                // 이미지, 온도, 주소 순서로 표시
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val pty = weatherData.find { it.category == "PTY" }?.fcstValue
                    val sky = weatherData.find { it.category == "SKY" }?.fcstValue

                    val ptyValue = when (pty) {
                        "0" -> "0"
                        else -> "1"
                    }
                    Log.d("Location1", "pty: $ptyValue sky: $sky")
                    val imageResource = when (ptyValue to sky) {
                        Pair("0", "1") -> R.drawable.sun
                        Pair("0", "3") -> R.drawable.sunandcloud
                        Pair("0", "4") -> R.drawable.cloud
                        Pair("1", "1") -> R.drawable.cloudyandrainy
                        Pair("1", "3") -> R.drawable.cloudyandrainy
                        Pair("1", "4") -> R.drawable.rainy
                        else -> R.drawable.question
                    }

                    val pinImage = R.drawable.address
                    val temperature = weatherData.getOrNull(2)?.fcstValue
                    val text = if (temperature != null) {
                        "${temperature}º"
                    } else {
                        ""
                    }

                    val refreshImage = R.drawable.reload

                    // 이미지를 표시
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp) // 이미지 크기 조정
                    )
                    Text(
                        text = text,
                        style = TextStyle(fontSize = 40.sp),
                        modifier = Modifier.padding(5.dp,0.dp,0.dp,0.dp)
                    )
                    // 온도와 주소를 묶어서 표시 (한 줄로 묶음)
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = pinImage),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(top = 8.dp)
                            )
                            // 주소 표시
                            Text(
                                text = address,
                                style = TextStyle(fontSize = 22.sp),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        Row {
                        // timeString 따로 표시
                            Text(
                                text = timeString,
                                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Image(
                                painter = painterResource(id = refreshImage),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(4.dp, 7.dp, 0.dp, 0.dp)
                                    .size(16.dp)
                                    .clickable {
                                        refreshClickCount++
                                    }
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 16.dp, 16.dp, 0.dp)
                    .background(Color.Transparent)
            ) {
                AutoScrollingText(combinedEvents)
                // eventText를 텍스트뷰로 추가하고 가로로 너비를 부모의 너비로 설정
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.Transparent)
            ) {
                TodaysPicture(viewModel = HomeViewModel())
            }
        }
    }
}
