package com.ssafy.stellargram.data.remote

import android.net.Uri
import com.google.gson.Gson
import com.ssafy.stellargram.model.AstronomicalEventResponse
import com.ssafy.stellargram.model.CardLikersResponse
import com.ssafy.stellargram.model.CardPostResponse
import com.ssafy.stellargram.model.CardsResponse
import com.ssafy.stellargram.model.CursorResponse
import com.ssafy.stellargram.model.FollowCancelResponse
import com.ssafy.stellargram.model.FollowersResponse
import com.ssafy.stellargram.model.MessageListResponse
import com.ssafy.stellargram.model.RoomListResponse
import com.ssafy.stellargram.model.MemberCheckDuplicateRequest
import com.ssafy.stellargram.model.MemberCheckDuplicateResponse
import com.ssafy.stellargram.model.MemberCheckResponse
import com.ssafy.stellargram.model.MemberIdResponse
import com.ssafy.stellargram.model.MemberMeResponse
import com.ssafy.stellargram.model.MemberResponse
import com.ssafy.stellargram.model.MemberSearchResponse
import com.ssafy.stellargram.model.MemberSignUpRequest
import com.ssafy.stellargram.model.MemberSignUpResponse
import com.ssafy.stellargram.model.WeatherResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.io.File
import javax.inject.Inject

interface ApiServiceForMember {

     @GET("member/check")
     suspend fun getMemberCheck(): Response<MemberCheckResponse>

     @POST("member/check-duplicate/")
     suspend fun getMemberCheckDuplicate(@Body getMemberCheckDuplicateRequest: MemberCheckDuplicateRequest): Response<MemberCheckDuplicateResponse>

     @POST("member/signup")
     suspend fun postMemberSignUP(@Body postMemberSignUpRequest : MemberSignUpRequest) : Response<MemberSignUpResponse>

     // 특정회원 정보조회 (본인도 대상으로 가능)
     @GET("member/others/{userId}")
     suspend fun getMember(
         @Path("userId") userId: Long
     ) : Response<MemberResponse>

    // 내 정보 조회
    @GET("member/me")
    suspend fun getMemberMe() : Response<MemberMeResponse>

    // 닉네임 수정
    @PATCH("member/nickname")
    suspend fun patchNickName(@Body nickname: String): Response<MemberMeResponse>

    // 프로필 이미지 수정 TODO: 파일 어떻게 넣는지 알아보기
    @PATCH("member/profile-image")
    suspend fun patchProfileImage(@Body profileImageFile: String): Response<MemberMeResponse>

    // 회원 탈퇴 -> 추후 구현 예정
    @PATCH("member/withdrawal")
    suspend fun withdrawal(@Body nickname: String): Response<MemberMeResponse>

    // 특정 사용자 팔로우 API
    @GET("member/follow/{followingId}")
    suspend fun followUser(
        @Path("followingId") followingId: Long
    ): Response<MemberCheckResponse>

    // 특정 사용자 팔로우 취소 API
    @DELETE("member/follow/{followingId}")
    suspend fun unfollowUser(
        @Path("followingId") followingId: Long
    ): Response<FollowCancelResponse>

    // 특정 멤버가 팔로우하는 멤버 목록
    @GET("member/following-list/{memberId}")
    suspend fun getFollowingList(
        @Path("memberId") memberId: Long
    ): Response<FollowersResponse>

    // 특정 멤버를 팔로잉하는 멤버 목록
    @GET("member/follow-list/{memberId}")
    suspend fun getFollowerList(
        @Path("memberId") memberId: Long
    ): Response<FollowersResponse>

    // 닉네임으로 id받아오기
    @POST("member/id")
    suspend fun getMemberIdByNickName(@Body nickname: String): Response<MemberIdResponse>
    // TODO: 실패할 경우 MemberId에 null이 들어가는 것 확인해보자.

    // 닉네임으로 유저들 검색
    @POST("member/nickname/search")
    suspend fun searchMemberByNickname(@Body searchNickname: String): Response<MemberSearchResponse>

    // 멤버id 리스트로 멤버 정보 조회
    @POST("member/member-list")
    suspend fun getMemberListByIds(@Body memberIds: List<Long>): Response<FollowersResponse>
}

interface ApiServiceForCards {
    // 내 카드 전체 조회
    @GET("/starcard/{memberId}")
    suspend fun getCards(
        @Path("memberId") memberId: Long
    ): Response<CardsResponse>

    // 특정회원이 좋아하는 카드 전체 조회
    @GET("/starcard/{memberId}")
    suspend fun getLikeCards(
        @Path("memberId") memberId: Long
    ): Response<CardsResponse>

    // 키워드로 카드 검색
    @GET("starcard/search")
    suspend fun searchStarCards(
        @Query("keyword") keyword: String,
        @Query("category") category: String = "galaxy"
    ): Response<CardsResponse>

    // 카드 Id로 좋아요한 멤버들 조회
    @GET("starcard/like-member/{cardId}")
    suspend fun getCardLikers(@Path("cardId") cardId: Int): Response<CardLikersResponse>

    // ** 카드 등록 **  - 오류 생길 가능성 큼. 테스트 안해봄. 사용 방법은 StarCardRepository.kt 파일을 참조할 것.
    // 사용 방법: val response = repository.uploadCard(imageUri, content, photo_at, category, tool, observeSiteId)
    @Multipart
    @POST("/starcard/")
    suspend fun uploadStarCard(
        @Part imageFile: MultipartBody.Part,
        @Part("requestDto") requestDto: RequestBody
    ): Response<CardPostResponse>

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


