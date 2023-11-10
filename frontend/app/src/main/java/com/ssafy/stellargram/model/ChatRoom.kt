package com.ssafy.stellargram.model

data class ChatRoom(
    val roomId: Int,
    val personnel: Int,
    val observeSiteId: String
)

data class ChatRoomsData(
    val roomCount:Int,
    val roomList: List<ChatRoom>
)

data class RoomListResponse(
    val code: Int,
    val message: String,
    val data: ChatRoomsData
)
