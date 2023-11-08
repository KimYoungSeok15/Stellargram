package com.ssafy.stellargram.data.remote

import com.ssafy.stellargram.model.CardsResponse
import com.ssafy.stellargram.model.RoomListResponse
import com.ssafy.stellargram.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
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
    @GET("/api/cards")
    suspend fun getCards(): CardsResponse
}

// 채팅 관련
interface ApiServiceForChat{
    // 내 채팅방 목록 가져오기
    @GET("chat/rooms")
    suspend fun getRoomList(
        @Header("myId") myId:Int
    ): RoomListResponse

//    // 특정 채팅방의 이전 목록 가져오기
//    @GET("chat/open/{chatRoomId}/{cursor}")
//    suspend fun getPrevChats():
//
//            //
}

