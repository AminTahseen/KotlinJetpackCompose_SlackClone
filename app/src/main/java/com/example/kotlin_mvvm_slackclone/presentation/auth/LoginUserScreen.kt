package com.example.kotlin_mvvm_slackclone.presentation.auth

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kotlin_mvvm_slackclone.LoggedInMainActivity
import com.example.kotlin_mvvm_slackclone.common.Appbar
import com.example.kotlin_mvvm_slackclone.presentation.events.AuthEvents
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.AuthViewModel
import com.example.kotlin_mvvm_slackclone.ui.theme.SlackPurple
import com.example.kotlin_mvvm_slackclone.ui.theme.SlackRed
import com.example.kotlin_mvvm_slackclone.utils.Routes
import com.example.kotlin_mvvm_slackclone.utils.UIEvent

@Composable
fun LoginUserScreen(
    viewModel: AuthViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { value: UIEvent ->
            when(value){
                is UIEvent.ShowSnackBar->{
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = value.message,
                    )
                }
                is UIEvent.NavigateToNewIntent->{
                    if(value.intent==Routes.LOGGED_IN_INTENT){
                        (context as Activity).finish()
                        val loggedInIntent= Intent(context, LoggedInMainActivity::class.java)
                        context.startActivity(loggedInIntent)
                    }
                }
                else->Unit
            }
        }
    }
    Scaffold(modifier = Modifier,scaffoldState=scaffoldState) {
        Column(
            modifier = Modifier
                .background(SlackPurple)
                .fillMaxWidth()
                .padding(it)
                .fillMaxHeight(),
        ) {

            Appbar()
            Column( modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                , horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Please verify yourself", fontSize = 20.sp,
                    color = Color.Gray
                )
                
                TextField("someone@email.com",viewModel,"email")
              //  Spacer(modifier = Modifier.height(10.dp))
                TextField("Password",viewModel,"password")
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Your about to join", fontSize = 17.sp,
                    color = Color.Gray
                )
                Row() {

                    Text(
                        text = "${viewModel.foundChannelName}.slack.com", fontSize = 17.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                }
            }
            Button(onClick = {
                             viewModel.onEvent(
                                 AuthEvents.VerifySignIn(
                                 email = viewModel.emailField,
                                 pass = viewModel.passwordField
                                 )
                             )
            },
                colors = ButtonDefaults.buttonColors(backgroundColor = SlackRed)
                , modifier = Modifier
                    .fillMaxWidth()
            )
            {
                Text(text = "Verify Me".uppercase(), color = Color.White, modifier = Modifier.padding(10.dp), fontSize = 16.sp)
            }
        }
    }
}
@Composable
fun TextField(fieldFor:String, viewModel: AuthViewModel, fieldForState:String){
    val indicatorWidth = 1.dp
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 30.dp, end = 30.dp)
        .drawBehind {
            val strokeWidth = indicatorWidth.value * density
            val y = size.height - strokeWidth / 2

            drawLine(
                Color.LightGray,
                Offset(0f, y),
                Offset(size.width, y),
                strokeWidth
            )
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
     if (fieldForState=="email"){
         TextField(
             value =viewModel.emailField,
             modifier = Modifier
                 .padding(
                     start = 10.dp
                 )
                 .fillMaxWidth(),
             onValueChange = {
                 viewModel.onEvent(AuthEvents.OnEmailChange(it))
             },
             colors= TextFieldDefaults.textFieldColors(
                 backgroundColor= Color.Transparent,
                 focusedIndicatorColor = Color.Transparent,
                 unfocusedIndicatorColor = Color.Transparent
             ),
             textStyle = TextStyle(
                 fontSize = 16.sp,
                 color = Color.White ,
                 textAlign = TextAlign.Center
             ),
             singleLine = true,
             placeholder = {
                 Text(fieldFor,
                     color = Color(0xFF7B59A8),
                     fontSize = 16.sp,
                     modifier = Modifier
                         . fillMaxWidth(),
                     textAlign = TextAlign.Center,
                 )
             }
         )
     }else{
         TextField(
             value =viewModel.passwordField,
             modifier = Modifier
                 .padding(
                     start = 10.dp
                 )
                 .fillMaxWidth(),
             onValueChange = {
                 viewModel.onEvent(AuthEvents.OnPasswordChange(it))
             },
             colors= TextFieldDefaults.textFieldColors(
                 backgroundColor= Color.Transparent,
                 focusedIndicatorColor = Color.Transparent,
                 unfocusedIndicatorColor = Color.Transparent
             ),
             textStyle = TextStyle(
                 fontSize = 16.sp,
                 color = Color.White ,
                 textAlign = TextAlign.Center
             ),
             singleLine = true,
             placeholder = {
                 Text(fieldFor,
                     color = Color(0xFF7B59A8),
                     fontSize = 16.sp,
                     modifier = Modifier
                         . fillMaxWidth(),
                     textAlign = TextAlign.Center,
                 )
             }
         )
     }

    }
}