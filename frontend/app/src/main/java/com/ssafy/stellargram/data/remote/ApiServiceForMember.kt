package com.ssafy.stellargram.data.remote

import androidx.room.Delete
import com.ssafy.stellargram.model.AstronomicalEventResponse
import com.ssafy.stellargram.model.CardDeleteResponse
import com.ssafy.stellargram.model.CardLikeResponse
import com.ssafy.stellargram.model.CardLikersResponse
import com.ssafy.stellargram.model.CardPostResponse
import com.ssafy.stellargram.model.CardRecommendResponse
import com.ssafy.stellargram.model.CardListResponse
import com.ssafy.stellargram.model.CardsResponse
import com.ssafy.stellargram.model.CursorResponse
import com.ssafy.stellargram.model.FollowCancelResponse
import com.ssafy.stellargram.model.FollowersResponse
import com.ssafy.stellargram.model.IdListRequest
import com.ssafy.stellargram.model.IdentifyPhotoData
import com.ssafy.stellargram.model.IdentifyResponse
import com.ssafy.stellargram.model.JoinChatCoomResponse
import com.ssafy.stellargram.model.LeaveChatRoomResponse
import com.ssafy.stellargram.model.MessageListResponse
import com.ssafy.stellargram.model.RoomListResponse
import com.ssafy.stellargram.model.MemberCheckDuplicateRequest
import com.ssafy.stellargram.model.MemberCheckDuplicateResponse
import com.ssafy.stellargram.model.MemberCheckResponse
import com.ssafy.stellargram.model.MemberIdResponse
import com.ssafy.stellargram.model.MemberMeResponse
import com.ssafy.stellargram.model.MemberResponse
import com.ssafy.stellargram.model.MemberSearchRequest
import com.ssafy.stellargram.model.MemberSearchResponse
import com.ssafy.stellargram.model.MemberSignUpRequest
import com.ssafy.stellargram.model.MemberSignUpResponse
import com.ssafy.stellargram.model.ObserveSiteListResponse
import com.ssafy.stellargram.model.ObserveSiteRequest
import com.ssafy.stellargram.model.ObserveSiteResponse
import com.ssafy.stellargram.model.SiteInfoByIdResponse
import com.ssafy.stellargram.model.SiteInfoResponse
import com.ssafy.stellargram.model.StarDislikeResponse
import com.ssafy.stellargram.model.StarLikeAllResponse
import com.ssafy.stellargram.model.StarLikeResponse
import com.ssafy.stellargram.model.StarLikersCountResponse
import com.ssafy.stellargram.model.WeatherResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

interface ApiServiceForMember {

    @GET("member/check")
    suspend fun getMemberCheck(): Response<MemberCheckResponse>

    @POST("member/check-duplicate/")
    suspend fun getMemberCheckDuplicate(@Body getMemberCheckDuplicateRequest: MemberCheckDuplicateRequest): Response<MemberCheckDuplicateResponse>

    @POST("member/signup")
    suspend fun postMemberSignUP(@Body postMemberSignUpRequest: MemberSignUpRequest): Response<MemberSignUpResponse>

    // 특정회원 정보조회 (본인도 대상으로 가능)
    @GET("member/others/{userId}")
    suspend fun getMember(
        @Path("userId") userId: Long
    ): Response<MemberResponse>

    // 내 정보 조회
    @GET("member/me")
    suspend fun getMemberMe(): Response<MemberMeResponse>

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

    // 유저 닉네임 검색
    @POST("member/nickname/search")
    suspend fun findMember(
        @Body postMemberSearchRequest: MemberSearchRequest
    ): MemberSearchResponse

    // 멤버id 리스트로 멤버 정보 조회
    @POST("member/member-list")
    suspend fun getMemberListByIds(@Body memberIds: IdListRequest): Response<FollowersResponse>

    // 천체 즐겨찾기하기
    @POST("member/favorite/star/{id}/like")
    suspend fun favoriteStar(
        @Path("id") id: Int
    ): Response<StarLikeResponse>

    // 즐겨찾는 천체 모두 조회
    @GET("member/favorite/all")
    suspend fun getAllFavoriteStars(): Response<StarLikeAllResponse>

    // 특정 천체의 좋아하는 유저 수 반환
    @GET("member/favorite/count/star/{starId}")
    suspend fun starLikersCount(
        @Path("starId") starId: Int
    ): Response<StarLikersCountResponse>

    // 천체 즐겨찾기 취소
    @DELETE("member/favorite/star/{id}/dislike")
    suspend fun disLikeStar(
        @Path("id") id: Int
    ): Response<StarDislikeResponse>

}

interface ApiServiceForCards {
    // 내 카드 전체 조회
    @GET("/starcard/{memberId}")
    suspend fun getCards(
        @Path("memberId") memberId: Long
    ): Response<CardsResponse>

    // 특정회원이 좋아하는 카드 전체 조회
    @GET("/starcard/like/{memberId}")
    suspend fun getLikeCards(
        @Path("memberId") memberId: Long
    ): Response<CardsResponse>

    // 키워드로 카드 검색
    @GET("starcard/search")
    suspend fun searchStarCards(
        @Query("keyword") keyword: String,
        @Query("category") category: String = "galaxy"
    ): CardListResponse


    // 카드 Id로 좋아요한 멤버들 조회
    @GET("starcard/like-member/{cardId}")
    suspend fun getCardLikers(@Path("cardId") cardId: Int): Response<CardLikersResponse>

    // ** 카드 등록 **  - 오류 생길 가능성 큼. 테스트 안해봄. 사용 방법은 StarCardRepository.kt 파일을 참조할 것.
    // 참조 - https://ducksever.tistory.com/28 + ChatGPT
    // 사용 방법: val response = StarCardRepository.uploadCard(uri, content, photo_at, category, tool, observeSiteId)

    @Multipart
    @POST("/starcard/")
    suspend fun uploadStarCard(
        @Part imageFile: MultipartBody.Part,
        @Part("requestDto") requestDto: RequestBody
    ): Response<CardPostResponse>

    // 내 카드 삭제 - 현재 내 카드도 권한 없다고 응답옴
    @DELETE("/starcard/{cardId}")
    suspend fun deleteCard(
        @Path("cardId") cardId: Int
    ): Response<CardDeleteResponse>

    // 오늘의 카드 추천
    @GET("/starcard/recommand")
    suspend fun recommendCard(): Response<CardRecommendResponse>

    // 카드 좋아요/해제
    @GET("/starcard/{cardId}/likes")
    suspend fun likeCard(
        @Path("cardId") cardId: Int
    ): Response<CardLikeResponse>

}

interface ApiServiceForWeather {
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

    @POST("chat/join/{observeSiteId}")
    suspend fun joinChatRoom(
        @Path("observeSiteId") observeSiteId: String,
    ): JoinChatCoomResponse

    // 채팅방 나가기
    @DELETE("chat/leave/{obserbeSiteId}")
    suspend fun leaveChatRoom(
        @Path("obserbeSiteId") observeSiteId: String,
    ): LeaveChatRoomResponse
}

interface ApiServiceForObserveSite {

    //관측지 post
    @POST("observe-site/")
    suspend fun postObserveSite(
        @Body observeSiteRequest: ObserveSiteRequest
    ): ObserveSiteResponse
}

interface ApiServiceForObserveSearch {
    @GET("observe-search/")
    suspend fun getObserveSearch(
        @Query("startX") startX: Float,
        @Query("endX") endX: Float,
        @Query("startY") startY: Float,
        @Query("endY") endY: Float
    ): ObserveSiteListResponse
}

// 관측포인트 관련
interface ApiServiceForSite {

    // 관측포인트 상세 조회
    @GET("observe-site/latitude/{latitude}/longitude/{longitude}")
    suspend fun getSiteInfo(
//        @Header("myId") myId: Long,
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double,
    ): SiteInfoResponse

    @GET("observe-site/{observeSiteId}")
    suspend fun getSiteInfoById(
        @Path("observeSiteId")observeSiteId:String
    ): SiteInfoByIdResponse

}

// TODO: 멀티파트 파일 가능하도록 하기
// 인식 관련
interface ApiServiceForIdentify {
    @Multipart
    @POST("/identify/tetraIdentify")
    suspend fun getIdentifyData(
        @Part file: MultipartBody.Part,
    ): Response<IdentifyPhotoData>
}
