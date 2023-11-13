package com.ssafy.stellargram.util

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.ssafy.stellargram.model.MessageForSend
import com.ssafy.stellargram.model.MessageInfo
import com.ssafy.stellargram.ui.screen.chat.TestValue
import io.reactivex.disposables.Disposable
import okhttp3.Connection
import okhttp3.OkHttpClient
import java.util.logging.Level

object StompUtil {
//    private lateinit var stompConnection: Disposable
//    private val topic: MutableMap<Long, Disposable> = mutableStateMapOf()
//    private val baseUrl: String = "ws://k9a101.p.ssafy.io:8000"
//    private val endpoint: String = "/ws"
//    private val thisUrl: String = baseUrl + endpoint
//    private val intervalMillis = 1000L
//    private val client = OkHttpClient().newBuilder()
////        .readTimeout(10, TimeUnit.SECONDS)
////        .writeTimeout(10, TimeUnit.SECONDS)
////        .connectTimeout(10, TimeUnit.SECONDS)
//        .build()
//    private var stomp: StompClient = StompClient(client, intervalMillis).apply { this@apply.url = thisUrl }
//
//    fun makeConnect(){
//        stompConnection = stomp.connect()
//    }
//
//    // 연결
//    fun getStompConnection(): Disposable {
//        if (stompConnection.isDisposed) stompConnection = stomp.connect().subscribe {
//            when (it.type) {
//                Event.Type.OPENED -> {
//                    Log.d("stomp:", "connection opened")
//                }
//
//                Event.Type.CLOSED -> {
//                    Log.d("stomp:", "connection closed")
//
//                }
//
//                Event.Type.ERROR -> {
//                    Log.d("stomp:", "connection makes error")
//
//                }
//
//                else -> {
//                    Log.d("stomp:", "connection event error")
//
//                }
//            }
//        }
//        return stompConnection;
//    }
//
//    // 연결 해제
//    fun destroyStompConnection() {
//        stompConnection.dispose()
//
//    }
//
//    // 방 아이디에 해당하는 채널 구독하기
//    fun subscribeToChannel(roomId: Long) {
//        var thisTopic = stomp.join("/subscribe/room/${roomId.toString()}").subscribe {
//            Log.d("subscribed", it)
//        }
//        topic.put(roomId, thisTopic)
//    }
//
//    // 방 아이디에 해당하는 채널 구독 취소하기
//    fun desubscribeToChannel(roomId: Long) {
//        var thisTopic = topic[roomId]
//        // 구독 되어있는 채널이라면
//        if (thisTopic != null) {
//            thisTopic.dispose()
//            topic.remove(roomId)
//        }
//    }
//
//    // 방 아이디에 해당하는 채널에 메세지 보내기
//    fun publishToChannel(roomId: Long, messageContent:String) {
//        var newMessage: MessageForSend = MessageForSend(TestValue.myId.toLong(),System.currentTimeMillis(),messageContent)
//
//        stomp.send("/publish/room/${roomId}",newMessage.toString()).subscribe()
//    }
}