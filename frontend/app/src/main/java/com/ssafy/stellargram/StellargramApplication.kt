package com.ssafy.stellargram

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

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
//        copyDatabaseFromAssets(this,"StarData.db")
    }
}


fun copyDatabaseFromAssets(context: Context, dbName: String) {
    val assetManager = context.assets

    val inputStream: InputStream
    val outputStream: OutputStream
    try {
        inputStream = assetManager.open(dbName)
        val dbFilePath = context.getDatabasePath(dbName)
        outputStream = FileOutputStream(dbFilePath)

        val buffer = ByteArray(8192)
        var length: Int
        while (inputStream.read(buffer, 0, 8192).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}