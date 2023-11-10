package com.ssafy.stellargram.model

data class MessageForSend(
    var memberId: Long,
    var unixTimestamp: Long,
    var content: String
)

data class MessageForReceive(
    var seq: Long,
    var time: Long,
    var memberId: Long,
    var memberNickName: String,
    var  memberImagePath:  String,
    var content: String,
    var roomId: Int
)

data class MessageInfo(
    val seq: Long,
    val time: Long,
    val memberId: Long,
    val memberNickName: String,
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