package com.ssafy.stellargram.util

import com.ssafy.stellargram.data.db.entity.Star

class CreateStarName {
    fun getStarName(star: Star): String {
        //원래의 고유 이름이 있으면 반환
        if(star.proper != null && star.proper != ""){
            return star.proper
        }
        // hip 번호가 있으면 반환
        if(star.hip != null) return "HIP ${star.hip}"
        // hd 번호가 있으면 반환
        if(star.hd != null) return "HD ${star.hd}"
        // hr 번호가 있으면 반환
        if(star.hr != null) return "HR ${star.hr}"
        return "HYG ${star.id}"
    }


    companion object {
        fun getStarName(star: Star): String{
            //원래의 고유 이름이 있으면 반환
            if(star.proper != null && star.proper != ""){
                return star.proper
            }
            // hip 번호가 있으면 반환
            if(star.hip != null) return "HIP ${star.hip}"
            // hd 번호가 있으면 반환
            if(star.hd != null) return "HD ${star.hd}"
            // hr 번호가 있으면 반환
            if(star.hr != null) return "HR ${star.hr}"
            return "HYG ${star.id}"
        }
    }
}