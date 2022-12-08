package com.example.kotlin_mvvm_slackclone.presentation.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kotlin_mvvm_slackclone.common.Appbar
import com.example.kotlin_mvvm_slackclone.presentation.events.AuthEvents
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.AuthViewModel
import com.example.kotlin_mvvm_slackclone.ui.theme.SlackPurple
import com.example.kotlin_mvvm_slackclone.ui.theme.SlackRed
import com.example.kotlin_mvvm_slackclone.presentation.events.UIEvent


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
){
    val scaffoldState = rememberScaffoldState()
    val indicatorWidth = 1.dp
    LaunchedEffect(key1 = true) {
       viewModel.uiEvent.collect { value: UIEvent ->
           when(value){
               is UIEvent.ShowSnackBar->{
                   scaffoldState.snackbarHostState.showSnackbar(
                       message = value.message,
                   )
               }
               is UIEvent.Navigate->onNavigate(value)
               else->Unit
           }
       }
    }
    Scaffold(
        modifier = Modifier,
        scaffoldState = scaffoldState,
    ) {
        Column(
            modifier = Modifier
                .background(SlackPurple)
                .fillMaxWidth()
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
                    text = "Sign in to Slack", fontSize = 20.sp,
                    color = Color.Gray
                )
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
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
                    TextField(
                        value = viewModel.channelNameField,
                        modifier = Modifier
                            .padding(
                                start = 10.dp
                            )
                            .weight(1f),
                       onValueChange = {
                           viewModel.onEvent(AuthEvents.OnChannelNameChange(it))
                        },
                        colors=TextFieldDefaults.textFieldColors(
                            backgroundColor=Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            fontSize = 30.sp,
                            color = Color.White ,
                            textAlign = TextAlign.End
                        ),
                        singleLine = true,
                        placeholder = {
                            Text("yourTeam",
                                color = Color(0xFF7B59A8),
                                fontSize = 30.sp,
                                modifier = Modifier
                                        . fillMaxWidth(),
                                 textAlign = TextAlign.End,
                            )
                        }
                    )
                    Text(
                        text = ".slack.com", fontSize = 30.sp,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Forgot your team's domain ?", fontSize = 17.sp,
                    color = Color.Gray
                )
                Row {
                    Text(
                        text = "Get a reminder", fontSize = 17.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Outlined.ArrowForward,
                        contentDescription = "Icon",
                        modifier = Modifier.size(20.dp).padding(top=5.dp),
                        tint = Color.White,
                        )
                }

            }
            Button(onClick = {
                             viewModel.onEvent(
                                 AuthEvents.FindTeamDomain(viewModel.channelNameField)
                             )
                },
                    colors = ButtonDefaults.buttonColors(backgroundColor = SlackRed)
                    , modifier = Modifier
                        .fillMaxWidth()
                        )
            {
                Text(text = "Find Team".uppercase(), color = Color.White, modifier = Modifier.padding(10.dp), fontSize = 16.sp)
            }
        }
    }
}
