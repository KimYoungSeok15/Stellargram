package com.ssafy.stellargram.data.db.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ssafy.stellargram.data.db.dao.StarDAO
import com.ssafy.stellargram.data.db.entity.Star
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StarRepository (private val starDAO: StarDAO) {



    val readAll = starDAO.readAll()

    val allstars = MutableLiveData<List<Star>>()
    val foundStar = MutableLiveData<Star>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    fun initDB(){
        coroutineScope.launch(Dispatchers.IO){

        }
    }

    fun updateAllStars() {
        coroutineScope.launch(Dispatchers.IO) {
            starDAO.findAll()
        }
    }

    fun getAllStars() {
        coroutineScope.launch(Dispatchers.IO) {
            allstars.postValue(starDAO.findAll())
            Log.d("GETSTAR", "$allstars")
        }
    }
}


