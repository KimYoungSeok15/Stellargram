package com.ssafy.stellargram.ui

import android.os.Bundle
import android.util.Log
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
import com.ssafy.stellargram.data.db.database.DatabaseModule
import com.ssafy.stellargram.data.db.entity.Star
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
//                exampleViewModel.createStarData(starArray, nameMap)
            }
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