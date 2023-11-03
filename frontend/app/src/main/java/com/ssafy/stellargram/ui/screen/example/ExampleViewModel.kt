package com.ssafy.stellargram.ui.screen.example

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ssafy.stellargram.data.db.entity.Star
import com.ssafy.stellargram.data.db.repository.StarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val repository: StarRepository
) : ViewModel()  {

    val starList: LiveData<List<Star>> = repository.allstars
    val findStar: LiveData<Star> = repository.foundStar
    init {
    }
    fun getAllStars(){
        repository.getAllStars()
    }


}
