package com.ssafy.instargram

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InstargramApplication : Application() {
    private var instance: InstargramApplication? = null

    companion object {
        const val INSTARGRAM_APP_URI = ""
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}