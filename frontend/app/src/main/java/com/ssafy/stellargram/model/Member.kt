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
        val memberId : Long,
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
    val data: Member
)

data class Member(
    val memberId: Long,
    val nickname: String,
    val profileImageUrl: String,
    var isFollow: Boolean,
    val followCount: Int,
    val followingCount: Int,
    val cardCount: Int
)

data class MemberMeResponse(
    val code: String,
    val message: String,
    val data: Data
) {
    data class Data(
        val memberId : Long,
        val nickname : String,
        val profileImageUrl : String,
        val followCount : Int,
        val followingCount : Int,
        val cardCount : Int
    )
}

data class FollowCancelResponse(
    val code: String,
    val message: String,
    val data: MemberMeResponse.Data
)

data class MemberSearchResponse(
    val code: Int,
    val message: String,
    val data: MemberList
)

data class MemberList(
    val members: List<Member>
)

data class MemberSearchRequest(
    @SerializedName("searchNickname")
    val searchNickname: String
)

data class IdListRequest(
    @SerializedName("memberIds")
    val memberIds: List<Long>
)

data class FollowersResponse(
    val code: String,
    val message: String,
    val data: MembersData
)

data class MembersData(
    val members: List<Member>
)

data class MemberIdResponse(
    val code: String,
    val message: String,
    val data: MemberId? // 실패한 경우에는 null이 들어갈 예정.
)

data class MemberId(
    val memberId: Long
)

data class StarLikeResponse(
    val code: String,
    val message: String,
    val data: LikeData
)

data class LikeData(
    val favoriteId: Int,
    val liker: Member,
    val starId: Int,
)

data class StarLikeAllResponse(
    val code: String,
    val message: String,
    val data: List<LikeData>
)

data class StarLikersCountResponse(
    val code: String,
    val message: String,
    val data: Int
)

data class StarDislikeResponse(
    val code: String,
    val message: String,
    val data: String // true 또는 "member already dislikes star"가 들어올 수 있음
)