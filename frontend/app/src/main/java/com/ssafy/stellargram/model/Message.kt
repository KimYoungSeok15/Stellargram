package com.ssafy.stellargram.model

data class MessageForSend(
    var memberId: Long,
    var unixTimestamp: Long,
    var content: String
)

data class MessageForReceive(
    var id: String,
    var seq: Long,
    var unixTimestamp: Long,
    var content: String,
    var memberId: Long,
    var roomId: Int
)

data class MessageInfo(
    val seq: Long,
    val time: Long,
    val memberId: Long,
    val memberNickname: String,
    val memberImagePath: String,
    val content: String,
)

data class MessagesData(
    val nextCursor: Int,
    val messageList: List<MessageInfo>
)

data class MessageListResponse(
    val code: Int,
    val message: String,
    val data: MessagesData
)

data class CursorResponse(
    val code: Int,
    val message: String,
    val data: Int
)