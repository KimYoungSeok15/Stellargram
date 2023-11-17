package com.ssafy.stellargram.data.remote

import com.ssafy.stellargram.model.MemberCheckDuplicateRequest
import com.ssafy.stellargram.model.MemberCheckDuplicateResponse
import com.ssafy.stellargram.model.MemberCheckResponse
import com.ssafy.stellargram.model.MemberSignUpRequest
import com.ssafy.stellargram.model.MemberSignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
     @GET("member/check")
     suspend fun getMemberCheck(): Response<MemberCheckResponse>

     @POST("member/check-duplicate/")
     suspend fun getMemberCheckDuplicate(@Body getMemberCheckDuplicateRequest: MemberCheckDuplicateRequest): Response<MemberCheckDuplicateResponse>

     @POST("member/signup")
     suspend fun postMemberSignUP(@Body postMemberSignUpRequest : MemberSignUpRequest) : Response<MemberSignUpResponse>
}