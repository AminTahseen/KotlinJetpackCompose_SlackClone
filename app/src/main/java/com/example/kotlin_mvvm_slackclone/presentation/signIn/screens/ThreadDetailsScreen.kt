package com.example.kotlin_mvvm_slackclone.presentation.signIn.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kotlin_mvvm_slackclone.R
import com.example.kotlin_mvvm_slackclone.common.AppbarChannelInside
import com.example.kotlin_mvvm_slackclone.presentation.events.UIEvent
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.ChannelDetailsViewModel
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.ThreadDetailsViewModel

@Composable
fun ThreadDetailsScreen(
    viewModel: ThreadDetailsViewModel = hiltViewModel(),
    channelDetailsViewModel: ChannelDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
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
                .height(600.dp)
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {

            Text("Thread Details Screen")
        }
    }
}