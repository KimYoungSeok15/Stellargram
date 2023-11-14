package com.ssafy.stellargram.data.remote

import com.ssafy.stellargram.model.AstronomicalEventResponse
import com.ssafy.stellargram.model.CardsResponse
import com.ssafy.stellargram.model.CursorResponse
import com.ssafy.stellargram.model.MessageListResponse
import com.ssafy.stellargram.model.RoomListResponse
import com.ssafy.stellargram.model.MemberCheckDuplicateRequest
import com.ssafy.stellargram.model.MemberCheckDuplicateResponse
import com.ssafy.stellargram.model.MemberCheckResponse
import com.ssafy.stellargram.model.MemberMeResponse
import com.ssafy.stellargram.model.MemberSignUpRequest
import com.ssafy.stellargram.model.MemberSignUpResponse
import com.ssafy.stellargram.model.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
     @GET("member/check")
     suspend fun getMemberCheck(): Response<MemberCheckResponse>

     @POST("member/check-duplicate/")
     suspend fun getMemberCheckDuplicate(@Body getMemberCheckDuplicateRequest: MemberCheckDuplicateRequest): Response<MemberCheckDuplicateResponse>

     @POST("member/signup")
     suspend fun postMemberSignUP(@Body postMemberSignUpRequest : MemberSignUpRequest) : Response<MemberSignUpResponse>

     @GET("member/me")
     suspend fun getMemberMe() : Response<MemberMeResponse>

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
interface ApiServiceForAstronomicalEvents {
    @GET("B090041/openapi/service/AstroEventInfoService/getAstroEventInfo")
    suspend fun getAstronomicalEvents(
        @Query("solYear") solYear: String,
        @Query("solMonth") solMonth: String,
        @Query("ServiceKey") serviceKey: String,
        @Query("numOfRows") numOfRows: Int
    ): Response<AstronomicalEventResponse>
}

interface ApiServiceForCards {
    @GET("/starcard")
    suspend fun getCards(): CardsResponse
}

// 채팅 관련
interface ApiServiceForChat {
    // 내 채팅방 목록 가져오기
    @GET("chat/rooms")
    suspend fun getRoomList(
        @Header("myId") myId: Long
    ): RoomListResponse

    // 특정 채팅방의 이전 메세지 가져오기
    @GET("chat/open/{chatRoomId}/{cursor}")
    suspend fun getPrevChats(
        @Header("myId") myId: Long,
        @Path("chatRoomId") chatRoomId: Int,
        @Path("cursor") cursor: Int,
    ): MessageListResponse

    // 특정 채팅방의 가장 마지막 커서 가져오기
    @GET("chat/recentCurser/{chatRoomId}")
    suspend fun getRecentCursor(
        @Path("chatRoomId") chatRoomId: Int,
    ): CursorResponse
}

