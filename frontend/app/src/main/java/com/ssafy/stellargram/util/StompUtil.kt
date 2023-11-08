package com.ssafy.stellargram.util

import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import io.reactivex.disposables.Disposable
import okhttp3.Connection
import okhttp3.OkHttpClient

object StompUtil {
    private lateinit var stompConnection: Disposable
    lateinit var topic: Disposable

    private val baseUrl :String = "ws://k9a101.p.ssafy.io:8000"
    private val endpoint :String= "/ws"
    private val thisUrl:String = baseUrl + endpoint
    private val intervalMillis = 1000L
    private val client = OkHttpClient().newBuilder()
//        .readTimeout(10, TimeUnit.SECONDS)
//        .writeTimeout(10, TimeUnit.SECONDS)
//        .connectTimeout(10, TimeUnit.SECONDS)
        .build()
    private val stomp = StompClient(client, intervalMillis).apply { this@apply.url = thisUrl }

    fun getStompConnection(): Disposable{
        if (stompConnection==null) stompConnection = stomp.connect().subscribe{
            when(it.type){
                Event.Type.OPENED -> {

                }
                Event.Type.CLOSED -> {

                }
                Event.Type.ERROR -> {

                }

                else -> {}
            }
        }
        return stompConnection;
    }

    fun destroyStompConnection(){
        stompConnection.dispose()

    }
}