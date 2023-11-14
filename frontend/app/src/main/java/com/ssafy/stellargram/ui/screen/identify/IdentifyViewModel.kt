package com.ssafy.stellargram.ui.screen.identify

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.beust.klaxon.Klaxon
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.ChatRoom
import com.ssafy.stellargram.model.MessageForReceive
import com.ssafy.stellargram.model.MessageInfo
import com.ssafy.stellargram.ui.screen.chat.TestValue
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {


}
