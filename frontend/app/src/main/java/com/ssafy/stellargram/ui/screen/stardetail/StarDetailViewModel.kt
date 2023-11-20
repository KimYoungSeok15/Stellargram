package com.ssafy.stellargram.ui.screen.stardetail

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.Card
import com.ssafy.stellargram.model.Response
import com.ssafy.stellargram.model.Star
import com.ssafy.stellargram.model.StarDislikeResponse
import com.ssafy.stellargram.model.StarLikeAllResponse
import com.ssafy.stellargram.model.StarLikeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StarDetailViewModel @Inject constructor() : ViewModel() {
    private val _likeStarIds = mutableStateListOf<Int>()
    val likeStarIds: List<Int> get() = _likeStarIds

    // 천체를 즐겨찾기하는 함수
    fun favoriteStar(id: Int) {
        viewModelScope.launch {
            try {
                val response = NetworkModule.provideRetrofitInstance().favoriteStar(id)
                Log.d("상세", "디버깅 : $response")
                if (response.isSuccessful) {
                    val likeResponse = response.body()
                    likeResponse?.let {
                        // 성공적으로 즐겨찾기를 추가한 경우의 로직을 여기에 작성
                        updateLikeStarIds(likeStarIds + id)
                    }
                } else {
                    Log.d("상세", "API 호출 실패: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("상세", "API 호출 실패 $e")
            }
        }
    }

    // 즐겨찾기 취소
    fun unfavoriteStar(id: Int) {
        viewModelScope.launch {
            try {
                val response = NetworkModule.provideRetrofitInstance().disLikeStar(id)
                Log.d("상세", "디버깅 : $response")
                if (response.isSuccessful) {
                    val dislikeResponse = response.body()
                    dislikeResponse?.let {
                        // 성공적으로 즐겨찾기를 취소한 경우의 로직을 여기에 작성
                        updateLikeStarIds(likeStarIds - id)
                    }
                } else {
                    Log.d("상세", "API 호출 실패: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("상세", "API 호출 실패 $e")
            }
        }
    }

    // 현재 사용자가 즐겨찾기한 천체 목록 조회 함수
    suspend fun getFavoriteStars(): List<Int> {
        return try {
            val response = NetworkModule.provideRetrofitInstance().getAllFavoriteStars()
            Log.d("상세", "디버깅 : $response")
            if (response.isSuccessful) {
                val likeResponse = response.body()
                likeResponse?.data?.let { data ->
                    // 필요한 데이터를 추출하고 Card 객체를 생성합니다.
                    return data.map { star ->
                        star.starId
                    }
                } ?: emptyList()
            } else {
                Log.d("상세", "API 호출 실패: ${response.code()} - ${response.message()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("상세", "API 호출 실패 $e")
            emptyList()
        }
    }
    suspend fun updateLikeStarIds(newLikeStarIds: List<Int>) {
        _likeStarIds.clear()
        _likeStarIds.addAll(newLikeStarIds)
    }
}

