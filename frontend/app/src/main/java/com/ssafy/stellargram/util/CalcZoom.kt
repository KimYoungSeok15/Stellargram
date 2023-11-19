package com.ssafy.stellargram.util

import com.ssafy.stellargram.module.ScreenModule
import java.lang.Math.pow
import java.lang.Math.sqrt

class CalcZoom {
    fun getScreenSize(zoomLevel: Float): Float{
        val meterPerPixel = 1.4051 * pow(0.5, zoomLevel.toDouble())
        return meterPerPixel.toFloat()
    }

    fun getScreenDiameter(zoomLevel: Float): Float{
        val pixHeight = ScreenModule.gettingHeight()
        val pixWidth = ScreenModule.gettingWidth()
        val diameter = sqrt((pixHeight * pixHeight + pixWidth * pixWidth).toDouble()).toFloat()
        return diameter * getScreenSize(zoomLevel)
    }
}
