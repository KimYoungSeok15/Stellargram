package com.ssafy.stellargram.ui.screen.chat

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.ChatRoom
import com.ssafy.stellargram.model.MessageInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    val initRoomId = -1
    val initPersonnel = 0
    val initSiteId = "initSite"

    // 채팅방 정보
    private var roomId: Int = -1
    private var personnel: Int = 0
    private var siteId: String = "initSite"
    private var thisCursor: Int = 0

//    private val privateMessages : MutableList<MessageInfo>= mutableStateListOf(*initialMessages.toTypedArray())
//
//    var messages :List<MessageInfo> = privateMessages

    private val privateMessages: MutableList<MessageInfo> =
        mutableStateListOf()
    val messages: List<MessageInfo> = privateMessages

    fun addMessage(message: MessageInfo) {
        privateMessages.add(0, message)
    }

    fun initAll(message: MessageInfo) {
        roomId = initRoomId
        personnel = initPersonnel
        siteId = initSiteId
        privateMessages.clear()
    }

    fun setRoomInfo(newInfo: ChatRoom) {
        roomId = newInfo.roomId
        personnel = newInfo.personnel
        siteId = newInfo.observeSiteId
    }

    suspend fun getMessages(cursor: Int): Int? {
        if (roomId != initRoomId) {
            val response = NetworkModule.provideRetrofitInstanceChat()
                .getPrevChats(myId = TestValue.myId, chatRoomId = roomId, cursor = cursor)
            if (response?.code == 200) {
                Log.d("getMessages response",response.data.messageList.toString())
                privateMessages.addAll(response.data.messageList)
                return response.data.nextCursor
            } else {
                null
            }
            return null
        }
        return null
    }

//    suspend fun getMyRooms(): ChatRoomsData? {
//        return try {
//            val response = NetworkModule.provideRetrofitInstanceChat().getRoomList(myId = myId)
//            if (response.code == 200) {
//                response.data
//            } else {
//                null
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }

}