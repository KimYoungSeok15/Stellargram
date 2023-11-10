package com.ssafy.stellargram.data.remote

import com.ssafy.stellargram.model.CardsResponse
import com.ssafy.stellargram.model.MemberCheckDuplicateRequest
import com.ssafy.stellargram.model.MemberCheckDuplicateResponse
import com.ssafy.stellargram.model.MemberCheckResponse
import com.ssafy.stellargram.model.MemberSignUpRequest
import com.ssafy.stellargram.model.MemberSignUpResponse
import com.ssafy.stellargram.model.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
     @GET("member/check")
     suspend fun getMemberCheck(): Response<MemberCheckResponse>

     @POST("member/check-duplicate/")
     suspend fun getMemberCheckDuplicate(@Body getMemberCheckDuplicateRequest: MemberCheckDuplicateRequest): Response<MemberCheckDuplicateResponse>

     @POST("member/signup")
     suspend fun postMemberSignUP(@Body postMemberSignUpRequest : MemberSignUpRequest) : Response<MemberSignUpResponse>
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
