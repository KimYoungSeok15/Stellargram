package com.ssafy.stellargram.model

data class SiteInfo(
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val reviewCount: Int,
    val ratingSum: Int
)

data class SiteInfoResponse(
    val code: Int,
    val message: String,
    val data: SiteInfo
)

data class SiteInfoByIdResponse(
    val code: Int,
    val message: String,
    val data: SiteInfoById
)

data class SiteInfoById(
    val observeSite: String,
    val name:String,
    val latitude: Double,
    val longitude: Double,
    val reviewCount: Int,
    val ratingSum: Int,
    val memberId: Long
)