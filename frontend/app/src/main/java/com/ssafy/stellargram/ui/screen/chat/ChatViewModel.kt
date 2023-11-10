package com.ssafy.stellargram.ui.screen.chat

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.beust.klaxon.Klaxon
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.ChatRoom
import com.ssafy.stellargram.model.MessageInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    val initRoomId :Int = -1
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
                Log.d("getMessages response", response.data.messageList.toString())
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
    
    // STOMP 관련

    private lateinit var stompConnection: Disposable
    private lateinit var topic: Disposable

    private val baseUrl: String = "ws://k9a101.p.ssafy.io:8000"
    private val endpoint: String = "/ws"
    private val thisUrl: String = baseUrl + endpoint
    private val intervalMillis = 1000L
    private val client = OkHttpClient().newBuilder()
//        .readTimeout(10, TimeUnit.SECONDS)
//        .writeTimeout(10, TimeUnit.SECONDS)
//        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

    private var stomp: StompClient =
        StompClient(client, intervalMillis).apply { this@apply.url = thisUrl }

    fun makeConnect() {
        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                Event.Type.OPENED -> {

                    subscribeToChannel(roomId)
                    Log.d("stomp:", "connection opened")
                }

                Event.Type.CLOSED -> {
                    Log.d("stomp:", "connection closed")

                }

                Event.Type.ERROR -> {
                    Log.d("stomp:", "connection makes error")

                }

                else -> {
                    Log.d("stomp:", "connection event error")

                }
            }
        }
    }

    // 연결 해제
    fun destroyStompConnection() {
        stompConnection.dispose()

    }

    // 방 아이디에 해당하는 채널 구독하기
    fun subscribeToChannel(roomId: Int) {
        var thisTopic =
            stomp.join("/subscribe/room/${roomId.toString()}").subscribe { stompMessage ->
                val result = Klaxon()
                    .parse<MessageInfo>(stompMessage)
                if (result != null)
                    addMessage(result)
            }
        topic = thisTopic
    }

    // 방 아이디에 해당하는 채널 구독 취소하기
    fun desubscribeToChannel() {
        topic.dispose()
    }

    // 방 아이디에 해당하는 채널에 메세지 보내기
    fun publishToChannel(roomId: Int, messageContent: String) {
        val newMessage = JSONObject()

        newMessage.put("memberId", TestValue.myId.toLong())
        newMessage.put("unixTimestamp", System.currentTimeMillis())
        newMessage.put("content", messageContent)
        stomp.send("/publish/room/${roomId}", newMessage.toString()).subscribe()
    }


}