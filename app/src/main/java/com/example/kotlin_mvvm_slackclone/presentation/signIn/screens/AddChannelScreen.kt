package com.example.kotlin_mvvm_slackclone.presentation.signIn.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kotlin_mvvm_slackclone.common.AppbarLoggedIn
import com.example.kotlin_mvvm_slackclone.presentation.events.ChannelEvents
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.MainScreenViewModel
import com.example.kotlin_mvvm_slackclone.utils.PrefManager
import com.example.kotlin_mvvm_slackclone.presentation.events.UIEvent

@Composable
fun AddChannelScreen(
    mainViewModel:MainScreenViewModel= hiltViewModel()
){
    val activity = (LocalContext.current as? Activity)
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        mainViewModel.uiEvent.collect{value: UIEvent ->
            when(value){
                is UIEvent.ShowSnackBar->{
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = value.message,
                    )
                }
                else->{

                }
            }
        }
    }
    Scaffold(modifier = Modifier,
        scaffoldState = scaffoldState,
    topBar = {
        AppbarLoggedIn(showBackBtn=true,screenName="Create Channel", functionPerform = {
            activity?.finish()
        })
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {

            ChannelNameField(mainViewModel)
            VisibilityCheckBox(mainViewModel)
            CreateChannelBtn(mainViewModel)
        }
    }
}
@Composable
fun ChannelNameField(mainViewModel: MainScreenViewModel) {
    Text(text = "Channel Name", modifier = Modifier.padding(top = 20.dp, start = 10.dp),fontWeight = FontWeight.Bold, fontSize = 20.sp)
    OutlinedTextField(
        value =mainViewModel.channelNameField,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, end = 10.dp, start = 10.dp, bottom = 10.dp)
            .background(Color.Transparent),
        onValueChange = {
                        mainViewModel.onEvent(ChannelEvents.OnChannelNameChange(it))
        },
        colors= TextFieldDefaults.textFieldColors(
            backgroundColor= Color.Transparent,
            focusedIndicatorColor = Color.Black,
        ),
        textStyle = TextStyle(
            fontSize = 15.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        ),
        singleLine = true,
        placeholder = {
            Text(
                "e.g, #Testing, #Marketing",
                color = Color.Gray,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 4.dp),
                fontWeight = FontWeight.Bold
            )
        }
    )
}

@Composable
fun VisibilityCheckBox(mainViewModel: MainScreenViewModel) {
    Row(
        modifier = Modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            Text(text = "Make private", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = "When channel is private, it can only be\nviewed or joined by invite", color = Color.Gray)
        }
        // Creating a Switch, when value changes,
        // it updates mCheckedState value
        Switch(checked = mainViewModel.channelIsPrivate, onCheckedChange = {
            mainViewModel.onEvent(ChannelEvents.OnChannelVisibilityChange(isPrivate = it))
        })
    }
}

@Composable
fun CreateChannelBtn(mainViewModel: MainScreenViewModel) {
    val prefManager= PrefManager(LocalContext.current)
    Button(onClick = {
                     mainViewModel.onEvent(ChannelEvents.OnCreateChannel(
                         mainViewModel.channelNameField,
                         mainViewModel.channelIsPrivate,
                         prefManager.userGsonToObj(prefManager.loggedInUserValue).id,
                         prefManager.masterChannelGsonToObj(prefManager.masterChannelValue).id
                     ))
    },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
        , modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 10.dp, end = 10.dp)
    )
    {
        Text(text = "Create".uppercase(), color = Color.Black, modifier = Modifier.padding(10.dp), fontSize = 16.sp)
    }
}