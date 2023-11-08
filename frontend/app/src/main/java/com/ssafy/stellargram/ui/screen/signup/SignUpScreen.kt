package com.ssafy.stellargram.ui.screen.signup

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.stellargram.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SignUpScreen(){
    var textIpt by remember { mutableStateOf("")}
    var Nickname_isvalid by remember { mutableStateOf(true)}
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
                 },
        content = {
            Column(modifier = Modifier
                .padding(it)
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "환영합니다!",
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                Box(modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(150.dp))
                    .background(Color.LightGray)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.sunandcloud ),
                        contentDescription = "UserImage" ,
                        )
                }
                Column( ) {
                    OutlinedTextField(
                        modifier = Modifier.padding(30.dp,30.dp,30.dp,5.dp),
                        value = textIpt ,
                        onValueChange = { ipt ->
                            textIpt = ipt
                        },
                        label = {Text(text = "닉네임을 입력하세요", color = Color(0xffD0BCFF))},
                        singleLine = true,
                        isError = !Nickname_isvalid,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xffD0BCFF),
                            unfocusedBorderColor = Color(0xffD0BCFF),
                            focusedTextColor = Color(0xFFD4CBE9)
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        keyboardActions = KeyboardActions(onDone = {
//                            viewModel.submit(phoneNumberValue)
//                            keyboardController?.hide()
//                            navController.navigate(BottomNavItem.Main.screenRoute)
                        })
                    )
                    if (!Nickname_isvalid){
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp, 0.dp), horizontalArrangement = Arrangement.Start) {
                            Text(text = "중복된 아이디 입니다.", color = Color.Red)
                        }
                    }

                }
                Button(onClick = { /*TODO*/ },
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
    )
}
