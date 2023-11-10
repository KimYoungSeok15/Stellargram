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
    var constellation: Array<DoubleArray> = arrayOf()
    var starInfo: HashMap<Int, Int> = hashMapOf()

    fun settingData(_starArray: Array<DoubleArray>, _nameMap: HashMap<Int, String>, _starInfo: HashMap<Int, Int>) {
        starArray = _starArray
        nameMap = _nameMap
        starInfo = _starInfo
        Log.d("check", "${starArray.size}")
    }

    fun settingConstellation(_constellation: Array<DoubleArray>) {
        constellation = _constellation
    }

    fun gettingStarArray(): Array<DoubleArray> {
        return starArray
    }

    fun gettingNameMap(): HashMap<Int, String> {
        return nameMap
    }

    fun gettingStarInfo(): HashMap<Int, Int> {
        return starInfo
    }

    fun gettingConstellation(): Array<DoubleArray> {
        return constellation
    }
}