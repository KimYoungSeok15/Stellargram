package com.ssafy.stellargram.model


import com.google.gson.annotations.SerializedName


data class MemberCheckResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val status: Boolean
    )
}

data class MemberCheckDuplicateRequest(
    @SerializedName("nickname")
    val nickname : String
)

data class MemberCheckDuplicateResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val status: Boolean
    )
}

data class MemberSignUpRequest(
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("profileImageUrl")
    val profileImageUrl : String
)

data class MemberSignUpResponse(
    val code: String,
    val message: String,
    val data: Data
) {
    data class Data(
        val memberId : Int,
        val nickname : String,
        val profileImageUrl : String,
        val followCount : Int,
        val followingCount : Int,
        val cardCount : Int
    )
}


data class MemberResponse(
    val code: String,
    val message: String,
    val data: MembersData
)

data class MembersData(
    val members: MutableList<Member>
)

data class Member(
    val memberId: Int,
    val nickname: String,
    val profileImageUrl: String,
    val isFollow: Boolean,
    val followCount: Int,
    val followingCount: Int,
    val cardCount: Int
)

