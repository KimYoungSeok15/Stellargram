package com.ssafy.stellargram.module

import android.util.Log
import com.ssafy.stellargram.data.db.entity.Star
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DBModule {
    var starArray: Array<DoubleArray> = arrayOf()
    var nameMap: HashMap<Int, String> = hashMapOf()
    var starInfo: HashMap<Int, Int> = hashMapOf()
    var starMap: HashMap<Int, Star> = hashMapOf()
    var stars: MutableList<Star> = mutableListOf()

    fun settingData(_starArray: Array<DoubleArray>, _nameMap: HashMap<Int, String>, _starInfo: HashMap<Int, Int>, _starMap: HashMap<Int, Star>, _stars: MutableList<Star>) {
        starArray = _starArray
        nameMap = _nameMap
        starInfo = _starInfo
        starMap = _starMap
        stars = _stars
        Log.d("check", "${starArray.size}")
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

    fun gettingStarMap(): HashMap<Int, Star> {
        return starMap
    }
}