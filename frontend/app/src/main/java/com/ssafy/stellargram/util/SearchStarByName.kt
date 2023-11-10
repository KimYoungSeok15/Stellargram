package com.ssafy.stellargram.util

import com.ssafy.stellargram.data.db.entity.Star
import com.ssafy.stellargram.module.DBModule

class SearchStarByName {

    val stars = DBModule.stars
    val createStarName = CreateStarName()
    fun searchByName(_name: String): MutableList<Star>{
        var result: MutableList<Star> = mutableListOf()
        stars.forEach{star ->
            val name = createStarName.getStarName(star).lowercase()
            if(name.contains(_name.lowercase())){
                result.add(star)
            }
        }
        return result
    }
}