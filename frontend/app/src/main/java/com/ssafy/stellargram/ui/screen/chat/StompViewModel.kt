package com.ssafy.stellargram.ui.screen.chat

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.beust.klaxon.Klaxon
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.ssafy.stellargram.model.MessageForSend
import com.ssafy.stellargram.model.MessageInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class StompViewModel @Inject constructor() : ViewModel() {

}