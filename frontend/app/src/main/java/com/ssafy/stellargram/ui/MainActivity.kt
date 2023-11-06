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
import com.ssafy.stellargram.ui.theme.INSTARGRAMTheme
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
            val res = db.starDAO().readAll()
            res.collect {it.forEach{ star ->
                Log.d("GETSTARPROP", "ID: ${star.id}")
                }
            }
        }
    }

//        val lst = db.starDAO().findAll()
//        val lst = this.getDatabasePath("stars").absolutePath
//        Log.d("GETSTAR","$lst")
//        val db = StarDatabase.getDatabase(this)
//        Log.d("GETSTARDB","$db")
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    INSTARGRAMTheme {
        NavGraph()
    }
}