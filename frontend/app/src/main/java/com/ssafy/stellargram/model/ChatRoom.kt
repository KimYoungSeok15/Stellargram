package com.ssafy.stellargram.model

// 채팅방 1개 정보
data class ChatRoom(
    val roomId: Int,
    val personnel: Int,
    val observeSiteId: String
)

// 채팅방 리스트 응답 시 data
data class ChatRoomsData(
    val roomCount: Int,
    val roomList: List<ChatRoom>
)

// 채팅방 리스트 응답용
data class RoomListResponse(
    val code: Int,
    val message: String,
    val data: ChatRoomsData
)

// 채팅방 1개 정보 + 해당 관측소 데이터 조회 정보
data class CombinedChatRoom(
    val roomId: Int,
    val personnel: Int,
    val observeSiteId: String,
    var siteName: String?,
    val longitude: Double?,
    val latitude: Double?
)

data class JoinChatCoomResponse(
    val code: Int,
    val message: String,
    val data: roomIdInfo
)

data class roomIdInfo(
    val roomNumber:Int
)

data class LeaveChatRoomResponse(
    val code: Int,
    val message: String,
    val data: leaveChatRoomData

)

data class leaveChatRoomData(
    val chatRoomId:Int
)