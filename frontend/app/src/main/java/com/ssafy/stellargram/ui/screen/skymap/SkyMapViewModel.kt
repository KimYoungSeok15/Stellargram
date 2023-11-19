package com.ssafy.stellargram.ui.screen.skymap

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SkyMapViewModel @Inject constructor(
) : ViewModel()  {
    var starData: MutableState<Array<DoubleArray>> = mutableStateOf(arrayOf())
    var starSight: MutableState<Array<DoubleArray>> = mutableStateOf(arrayOf())
    var names: MutableState<HashMap<Int, String>> = mutableStateOf(hashMapOf())
    var screenWidth by mutableFloatStateOf(0f)
    var screenHeight by mutableFloatStateOf(0f)
    var constellationSight: MutableState<Array<DoubleArray>> = mutableStateOf(arrayOf())
    var horizonSight: MutableState<Array<DoubleArray>> = mutableStateOf(arrayOf())
    var autoMode by mutableStateOf(false)
    fun setScreenSize(width: Int, height: Int){
        screenWidth = width.toFloat()
        screenHeight = height.toFloat()
    }
    fun createStarData(Data: Array<DoubleArray>, Names: HashMap<Int, String>){
        starData.value = Data
        names.value = Names
    }


    fun gettingClickedStar(_x: Float, _y: Float, _arr: Array<DoubleArray>): Int?{
        var res: Int? = null
        var dist: Float = 900.0f
        _arr.forEach{element ->
            val x1 = element[0]
            val y1 = element[1]
            if((_x - x1) * (_x - x1) + (_y - y1) * (_y - y1) < dist){
                res = element[4].toInt()
                dist = ((_x - x1) * (_x - x1) + (_y - y1) * (_y - y1)).toFloat()
            }
        }
        return res
    }

    external fun getAllStars(longitude: Double, latitude: Double, zoom: Double, theta: Double, phi: Double, limit: Double, screenHeight: Double, screenWidth: Double) : Array<DoubleArray>

    external fun getAllConstellations(longitude: Double, latitude: Double, zoom: Double, theta: Double, phi: Double, screenHeight: Double, screenWidth: Double) : Array<DoubleArray>

    external fun getAllConstellationLines(latitude: Double, longitude: Double,zoom: Double, theta: Double, phi: Double, screenHeight: Double, screenWidth: Double) : Array<DoubleArray>
}
