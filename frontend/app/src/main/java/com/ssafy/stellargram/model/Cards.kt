package com.ssafy.stellargram.model

    data class CardsResponse(
    val code: Int,
    val message: String,
    val data: CardsData
)

data class CardsData(
    val starcards: MutableList<Card>
)

data class Card(
    val cardId: Int,
    val memberId: Long,
    val memberNickname: String,
    val memberProfileImageUrl: String,
    val observeSiteId: String,
    val imagePath: String,
    val content: String,
    val photoAt: String?,
    val category: String,
    val tools: String,
    val likeCount: Int,
    val amILikeThis: Boolean
)

data class CardLikersResponse(
    val code: Int,
    val message: String,
    val data: List<Long>
)

data class CardPostResponse(
    val code: Int,
    val message: String,
    val data: Int
)