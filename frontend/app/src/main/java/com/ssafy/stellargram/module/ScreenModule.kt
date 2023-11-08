package com.ssafy.stellargram.module

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ScreenModule {
    var width: Int = 0
    var height: Int = 0

    fun settingData(_width: Int, _height: Int){
        width = _width
        height = _height
    }

    fun gettingWidth(): Int {
        return width
    }

    fun gettingHeight(): Int{
        return height
    }
}