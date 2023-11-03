package com.ssafy.stellargram.ui.screen.home

import android.content.Context
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.Address
import android.location.Geocoder
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
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.tan
import kotlin.math.sqrt
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.atan
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.WeatherItem
import com.ssafy.stellargram.model.WeatherResponse
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
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
import java.io.IOException
import java.util.Calendar
import java.util.Date
import java.util.Locale

// 기상청에 맞게 위도 경도를 수정하기 위한 상수들
const val COEFFICIENT_TO_RADIAN = Math.PI / 180.0
const val GRID_UNIT_COUNT = 6371.00877 / 5.0
const val REF_X = 43.0
const val REF_Y = 136.0
const val REF_LON_RAD = 126.0 * COEFFICIENT_TO_RADIAN
const val REF_LAT_RAD = 38.0 * COEFFICIENT_TO_RADIAN
const val PROJ_LAT_1_RAD = 30.0 * COEFFICIENT_TO_RADIAN
const val PROJ_LAT_2_RAD = 60.0 * COEFFICIENT_TO_RADIAN


// api에 담을 nx, ny가 담긴 변수
data class CoordinatesXy(val nx: Int, val ny: Int)
data class CoordinatesLatLon(val lat: Double, val lon: Double)

// 경도 위도 -> nx, ny 변환 or 반대도 가능한 함수
class CoordinateConverter {
    private val sn = ln(cos(PROJ_LAT_1_RAD) / cos(PROJ_LAT_2_RAD)) / ln(tan(Math.PI * 0.25 + PROJ_LAT_2_RAD * 0.5) / tan(Math.PI * 0.25 + PROJ_LAT_1_RAD * 0.5))
    private val sf = tan(Math.PI * 0.25 + PROJ_LAT_1_RAD * 0.5).pow(sn) * cos(PROJ_LAT_1_RAD) / sn
    private val ro = GRID_UNIT_COUNT * sf / tan(Math.PI * 0.25 + REF_LAT_RAD * 0.5).pow(sn)

    // 위경도 -> xy
    internal fun convertToXy(lat: Double, lon: Double): CoordinatesXy {
        val ra = GRID_UNIT_COUNT * sf / tan(Math.PI * 0.25 + lat * COEFFICIENT_TO_RADIAN * 0.5).pow(sn)
        val theta: Double = lon * COEFFICIENT_TO_RADIAN - REF_LON_RAD
        val niceTheta = if (theta < -Math.PI) {
            theta + 2 * Math.PI
        } else if (theta > Math.PI) {
            theta - 2 * Math.PI
        } else theta

        return CoordinatesXy(
            nx = floor(ra * sin(niceTheta * sn) + REF_X + 0.5).toInt(),
            ny = floor(ro - ra * cos(niceTheta * sn) + REF_Y + 0.5).toInt()
        )
    }

    //xy -> 위경도
    internal fun convertToLatLon(nx: Double, ny: Double): CoordinatesLatLon {
        val diffX: Double = nx - REF_X
        val diffY: Double = ro - ny + REF_Y
        val distance = sqrt(diffX * diffX + diffY * diffY)
        val latSign: Int = if (sn < 0) -1 else 1
        val latRad = 2 * atan((GRID_UNIT_COUNT * sf / distance).pow(1.0 / sn)) - Math.PI * 0.5

        val theta: Double = if (abs(diffX) <= 0) 0.0 else {
            if (abs(diffY) <= 0) {
                if (diffX < 0) -Math.PI * 0.5 else Math.PI * 0.5
            } else atan2(diffX, diffY)
        }

        val lonRad = theta / sn + REF_LON_RAD

        return CoordinatesLatLon(
            lat = (latRad * latSign) / COEFFICIENT_TO_RADIAN,
            lon = lonRad / COEFFICIENT_TO_RADIAN
        )
    }
}
// 현재 주소를 위경도를 통해 받아오는 함수
fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    var addressText = ""
    try {
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            // 주소를 원하는 형식으로 조합하거나 필요한 부분만 추출할 수 있습니다.
            addressText = address.thoroughfare ?: "주소 정보 없음"
        } else {
            addressText = "주소 정보 없음"
        }
    } catch (e: IOException) {
        e.printStackTrace()
        addressText = "주소 정보 없음"
    }
    Log.d("Location1", "getAddressFromLocation: $addressText")
    return addressText
}

// 메인함수
@Composable
fun HomeScreen(navController: NavController) {
    var weatherData by remember { mutableStateOf<List<WeatherItem>>(emptyList()) }
    var address by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // 현재 시간을 가져옴
    val currentTime = Date()
    val calendar = Calendar.getInstance()
    calendar.time = currentTime
    val minute = calendar.get(Calendar.MINUTE)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    Log.d("Location1", "$hour")
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
    // 현재 위경도를 받아오고, 변환 함수에 넣어 nx, ny를 coordinatesXy에 저장
    val locationListener = LocationListener { location ->
        val latitude = location.latitude
        val longitude = location.longitude
        val currentAddress = getAddressFromLocation(context, latitude, longitude)
        address = currentAddress // address 변수 업데이트
        Log.d("Location1", "Latitude: $latitude, Longitude: $longitude")

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
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            1.0f,
            locationListener
        )
    } else {
        // 위치 권한이 허용되지 않은 경우
        // 사용자에게 위치 권한을 요청할 수 있음 ( 추후 구현 )
    }
    // API 호출 및 데이터 가져오기
    LaunchedEffect(key1 = coordinatesXy) {
        coordinatesXy?.let { coordinates ->
            Log.d("Location1", "날짜: $baseDate 시간: $baseTime")
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
                    val items = gson.fromJson<List<WeatherItem>>(
                        gson.toJson(weatherResponse?.response?.body?.items?.item),
                        itemType
                    )
                    // 필터링할 카테고리 목록
                    val targetCategories = setOf("T1H", "SKY", "PTY")

                    // targetCategories에 속하는 카테고리만 필터링
                    val filteredItems = items.filter { it.category in targetCategories }

                    // 각 카테고리별 첫 번째 아이템 가져오기
                    val firstItems =
                        filteredItems.groupBy { it.category }.mapValues { it.value.first() }

                    // firstItems에는 각 카테고리별 첫 번째 아이템이 들어가게 됩니다.
                    weatherData = firstItems.values.toList()
                    Log.d("Location1", weatherData[0].toString())
                    Log.d("Location1", weatherData[1].toString())
                    Log.d("Location1", weatherData[2].toString())
                }
            }
        }
    }

    // Composable에서 받은 날씨 데이터 표시
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(30.dp, 0.dp),
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
                AutoScrollingText()
                // eventText를 텍스트뷰로 추가하고 가로로 너비를 부모의 너비로 설정
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.Transparent)
            ) {
                TodaysPicture()
            }
        }
    }
}

