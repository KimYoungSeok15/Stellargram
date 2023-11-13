package com.ssafy.stellargram.ui.screen.stardetail

import androidx.lifecycle.ViewModel
import com.ssafy.stellargram.model.Star
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StarDetailViewModel @Inject constructor() : ViewModel() {
    private val TAG = "STAR DETAIL"
    fun getStarResults(text: String): List<Star> {
        // TODO: 별 검색.
        val results: List<Star> = listOf(
            Star(
                name = "Vega",
                constellation = "Lyra",
                rightAscension = "18h 36m 56.19s",
                declination = "+38° 46′ 58.8″",
                apparentMagnitude = "0.03",
                absoluteMagnitude = "0.58",
                distanceLightYear = "25",
                spectralClass = "A0Vvar"
            )
        )
        return results
    }
}