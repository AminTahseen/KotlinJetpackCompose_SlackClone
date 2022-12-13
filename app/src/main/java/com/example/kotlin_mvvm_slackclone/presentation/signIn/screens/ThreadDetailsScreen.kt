package com.example.kotlin_mvvm_slackclone.presentation.signIn.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.kotlin_mvvm_slackclone.R
import com.example.kotlin_mvvm_slackclone.common.AppbarChannelInside
import com.example.kotlin_mvvm_slackclone.data.MockData
import com.example.kotlin_mvvm_slackclone.data.models.ChannelThread
import com.example.kotlin_mvvm_slackclone.data.models.User
import com.example.kotlin_mvvm_slackclone.presentation.events.AuthEvents
import com.example.kotlin_mvvm_slackclone.presentation.events.ChannelEvents
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.AuthViewModel
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.ChannelDetailsViewModel
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.ThreadDetailsViewModel
import com.example.kotlin_mvvm_slackclone.ui.theme.SlackRed

@Composable
fun ThreadDetailsScreen(
    viewModel: ThreadDetailsViewModel = hiltViewModel(),
    channelDetailsViewModel: ChannelDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth =configuration.screenWidthDp.dp
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(modifier = Modifier, scaffoldState = scaffoldState, topBar = {
        AppbarChannelInside(
            showBackBtn = true,
            screenName = "Thread",
            subTitle = "Post in #${viewModel.foundChannelName}",
            functionPerform = onNavigateBack,
            viewModel = channelDetailsViewModel,
            null,
            null,
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight)
                .padding(it)
        ) {

            viewModel.thread?.let { it1 ->
                ThreadDetails(item =
                it1, channelDetailsViewModel =  channelDetailsViewModel,screenHeight)
            }
            ReplyField(screenHeight,screenWidth)
        }
    }
}
@Composable
fun ThreadDetails(
    item: ChannelThread,
    channelDetailsViewModel: ChannelDetailsViewModel,
    screenHeight: Dp,
) {
    val userViewModel: AuthViewModel = hiltViewModel()
    userViewModel.onEvent(AuthEvents.GetUserDetailsById(item.threadPostedByUserId))
    val user = userViewModel.user?.collectAsState(initial = null)
    Column(modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .height(screenHeight * 0.7f)
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://ui-avatars.com/api/?name=${user?.value?.userName}&color=000"),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .weight(0.2f)
                    .clip(RoundedCornerShape(20.dp))
                    .height(40.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                ThreadUserDetails(user?.value, item.threadPostedDate)
                ThreadMainContent(
                    item.mainThreadContent,
                    item.mainThreadImageURI,
                    channelDetailsViewModel
                )
                ReactionArea(item, channelDetailsViewModel)
            }
        }
        RepliesList(channelDetailsViewModel)

    }
}
@Composable
fun RepliesList(channelDetailsViewModel: ChannelDetailsViewModel){
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .border(
                    border = ButtonDefaults.outlinedBorder,
                    shape = RoundedCornerShape(4.dp)
                ),
        ) {
            Text(text = "3 Replies", modifier = Modifier.padding(10.dp))
        }
        MockData.usersList.map{
            ReplyItem(user = it, channelDetailsViewModel = channelDetailsViewModel)
        }
    }
}
@Composable
fun ReplyItem(user: User?,channelDetailsViewModel:ChannelDetailsViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(user?.imageURL),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .weight(0.2f)
                .clip(RoundedCornerShape(20.dp))
                .height(40.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            ThreadUserDetails(user, "11 Sept 12:30 PM")
            ThreadMainContent(
                "This is a thread reply",
                null,
                channelDetailsViewModel
            )
        }
    }
}
@Composable
fun ReplyField(screenHeight: Dp, screenWidth: Dp) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(screenHeight*0.3f)
        .border(
            border = ButtonDefaults.outlinedBorder,
            shape = RoundedCornerShape(4.dp)
        ),
    ) {
        TextField(value = "", modifier = Modifier
            .padding(
                start = 5.dp, end = 5.dp
            )
            .fillMaxWidth()
            .height(screenHeight*0.2f),
            onValueChange = {


        }, colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ), textStyle = TextStyle(
            fontSize = 16.sp,
            color = Color.Black,
        ), placeholder = {
            Text(
                "Write a reply",
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
            )
        })
        ReplyOptionsRow(screenHeight,screenWidth)

    }
}
@Composable
fun ReplyOptionsRow(screenHeight: Dp,screenWidth:Dp){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(screenHeight*0.1f)
        .background(com.example.kotlin_mvvm_slackclone.ui.theme.SearchBar)
    ){
        Row(
            modifier = Modifier.width(screenWidth * 0.8f)
        ) {
            OptionItem(painterResource(R.drawable.add_image), Color.DarkGray) {

            }
            OptionItem(painterResource(R.drawable.emoji), Color.DarkGray) {

            }
            OptionItem(painterResource(R.drawable.mentions_grey), Color.DarkGray) {}

        }
        Button(
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(backgroundColor = SlackRed),
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            Text(text = "Reply".uppercase(), color = Color.White)
        }
    }

}