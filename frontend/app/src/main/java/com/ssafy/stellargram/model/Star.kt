package com.ssafy.stellargram.model

data class StarResponse(
    val code: String,
    val message: String,
    val data: StarData
)

data class StarData(
    val stars: MutableList<Star>
)

data class Star(
    val name: String,
    val constellation: String,
    val rightAscension: String,
    val declination: String,
    val apparentMagnitude: String,
    val absoluteMagnitude: String,
    val distanceLightYear: String,
    val spectralClass: String
)