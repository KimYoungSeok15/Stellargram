package com.ssafy.stellargram.model

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