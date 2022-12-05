package com.example.kotlin_mvvm_slackclone.presentation.signIn.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.kotlin_mvvm_slackclone.ChannelActivity
import com.example.kotlin_mvvm_slackclone.R
import com.example.kotlin_mvvm_slackclone.common.AppbarLoggedIn
import com.example.kotlin_mvvm_slackclone.common.SectionItemModel
import com.example.kotlin_mvvm_slackclone.data.MockData
import com.example.kotlin_mvvm_slackclone.data.models.SubChannel
import com.example.kotlin_mvvm_slackclone.data.models.User
import com.example.kotlin_mvvm_slackclone.presentation.events.ChannelEvents
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.MainScreenViewModel
import com.example.kotlin_mvvm_slackclone.ui.theme.SlackRed
import com.example.kotlin_mvvm_slackclone.utils.PrefManager
import com.example.kotlin_mvvm_slackclone.utils.Routes
import com.example.kotlin_mvvm_slackclone.utils.UIEvent

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
){
    val subChannelList =viewModel.subChannelsPublicList.collectAsState(initial = emptyList())
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { value: UIEvent ->
            when(value){
                is UIEvent.NavigateToNewIntent->{
                    if(value.intent==Routes.ADD_CHANNEL_ROUTE){
                        val channelIntent= Intent(context, ChannelActivity::class.java)
                        context.startActivity(channelIntent)
                    }
                }
                is UIEvent.NavigateToNewIntentWithId->{
                    if(value.intent==Routes.CHANNEL_DETAILS_ROUTE){
                        val channelIntent= Intent(context, ChannelActivity::class.java)
                        channelIntent.putExtra("channelId",value.id)
                        channelIntent.putExtra("route",Routes.CHANNEL_DETAILS_ROUTE)
                        context.startActivity(channelIntent)
                    }
                }
                is UIEvent.LoadDataAgain->{
                    if(value.load){
                        Log.d("loadER","lOAD DATA AGAIN")
                    }
                }
                else->Unit
            }
        }
    }
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    Scaffold(modifier = Modifier,
    scaffoldState = scaffoldState,
    topBar = {
        AppbarLoggedIn(functionPerform = {
        })
    }) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(it)
            .height(screenHeight)
            .verticalScroll(rememberScrollState())
        ) {

                    SearchBar()
                    MainSection(
                        listOf(
                            SectionItemModel("Threads",R.drawable.threads,2),
                            SectionItemModel("Drafts",R.drawable.drafts,0)
                        )
                    )
                    ChannelSection(
                        viewModel,
                        subChannelList.value
                    )
                    DirectMessagesSection()
                }
            }
}
@Composable
fun SearchBar(){
    OutlinedTextField(
        value = "",
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, end = 10.dp, start = 10.dp)
            .background(Color.Transparent),
        onValueChange = {
//                    vaultName = it
        },
        colors= TextFieldDefaults.textFieldColors(
            backgroundColor=Color.Transparent,
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
                "Jump to...",
                color = Color.Gray,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 4.dp),
                fontWeight = FontWeight.Bold
            )
        }
    )
}

@Composable
fun MainSection(
    sectionList:List<SectionItemModel>
){
    val indicatorWidth = 1.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
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
    ) {
        sectionList.iterator().forEach { sectionItem ->
           Row(modifier = Modifier.padding(end=10.dp, start = 10.dp)) {
               SectionItem(sectionItem)
           }
        }
    }
}
@Composable
fun DirectMessagesSection(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp)
        ) {
            Text(
                text = "Direct Messages",
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            )
        }
        MockData.usersList.iterator().forEach {

            Row(modifier = Modifier.padding(end=10.dp, start = 10.dp)) {
                DMSectionItem(user = it, notificationContent = 0 )
            }
        }

    }
}
@Composable
fun ChannelSection(
    viewModel: MainScreenViewModel,
    value: List<SubChannel>,
){
    val prefManager= PrefManager(LocalContext.current)
    val indicatorWidth = 1.dp
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
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
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(end = 10.dp)) {
            Text(
                text = "Channels",
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            )
            Image(
                painterResource(R.drawable.add),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(40.dp)
                    .clickable {
                        viewModel.navigateToAddChannelScreen(
                            prefManager.masterChannelGsonToObj(prefManager.masterChannelValue).id
                        )
                    },
                colorFilter = ColorFilter.tint(color = Color.Gray)
            )
        }

        value.iterator().forEach { sectionItem ->
            Row(modifier = Modifier.padding(end=10.dp, start = 10.dp)) {
               SectionItemForSubChannel(subChannel = sectionItem, notificationContent =0,viewModel)
            }
        }
   } 
}
@Composable
fun DMSectionItem(
    user: User,
    notificationContent:Int
){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clickable {

        }
    ) {
        Image(
            painter =  rememberAsyncImagePainter(user.imageURL),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .weight(0.2f)
                .clip(RoundedCornerShape(8.dp))

                .height(30.dp)
        )
        if(notificationContent>0) {
            Text(
                text = user.userName.toString(),
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(2f)
                    .padding(top = 5.dp, start = 5.dp),
                fontWeight = FontWeight.Bold
            )
        }else{
            Text(
                text =  user.userName.toString(),
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(2f)
                    .padding(top = 2.dp, start = 5.dp),

            )
        }
        if(notificationContent>0) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(SlackRed)
                    .border(
                        1.dp,
                        SlackRed,
                        shape = RoundedCornerShape(8.dp),
                    )

            ) {
                Text(
                    text = notificationContent.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.
                    padding(end=10.dp, start = 10.dp,top=1.dp, bottom = 1.dp)
                )
            }
        }
    }
}

@Composable
fun SectionItemForSubChannel(
    subChannel: SubChannel,
    notificationContent: Int,
    viewModel: MainScreenViewModel
){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clickable {
            viewModel.onEvent(ChannelEvents.ShowChannelDetails(subChannel.id))
        }
    ) {
        Image(
            painterResource(R.drawable.hashtag),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(20.dp)
                .padding(end = 10.dp),
            colorFilter = ColorFilter.tint(color = Color.Gray)
        )
        if(notificationContent>0) {
            Text(
                text = subChannel.channelName.toString(),
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(2f),
                fontWeight = FontWeight.Bold
            )
        }else{
            Text(
                text =  subChannel.channelName.toString(),
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(2f),
                fontWeight = FontWeight.Bold
            )
        }
        if(notificationContent>0) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(SlackRed)
                    .border(
                        1.dp,
                        SlackRed,
                        shape = RoundedCornerShape(8.dp),
                    )

            ) {
                Text(
                    text =notificationContent.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.
                    padding(end=10.dp, start = 10.dp,top=1.dp, bottom = 1.dp)
                )
            }
        }
    }
}
@Composable
fun SectionItem(
    sectionItem: SectionItemModel,
){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .clickable {

        }
    ) {
        Image(
            painterResource(R.drawable.hashtag),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(20.dp)
                .padding(end = 10.dp),
            colorFilter = ColorFilter.tint(color = Color.Gray)
        )
        if(sectionItem.sectionNotificationCount>0) {
            Text(
                text = sectionItem.sectionName,
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(2f),
                fontWeight = FontWeight.Bold
            )
        }else{
            Text(
                text = sectionItem.sectionName,
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(2f),
                fontWeight = FontWeight.Bold
            )
        }
       if(sectionItem.sectionNotificationCount>0) {
           Box(
               modifier = Modifier
                   .clip(RoundedCornerShape(8.dp))
                   .background(SlackRed)
                   .border(
                       1.dp,
                       SlackRed,
                       shape = RoundedCornerShape(8.dp),
                   )

           ) {
               Text(
                   text =sectionItem.sectionNotificationCount.toString(),
                   color = Color.White,
                   fontSize = 12.sp,
                   fontWeight = FontWeight.Bold,
                   textAlign = TextAlign.End,
                   modifier = Modifier.
                   padding(end=10.dp, start = 10.dp,top=1.dp, bottom = 1.dp)
               )
           }
       }
    }
}