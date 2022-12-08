@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.example.kotlin_mvvm_slackclone.presentation.signIn.screens

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.example.kotlin_mvvm_slackclone.R
import com.example.kotlin_mvvm_slackclone.common.AppbarChannelInside
import com.example.kotlin_mvvm_slackclone.data.models.ChannelThread
import com.example.kotlin_mvvm_slackclone.data.models.SubChannel
import com.example.kotlin_mvvm_slackclone.data.models.User
import com.example.kotlin_mvvm_slackclone.presentation.events.AuthEvents
import com.example.kotlin_mvvm_slackclone.presentation.events.ChannelEvents
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.AuthViewModel
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.ChannelDetailsViewModel
import com.example.kotlin_mvvm_slackclone.ui.theme.*
import com.example.kotlin_mvvm_slackclone.utils.PrefManager
import com.example.kotlin_mvvm_slackclone.presentation.events.UIEvent
import java.util.*

@Suppress("UNNECESSARY_SAFE_CALL")
@Composable
fun ChannelDetailsScreen(
    channelId: Int,
    viewModel: ChannelDetailsViewModel = hiltViewModel(),
    onNavigate: (UIEvent.Navigate) -> Unit,
    ) {
    val context = LocalContext.current
    val activity = (context as? Activity)
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val subChannel = viewModel.subChannel?.collectAsState(initial = null)
    var channelThreadsList = viewModel.channelThreads?.collectAsState(initial = emptyList())
    Log.d("channelThreadsList", channelThreadsList?.value?.size.toString())
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    LaunchedEffect(key1 = true) {
        Log.d("launched", "TIME 1")
        viewModel.onEvent(ChannelEvents.ShowChannelDetails(channelId))
        viewModel.uiEvent.collect { value: UIEvent ->
            when (value) {
                is UIEvent.Navigate->{
                    onNavigate(value)
                }
                is UIEvent.LoadDataAgain->{
                    Log.d("UiEvent", "load again")
                    Log.d("channelThreadsList", channelThreadsList?.value.toString())
                }
                else -> {

                }
            }
        }
    }
    Scaffold(
        modifier = Modifier,
        scaffoldState = scaffoldState,
        topBar = {
            when {
                subChannel?.value != null ->
                    AppbarChannelInside(
                        showBackBtn = true,
                        screenName = "# ${subChannel.value?.channelName.toString()}",
                        subTitle = "${subChannel.value?.channelMembersId?.size} Members",
                        functionPerform = {
                            activity?.finish()
                        },
                        viewModel = viewModel,
                        functionPerform2 = {
                            viewModel.onEvent(ChannelEvents.ShowSearchBar(true))
                        },
                        painterResource(R.drawable.filter),
                        )
                else ->
                    AppbarChannelInside(
                        showBackBtn = true,
                        screenName = "Loading...",
                        subTitle = "Loading...",
                        functionPerform = {
                            activity?.finish()
                        },
                        viewModel = viewModel,
                        functionPerform2 = {
                            viewModel.onEvent(ChannelEvents.ShowSearchBar(true))
                        },
                        painterResource(R.drawable.filter),
                        )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(ChannelEvents.AddChannelThread(channelId = channelId))
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

            when {
                channelThreadsList.value.isNotEmpty() ->{
                    MainThreadsSection(channelThreadsList.value, screenHeight, viewModel,subChannel)
                }
                else ->
                    Text(text = "Loading...")
            }
        }
    }
}

@Composable
fun MainThreadsSection(
    value: List<ChannelThread>,
    screenHeight: Dp,
    viewModel: ChannelDetailsViewModel,
    subChannel: State<SubChannel?>?
) {
    Log.d("channelThreadListSize", value.size.toString())
    val indicatorWidth = 1.dp
    Box(
        modifier = Modifier
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
        LazyColumn {
            items(value.reversed()) { item ->
                MainThreadSectionListItem(item, viewModel, functionPerform = {
                    Log.d("ThreadItem", item.id.toString())
                    viewModel.onEvent(ChannelEvents.ThreadDetails(item.id,subChannel?.value?.channelName.toString()))
                })
            }
        }

    }
}

@Composable
fun MainThreadSectionListItem(
    item: ChannelThread,
    channelDetailsViewModel: ChannelDetailsViewModel,
    functionPerform: () -> Unit,
) {
    val userViewModel: AuthViewModel = hiltViewModel()
    userViewModel.onEvent(AuthEvents.GetUserDetailsById(item.threadPostedByUserId))
    val user = userViewModel.user?.collectAsState(initial = null)
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            functionPerform()
        }) {
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
        ReplyArea(item.replyCount)

    }
}

@Composable
fun ReplyArea(replyCount: Int) {
    when {
        replyCount > 0 ->
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    .fillMaxWidth()
                    .border(
                        border = ButtonDefaults.outlinedBorder,
                        shape = RoundedCornerShape(4.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(modifier = Modifier.padding(end = 10.dp, start = 10.dp)) {
                    Row {
                        listOf("Amin Tahseen", "Ahmed Ali", "Tauheed Khan").map {
                            Image(
                                painter = rememberAsyncImagePainter(model = "https://ui-avatars.com/api/?name=$it&background=fdcb6e&color=000"),
                                contentDescription = "",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .height(50.dp)
                                    .padding(start = 5.dp),
                            )
                        }
                    }
                }
                Text(text = "$replyCount Replies", color = Color.Gray, fontWeight = FontWeight.Bold)
            }
    }

}

@Composable
fun ReactionArea(
    item: ChannelThread,
    channelDetailsViewModel: ChannelDetailsViewModel
) {
    val prefManager = PrefManager(LocalContext.current)
    val loggedInUserId = prefManager.userGsonToObj(prefManager.loggedInUserValue).id
    val reactionList = item.reactionList
    val refresh=channelDetailsViewModel.refreshReactions.collectAsState()

    Row(Modifier.fillMaxWidth()) {
        if(refresh.value){
            Log.d("updateList","update")
            val value =channelDetailsViewModel.checkIfReactionExists(
                reactionList[0], loggedInUserId)
            Log.d("updateFOUND","$value")

        }
        ReactionItem(
            painterResource(R.drawable.like_reaction),
            if (reactionList.isNotEmpty() && channelDetailsViewModel.checkIfReactionExists(
                    reactionList[0], loggedInUserId)) SlackBlue else Color.Gray,
            if (0 in 0..reactionList.lastIndex) reactionList[0].size.toString() else "0",
            functionPerform = {
                channelDetailsViewModel.onEvent(
                    ChannelEvents.UpdateThreadReaction(
                        item,
                        0,
                        loggedInUserId
                    )
                )
            }
        )
        ReactionItem(
            painterResource(R.drawable.love_reaction),
            if (reactionList.isNotEmpty() && channelDetailsViewModel.checkIfReactionExists(
                    reactionList[1], loggedInUserId)) SlackRed else Color.Gray,
            if (reactionList.isNotEmpty() && (1 in 0..reactionList.lastIndex)) reactionList[1].size.toString() else "0",
            functionPerform = {
                channelDetailsViewModel.onEvent(
                    ChannelEvents.UpdateThreadReaction(
                        item,
                        1,
                        loggedInUserId
                    )
                )
            }

        )
        ReactionItem(
            painterResource(R.drawable.celebrate_reaction),
            if (reactionList.isNotEmpty() && channelDetailsViewModel.checkIfReactionExists(
                    reactionList[2], loggedInUserId)) SlackYellow else Color.Gray,
            if (reactionList.isNotEmpty() && (2 in 0..reactionList.lastIndex)) reactionList[2].size.toString() else "0",
            functionPerform = {
                channelDetailsViewModel.onEvent(
                    ChannelEvents.UpdateThreadReaction(
                        item,
                        2,
                        loggedInUserId
                    )
                )
            }

        )

    }
}

@Composable
fun ReactionItem(
    painterResource: Painter,
    color: Color,
    value: String = "0",
    functionPerform: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 10.dp)
    ) {
        Image(
            painterResource,
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(50.dp)
                .padding(end = 10.dp)
                .clickable {
                    functionPerform()
                },
            colorFilter = ColorFilter.tint(color)

        )
        if (value != "0") Text(text = value, color = Color.Black)
    }
}

@Composable
fun ThreadUserDetails(threadPostedByUser: User?, threadPostedDate: String) {
    Row {
        threadPostedByUser?.userName?.split(" ")?.let { it1 ->
            Text(text = it1.joinToString(" ") { it ->
                it.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
            }, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Text(
            text = threadPostedDate,
            modifier = Modifier.padding(start = 10.dp),
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ThreadMainContent(
    mainThreadContent: String?,
    mainThreadImageURI: Uri?,
    channelDetailsViewModel: ChannelDetailsViewModel
) {
    Column(Modifier.fillMaxWidth()) {
        when {
            mainThreadContent != null ->
                Text(text = mainThreadContent)
        }
        when {
            mainThreadImageURI != null ->
                Image(
                    painter = rememberAsyncImagePainter(
                        channelDetailsViewModel.getVaultImageFromLocal(
                            LocalContext.current,
                            mainThreadImageURI
                        )
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(300.dp)
                        .padding(top = 5.dp)
                )
        }
    }
}