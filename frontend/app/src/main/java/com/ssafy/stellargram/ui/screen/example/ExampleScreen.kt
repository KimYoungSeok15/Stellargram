package com.ssafy.stellargram.ui.screen.example

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ssafy.stellargram.data.db.entity.Star

@Composable
fun ExampleScreen(navController : NavController, modifier: Modifier){
    val viewModel: ExampleViewModel = hiltViewModel()
    viewModel.getAllStars()
    val starlist: List<Star> by viewModel.starList.observeAsState(initial = listOf())
    Log.d("GETSTAR","$starlist")
    val lazyListState = rememberLazyListState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
    ){
        Button(modifier=Modifier,onClick={viewModel.getAllStars()}){
            Text("눌러서 데이터 반환")
        }
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier.padding(vertical = 4.dp),
            state = lazyListState
        ){
            items(items = starlist) {Star ->
                Log.d("GETSTAR","${Star}")
                Text("${Star.id}")
            }

        }
    }
}

