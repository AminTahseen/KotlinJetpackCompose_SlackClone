package com.example.kotlin_mvvm_slackclone.presentation.signIn.screens

import android.app.Activity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kotlin_mvvm_slackclone.R
import com.example.kotlin_mvvm_slackclone.common.AppbarChannelInside
import com.example.kotlin_mvvm_slackclone.presentation.events.ChannelEvents
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.ChannelDetailsViewModel
import com.example.kotlin_mvvm_slackclone.ui.theme.SlackRed

@Composable
fun AddThreadPostScreen(
    channelId: Int,
    viewModel: ChannelDetailsViewModel = hiltViewModel(),
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth=configuration.screenWidthDp.dp
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val activity = (context as? Activity)
    viewModel.onEvent(ChannelEvents.ShowChannelDetails(channelId))
    val subChannel=viewModel.subChannel?.collectAsState(initial = null)
    Scaffold(modifier = Modifier,
        scaffoldState = scaffoldState,
        topBar = {
            when{
                subChannel?.value!=null->
                    AppbarChannelInside(
                        showBackBtn=true,
                        screenName="# ${subChannel.value?.channelName.toString()}",
                        subTitle = "${subChannel.value?.channelMembersId?.size} Members",
                        functionPerform = {
                            activity?.finish()
                        },
                        viewModel=viewModel,
                        filterPerform={
                            viewModel.onEvent(ChannelEvents.ShowSearchBar(true))
                        }
                    )
                else->
                    AppbarChannelInside(
                        showBackBtn=true,
                        screenName="Loading...",
                        subTitle = "Loading...",
                        functionPerform = {
                            activity?.finish()
                        },
                        viewModel=viewModel,
                        filterPerform={
                            viewModel.onEvent(ChannelEvents.ShowSearchBar(true))
                        }
                    )
            }
        }
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight)
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            TextField(
                value = "",
                modifier = Modifier
                    .padding(
                        start = 5.dp,
                        end = 5.dp
                    )
                    .fillMaxWidth()
                    .height(screenHeight * 0.81f),
                onValueChange = {
                },
                colors= TextFieldDefaults.textFieldColors(
                    backgroundColor= Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.White,
                ),
                placeholder = {
                    Text("What's on your mind ?",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier
                            . fillMaxWidth(),
                    )
                }
            )
            PostOptions(screenHeight,screenWidth)
        }
    }
}
@Composable
fun PostOptions(screenHeight: Dp, screenWidth: Dp) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top=10.dp)
        .height(screenHeight * 0.1f)
        .background(com.example.kotlin_mvvm_slackclone.ui.theme.SearchBar)) {
       Row(
           modifier = Modifier.width(screenWidth*0.8f)
       ) {
           OptionItem(painterResource(R.drawable.add_image), Color.DarkGray)
           OptionItem(painterResource(R.drawable.emoji), Color.DarkGray)
           OptionItem(painterResource(R.drawable.mentions_grey), Color.DarkGray)
       }
        Button(onClick = {
        },
            colors = ButtonDefaults.buttonColors(backgroundColor = SlackRed)
            , modifier = Modifier.padding(bottom = 10.dp)
        )
        {
            Text(text = "post".uppercase(), color = Color.White)
        }
    }
}
@Composable
fun OptionItem(painterResource: Painter, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 10.dp)) {
        Image(
            painterResource,
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(50.dp)
                .padding(end = 10.dp)
                .clickable {

                },
            colorFilter = ColorFilter.tint(color)

        )
    }
}