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
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.kakao.sdk.common.util.Utility
import com.ssafy.stellargram.data.db.database.StarDatabase
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.common.util.Utility
import com.ssafy.stellargram.data.db.database.DatabaseModule
import com.ssafy.stellargram.data.db.entity.Star
import com.ssafy.stellargram.module.DBModule
import com.ssafy.stellargram.module.ScreenModule
import com.ssafy.stellargram.ui.screen.example.ExampleViewModel
import com.ssafy.stellargram.ui.theme.INSTARGRAMTheme
import com.ssafy.stellargram.util.CreateStarName
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


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


        val db = DatabaseModule.provideDatabase(this)
        lifecycleScope.launch {
            db.starDAO().readAll().collect{
                val _length = it.size
                val starArray = Array(it.size){DoubleArray(5)}
                val nameMap = hashMapOf<Int, String>()
                it.forEachIndexed {
                    index: Int, star: Star ->
                    starArray[index][0] = star.rarad?:999.0
                    starArray[index][1] = star.decrad?:999.0
                    starArray[index][2] = star.ci?:999.0
                    starArray[index][3] = star.mag?:999.0
                    starArray[index][4] = star.id.toDouble()
                    val name = CreateStarName.getStarName(star)
                    nameMap.put(star.id, name)
                }
                DBModule.settingData(starArray, nameMap)
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

    }

}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    INSTARGRAMTheme {
        NavGraph()
    }
}