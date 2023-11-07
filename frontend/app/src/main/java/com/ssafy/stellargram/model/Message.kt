package com.ssafy.stellargram.model

data class MessageInfo(
    val messageSeq: Long,
    val time: Long,
    val memberId: Long,
    val memberNickName: String,
    val memberImagePath: String,
    val content: String,

)