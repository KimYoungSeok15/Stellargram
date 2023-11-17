package com.ssafy.stellargram.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.common.util.Utility
import com.ssafy.stellargram.data.db.database.ConstellationDatabaseModule
import com.ssafy.stellargram.data.db.database.StarDatabaseModule
import com.ssafy.stellargram.data.db.entity.Star
import com.ssafy.stellargram.module.DBModule
import com.ssafy.stellargram.module.ScreenModule
import com.ssafy.stellargram.ui.theme.INSTARGRAMTheme
import com.ssafy.stellargram.util.ConstellationLine
import com.ssafy.stellargram.util.CreateStarName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Math.PI


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,false)
        setContent {
            INSTARGRAMTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 카카오 키 해쉬 발급 받아 출력
                    var keyHash = Utility.getKeyHash(this)
                    Log.d("KEY HASH",keyHash)
                    NavGraph()

                }
            }
        }


        val stardb = StarDatabaseModule.provideDatabase(this)
        lifecycleScope.launch {
            stardb.starDAO().readAll().collect{
                val _length = it.size
                val starArray = Array(it.size){DoubleArray(5)}
                val nameMap = hashMapOf<Int, String>()
                val starMap = hashMapOf<Int, Star>()
                val starInfo = hashMapOf<Int, Int>()
                val stars : MutableList<Star> = mutableListOf()
                it.forEachIndexed {
                    index: Int, star: Star ->
                    starArray[index][0] = star.rarad?:999.0
                    starArray[index][1] = star.decrad?:999.0
                    starArray[index][2] = star.ci?:999.0
                    starArray[index][3] = star.mag?:999.0
                    starArray[index][4] = star.id.toDouble()
                    val name = CreateStarName.getStarName(star)
                    starInfo.put(star.hip?:-1, index)
                    nameMap.put(star.id, name)
                    starMap.put(star.id, star)
                    stars.add(star)
                }
                setStarList(starArray)
                DBModule.settingData(starArray, nameMap, starInfo, starMap, stars)
                Log.d("Create", "Done")
            }
        }
        val constellationdb = ConstellationDatabaseModule.provideDatabase(this)
        lifecycleScope.launch {
            constellationdb.constellationDAO().readAll().collect{
                val _length = it.size
                val constellationArray = Array(it.size){DoubleArray(2)}
                for(i in 0 until it.size){
                    val st = it[i]
                    constellationArray[i][0] = st.ra * PI / 180
                    constellationArray[i][1] = st.dec * PI / 180
                }
                setLineList(constellationArray)
                Log.d("constellation", "${constellationArray.size}")
            }
        }

        fun getScreenWidth(context: Context): Int {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = wm.currentWindowMetrics
                val insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                println(windowMetrics.bounds.width() - insets.left - insets.right)
                return windowMetrics.bounds.width() - insets.left - insets.right
            } else {
                val displayMetrics = DisplayMetrics()
                wm.defaultDisplay.getMetrics(displayMetrics)
                return displayMetrics.widthPixels
            }
        }

        fun getScreenHeight(context: Context): Int {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = wm.currentWindowMetrics
                val insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                Log.d("check",(windowMetrics.bounds.width() - insets.left - insets.right).toString())
                return windowMetrics.bounds.height() - insets.bottom - insets.top

            } else {
                val displayMetrics = DisplayMetrics()
                wm.defaultDisplay.getMetrics(displayMetrics)
                return displayMetrics.heightPixels
            }
        }
        ScreenModule.settingData(getScreenWidth(this), getScreenHeight(this))
        val constellationLine = ConstellationLine()
        setConstLineList(constellationLine.lines)
    }

    external fun setStarList(stars: Array<DoubleArray>)

    external fun setLineList(constellationLineList: Array<DoubleArray>)

    external fun setConstLineList(lines: Array<DoubleArray>)

    companion object{
        init{
            System.loadLibrary("coord_convert")
        }
    }

}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    INSTARGRAMTheme {
        NavGraph()
    }
}