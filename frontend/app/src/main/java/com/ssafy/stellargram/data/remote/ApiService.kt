package com.ssafy.stellargram.data.remote

import com.ssafy.stellargram.model.CardsResponse
import com.ssafy.stellargram.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

}
interface ApiServiceForWeather{
    @GET("/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst")
    fun getWeatherData(
        @Query("ServiceKey") serviceKey: String,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("dataType") dataType: String,
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ): Call<WeatherResponse>
}

interface ApiServiceForCards {
    @GET("/starcard")
    suspend fun getCards(): CardsResponse
}
