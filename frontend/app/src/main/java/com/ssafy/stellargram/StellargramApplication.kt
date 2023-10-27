package com.ssafy.stellargram

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StellargramApplication : Application() {
    private var instance: StellargramApplication? = null

    companion object {
        const val INSTARGRAM_APP_URI = ""
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        KakaoSdk.init(this, "36c2ac2060a5b0da3ece614fcae40854")
    }
}