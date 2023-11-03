package com.ssafy.stellargram.model

// 날씨정보 model 규정
data class WeatherResponse(
    val response: Response
)

data class Response(
    val body: Body
)

data class Body(
    val items: Items
)

data class Items(
    val item: List<WeatherItem>
)

data class WeatherItem(
    val baseDate: String,
    val baseTime: String,
    val category: String,
    val fcstDate: String,
    val fcstTime: String,
    val fcstValue: String,
    val nx: Int,
    val ny: Int
)
