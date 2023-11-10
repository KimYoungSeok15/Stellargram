package com.ssafy.stellargram.ui.screen.skymap

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Math.PI
import java.lang.Math.abs
import java.lang.Math.asin
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt
import javax.inject.Inject
import kotlin.math.ln

@HiltViewModel
class SkyMapViewModel @Inject constructor(
) : ViewModel()  {

    var starData: MutableState<Array<DoubleArray>> = mutableStateOf(arrayOf())
    var starSight: MutableState<Array<DoubleArray>> = mutableStateOf(arrayOf())
    var names: MutableState<HashMap<Int, String>> = mutableStateOf(hashMapOf())
    var screenWidth by mutableFloatStateOf(0f)
    var screenHeight by mutableFloatStateOf(0f)
    var constellation: MutableState<Array<DoubleArray>> = mutableStateOf(arrayOf())
    var constellationSight: MutableState<Array<DoubleArray>> = mutableStateOf(arrayOf())
    var horizonSight: MutableState<Array<DoubleArray>> = mutableStateOf(arrayOf())
    fun setScreenSize(width: Int, height: Int){
        screenWidth = width.toFloat()
        screenHeight = height.toFloat()
    }
    fun createStarData(Data: Array<DoubleArray>, Names: HashMap<Int, String>){
        starData.value = Data
        names.value = Names
    }

    fun getMeanSiderealTime(longitude: Double): Double{
        val JD: Double = (System.currentTimeMillis() * 0.001) / 86400.0 +  2440587.5
        val GMST = 18.697374558 + 24.06570982441908*(JD - 2451545)
        var theta = (GMST * 15.0 + (longitude)) % 360.0
//        theta = (theta + 90.0) % 360.0
        return theta * PI / 180.0
    }

    fun getAllStars(longitude: Double, latitude: Double, sidereal: Double, starList: Array<DoubleArray>): Array<DoubleArray>{

        val new_latitude = latitude * PI / 180.0

        val sinPhi = sin(new_latitude)
        val cosPhi = cos(new_latitude)

        var starArray = Array(starList.size) {DoubleArray(6)}

        for (i in 0 until starList.size){
            val hourAngle = sidereal - starList[i][0]
            val sinDec = sin(starList[i][1])
            val cosDec = cos(starList[i][1])
            val sina = sinDec * sinPhi + cosDec * cosPhi * cos(hourAngle)
            val cosa = sqrt(1.0 - (sina * sina))
            val sinA = -sin(hourAngle) * cosDec / cosa
            val cosA = (sinDec -(sinPhi * sina)) / (cosPhi * cosa)
            starArray[i][0] = cosa * cosA
            starArray[i][1] = cosa * sinA
            starArray[i][2] = sina
            starArray[i][3] = starList[i][2]
            starArray[i][4] = starList[i][3]
            starArray[i][5] = starList[i][4]
        }
        return starArray
    }

    fun getSight(_theta: Double, _phi: Double, starData1: Array<DoubleArray>, flag: Int){
        val theta = _theta * PI / 180.0
        val phi = _phi * PI / 180.0
        val cosTheta = cos(theta)
        val sinTheta = sin(theta)
        val cosPhi = cos(phi)
        val sinPhi = sin(phi)

        val transMatrix = arrayOf(
            doubleArrayOf(cosTheta * cosPhi, -cosTheta * sinPhi, sinTheta),
            doubleArrayOf(sinTheta * cosPhi, -sinTheta * sinPhi, -cosTheta),
            doubleArrayOf(sinPhi, cosPhi, 0.0)
        )
        val resultMatrix = Array(starData1.size) {DoubleArray(5)}

        for(i in 0 until starData1.size){
            resultMatrix[i][2] = starData1[i][3]
            resultMatrix[i][3] = starData1[i][4]
            resultMatrix[i][4] = starData1[i][5]
            val temp = DoubleArray(3)
            for(j in 0 until 3){
                for(k in 0 until 3){
                    temp[j] += (starData1[i][k] * transMatrix[k][j])
                }
            }
            val a = asin(temp[2])
            val cosa = cos(a)
            if(abs(cosa) <1.0E-6){
                resultMatrix[i][0] = 0.0
                resultMatrix[i][1] = 1000000.0
                continue
            }
            val _sin = temp[1] / cosa
            val _cos = temp[0] / cosa

            val new_theta = if(_cos > 0) asin(_sin) else PI - asin(_sin)
            resultMatrix[i][0] = -800.0 * new_theta
            resultMatrix[i][1] = -800.0 * ln(abs((1 + sin(a)) / cosa))
        }
        if(flag == 0){
            starSight.value = resultMatrix
        }
        else if(flag == 1){
            constellationSight.value = resultMatrix
        }
        else{
            horizonSight.value = resultMatrix
        }
    }

    //TODO: 해당 좌표 범위에서 뽑아내기. (확대 구현 이후)
    fun getVisibleStars(_limit: Double, _xrange: Double, _yrange: Double): List<DoubleArray> {
        var visible: MutableList<DoubleArray> = mutableListOf()

        for(i in 0 until starSight.value.size){
            if(starSight.value[i][3] > _limit){
                continue
            }
            visible.add(starSight.value[i])
        }
        return visible
    }

    fun settingConstellation(_constellation: Array<DoubleArray>){
        constellation.value = _constellation
    }
}
