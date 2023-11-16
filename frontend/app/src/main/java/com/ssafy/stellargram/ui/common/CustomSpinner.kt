package com.ssafy.stellargram.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ssafy.stellargram.ui.theme.Purple80

@Composable
fun CustomSpinner() {
        CircularProgressIndicator(color = Purple80)
}

@Composable
fun CustomSpinnerProgress(){
    var progress by remember { mutableStateOf(0.1f) }
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = "spinnerAnimation"
    ).value

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        CircularProgressIndicator()

        CircularProgressIndicator(progress = animatedProgress)
    }
}