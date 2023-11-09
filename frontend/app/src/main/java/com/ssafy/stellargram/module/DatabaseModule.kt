package com.ssafy.stellargram.module

import android.util.Log
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DBModule {
    var starArray: Array<DoubleArray> = arrayOf()
    var nameMap: HashMap<Int, String> = hashMapOf()

    fun settingData(_starArray: Array<DoubleArray>, _nameMap: HashMap<Int, String>) {
        starArray = _starArray
        nameMap = _nameMap
        Log.d("check", "${starArray.size}")
    }

    fun gettingStarArray(): Array<DoubleArray> {
        return starArray
    }

    fun gettingNameMap(): HashMap<Int, String> {
        return nameMap
    }
}