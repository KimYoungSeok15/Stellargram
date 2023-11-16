package com.ssafy.stellargram.model

data class CardsResponse(
    val code: Int,
    val message: String,
    val data: CardsData
)

data class CardsData(
    val starcards: MutableList<Card>
)

data class CardListResponse(
    val code: Int,
    val message: String,
    val data: starcardsData
)

data class starcardsData(
    val starcards: List<CardResponse>
)

data class CardResponse(
    val cardId: Int,
    val memberId: Long,
    val observeSiteId: String,
    val imagePath: String,
    val imageUrl: String,
    val content: String,
    val photoAt: String?,
    val category: String?,
    val tools: String,
    val likeCount: Int,
    val amILikeThis: Boolean
)

data class Card(
    val cardId: Int,
    val memberId: Long,
    val memberNickname: String,
    val memberProfileImageUrl: String,
    val observeSiteId: String,
    val imagePath: String,
    val imageUrl: String,
    val content: String,
    val photoAt: String?,
    val category: String?,
    val tools: String,
    val likeCount: Int,
    val amILikeThis: Boolean,
    var isFollowing: Boolean  // 작가를 내가 팔로잉 하는지 추후에 판단하기 위한 변수
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

data class CardDeleteResponse(
    val code: Int,
    val message: String,
    val data: String
)

data class CardLikeResponse(
    val code: Int,
    val message: String,
    val data: String
)

data class CardRecommendResponse(
    val code: Int,
    val message: String,
    val data: StarCards
)

data class StarCards(
    val starcard: BestCard?
)

data class BestCard(
    val cardId: Int,
    val memberId: Long,
    val memberNickname: String,
    val memberProfileImageUrl: String,
    val observeSiteId: String,
    val imagePath: String,
    val imageUrl: String,
    val content: String,
    val photoAt: String?,
    val category: String?,
    val tools: String?,
    var likeCount: Int?,
    var amILikeThis: Boolean,
    var isFollowing: Boolean
)
