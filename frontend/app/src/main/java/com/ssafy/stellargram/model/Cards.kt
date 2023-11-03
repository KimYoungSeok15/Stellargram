package com.ssafy.stellargram.model

    data class CardsResponse(
    val code: Int,
    val message: String,
    val data: CardsData
)

data class CardsData(
    val starcards: List<Card>
)

data class Card(
    val cardId: Int,
    val memberId: Int,
    val memberNickName: String,
    val memberImagePath: String,
    val observeSiteId: String,
    val imagePath: String,
    val content: String,
    val photoAt: String,
    val category: String,
    val tools: String,
    val likeCount: Int,
    val amILikeThis: Boolean
)