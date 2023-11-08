package com.ssafy.stellargram.model

data class MessageInfo(
    val messageSeq: Long,
    val time: Long,
    val memberId: Long,
    val memberNickName: String,
    val memberImagePath: String,
    val content: String,

)

data class MessagesData(
    val nextCursor:Int,
    val messageList:List<MessageInfo>
)

data class MessageListResponse(
    val code: Int,
    val message: String,
    val data: MessagesData
)