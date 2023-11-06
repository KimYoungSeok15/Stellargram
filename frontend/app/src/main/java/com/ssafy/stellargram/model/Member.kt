package com.ssafy.stellargram.model


data class MemberCheckResponse(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val status: Boolean
    )
}


/// rest is TODO
data class MemberSignUpResponse(
    val code: String,
    val message: String,
    val data: Data
) {
    data class Data(
        val status: Boolean
    )
}
data class MemberSignUpRequest(
    val code: String,
    val message: String,
    val data: Data
){
    data class Data(
        val status: Boolean
    )
}

