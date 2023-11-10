package com.ssafy.stellargram.ui.screen.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SignUpScreen(navController: NavHostController) {
    val viewModel : SignUpViewModel = viewModel()
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        containerColor = Color.DarkGray,
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Text(text = "회원가입", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            }
                 }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "환영합니다!",
                modifier = Modifier.padding(10.dp),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(150.dp))
                    .background(Color.LightGray)
            ) {
                GlideImage(model = viewModel.profileImageUrl, contentDescription ="DefaultProfileImg" )
            }
            Column() {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(30.dp, 30.dp, 30.dp, 5.dp)
                        .fillMaxWidth(),
                    value = viewModel.textIpt,
                    onValueChange = { ipt ->
                        viewModel.textIpt = ipt
                    },
                    label = { Text(text = "닉네임을 입력하세요", color = Color(0xffD0BCFF)) },
                    singleLine = true,
                    isError = !viewModel.Nickname_isvalid,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFFD4CBE9),
                        focusedBorderColor = Color(0xffD0BCFF),
                        unfocusedBorderColor = Color(0xffD0BCFF),
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    keyboardActions = KeyboardActions(onDone = {
                        viewModel.IptisValid()
                        keyboardController?.hide()
                    }),
                    trailingIcon = {
                        if (viewModel.Nickname_isChecked){
                            Icon(imageVector = Icons.Sharp.Done, contentDescription = "done", tint = Color.Green)
                        }
                    }
                )
                if (!viewModel.Nickname_isvalid) {
                    Row(
                        modifier = Modifier
                            .padding(40.dp, 0.dp)
                            .fillMaxWidth(), horizontalArrangement = Arrangement.Start
                    )
                    {
                        Text(text = "중복된 아이디 입니다.", color = Color.Red)
                    }
                }
            }
            Button(
                onClick = { viewModel.handleSubmit(navController) },
                modifier = Modifier
                    .width(100.dp)
                    .padding(0.dp, 30.dp)
            ) {
                Text(
                    text = "가입",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
