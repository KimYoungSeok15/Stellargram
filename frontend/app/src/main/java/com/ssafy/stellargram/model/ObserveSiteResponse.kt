package com.ssafy.stellargram.model

data class ObserveSiteResponse (
    val code: Int,
    val message: String,
    val data: ObserveSite
)

data class ObserveSiteListResponse(
    val code: Int,
    val message: String,
    val data: MutableList<ObserveSite>
)
data class ObserveSite(
    val latitude: Float,
    val longitude: Float,
    val name: String,
    val reviewCount: Int,
    val ratingSum: Int
)
data class ObserveSiteRequest(
    val latitude: Double,
    val longitude: Double,
    val name: String
)