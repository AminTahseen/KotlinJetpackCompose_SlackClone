@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.example.kotlin_mvvm_slackclone.presentation.signIn.screens

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.kotlin_mvvm_slackclone.ChannelActivity
import com.example.kotlin_mvvm_slackclone.R
import com.example.kotlin_mvvm_slackclone.common.AppbarChannelInside
import com.example.kotlin_mvvm_slackclone.data.models.ChannelThread
import com.example.kotlin_mvvm_slackclone.data.models.User
import com.example.kotlin_mvvm_slackclone.presentation.events.AuthEvents
import com.example.kotlin_mvvm_slackclone.presentation.events.ChannelEvents
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.AuthViewModel
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.ChannelDetailsViewModel
import com.example.kotlin_mvvm_slackclone.ui.theme.*
import com.example.kotlin_mvvm_slackclone.utils.Routes
import com.example.kotlin_mvvm_slackclone.utils.UIEvent
import java.util.*

@Composable
fun ChannelDetailsScreen(
    channelId:Int,
    viewModel: ChannelDetailsViewModel = hiltViewModel(),
){
    val context = LocalContext.current
    val activity = (context as? Activity)
    viewModel.onEvent(ChannelEvents.ShowChannelDetails(channelId))
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val subChannel=viewModel.subChannel?.collectAsState(initial = null)
    val channelThreadsList = viewModel.channelThreads?.collectAsState(initial = emptyList())
    Log.d("channelThreadsList",channelThreadsList?.value?.size.toString())
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect{value:UIEvent->
            when(value){
                is UIEvent.NavigateToNewIntentWithId->{
                    if(value.intent== Routes.ADD_CHANNEL_POST_ROUTE){
                        val channelIntent= Intent(context, ChannelActivity::class.java)
                        channelIntent.putExtra("channelId",value.id)
                        channelIntent.putExtra("route",Routes.ADD_CHANNEL_POST_ROUTE)
                        context.startActivity(channelIntent)
                    }
                }
                else->{

                }
            }
        }
    }
    Scaffold(
        modifier = Modifier,
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                          viewModel.onEvent(ChannelEvents.AddChannelThread(channelId=channelId))
                },
                backgroundColor = SlackPurple,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.create),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            )
        },
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {

            channelThreadsList?.value?.let { it -> MainThreadsSection(it,viewModel,screenHeight) }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainThreadsSection(
    value: List<ChannelThread>,
    viewModel: ChannelDetailsViewModel,
    screenHeight: Dp
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val pullRefreshState= rememberPullRefreshState(refreshing = isLoading, onRefresh = {viewModel.loadStuff()})
    val indicatorWidth = 1.dp
    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxWidth()
            .height(screenHeight)
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
    )
            {
                when{
                    value.isNotEmpty() ->{
                        LazyColumn {
                            items(value){ item->
                                MainThreadSectionListItem(item)
                            }
                        }
                    }
                    else->
                        Text(text = "Loading...", modifier = Modifier.padding(10.dp))
                }
             //   PullRefreshIndicator(isLoading, pullRefreshState, Modifier.align(Alignment.TopCenter))
            }
}
@Composable
fun MainThreadSectionListItem(item: ChannelThread,userViewModel: AuthViewModel= hiltViewModel()) {
   userViewModel.onEvent(AuthEvents.GetUserDetailsById(item.threadPostedByUserId))
    val user=userViewModel.user?.collectAsState(initial = null)
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp)
            ){
            Image(
                painter =  rememberAsyncImagePainter("https://ui-avatars.com/api/?name=${user?.value?.userName}&color=000"),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .weight(0.2f)
                    .clip(RoundedCornerShape(20.dp))
                    .height(40.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                ThreadUserDetails(user?.value,item.threadPostedDate)
                ThreadMainContent(item.mainThreadContent,item.mainThreadImageURI)
                ReactionArea(item.reactionList)
            }
        }
        ReplyArea(item.replyCount)

    }
}
@Composable
fun ReplyArea(replyCount: Int) {
    when{
        replyCount>0->
            Row(modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                .fillMaxWidth()
                .border(
                    border = ButtonDefaults.outlinedBorder,
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable {

                },
                verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = (painterResource(R.drawable.reply)),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(50.dp)
                        .padding(start = 10.dp, end = 10.dp),
                    colorFilter = ColorFilter.tint(Color.Gray)
                )
                Text(text = "$replyCount Replies", color = Color.Gray, fontWeight = FontWeight.Bold)
            }
    }

}
@Composable
fun ReactionArea(reactionList: List<Int>) {
    Row(Modifier.fillMaxWidth()) {
        ReactionItem(painterResource(R.drawable.like_reaction),SlackBlue,reactionList[0].toString())
        ReactionItem(painterResource(R.drawable.love_reaction), SlackRed,reactionList[1].toString())
        ReactionItem(painterResource(R.drawable.celebrate_reaction), SlackYellow,reactionList[2].toString())
        ReactionItem(painterResource(R.drawable.add_reaction), Color.Gray)
    }
}
@Composable
fun ReactionItem(painterResource: Painter, color: Color,value:String="0") {
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
        if (value!="0") Text(text = value, color = Color.Black)
    }
}
@Composable
fun ThreadUserDetails(threadPostedByUser: User?, threadPostedDate: String) {
    Row{
        threadPostedByUser?.userName?.split(" ")?.let { it1 ->
            Text(text = it1.joinToString(" ") { it ->
                it.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            } }, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Text(text = threadPostedDate, modifier = Modifier.padding(start = 10.dp),fontSize = 16.sp,color = Color.Gray)
    }
}
@Composable
fun ThreadMainContent(mainThreadContent: String, mainThreadImageURI: String) {
    Column(Modifier.fillMaxWidth()) {
        Text(text = mainThreadContent)
        when{
            mainThreadImageURI.isNotEmpty() ->
                Image(
                    painter = rememberAsyncImagePainter(mainThreadImageURI),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(300.dp)
                        .padding(top = 5.dp)
                )

        }

    }
}