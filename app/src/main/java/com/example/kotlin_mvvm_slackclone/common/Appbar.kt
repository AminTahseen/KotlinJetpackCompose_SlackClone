package com.example.kotlin_mvvm_slackclone.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlin_mvvm_slackclone.R
import com.example.kotlin_mvvm_slackclone.presentation.events.ChannelEvents
import com.example.kotlin_mvvm_slackclone.presentation.viewmodels.ChannelDetailsViewModel
import com.example.kotlin_mvvm_slackclone.ui.theme.SlackPurpleDark
import com.example.kotlin_mvvm_slackclone.utils.PrefManager

@Composable
fun Appbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SlackPurpleDark)
    ) {
        Image(
            painterResource(R.drawable.mainlogo),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(50.dp)
                .padding(10.dp)
        )

    }
}

@Composable
fun AppbarLoggedIn(
    showBackBtn: Boolean = false,
    screenName: String = "",
    functionPerform: () -> Unit

) {
    val prefManager = PrefManager(LocalContext.current)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SlackPurpleDark)
    ) {
        Image(
            if (!showBackBtn)
                painterResource(R.drawable.slack_only_icon)
            else
                painterResource(R.drawable.ic_baseline_arrow_back_24),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(50.dp)
                .padding(10.dp)
                .clickable {
                    functionPerform()
                }
        )
        Text(
            text =
            if (screenName === "")
                prefManager.masterChannelGsonToObj(prefManager.masterChannelValue).channelName.toString()
            else
                screenName,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(10.dp)
                .weight(1f),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Image(
            painterResource(R.drawable.settings),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(50.dp)
                .padding(10.dp)
        )
    }
}

@Composable
fun AppbarChannelInside(
    showBackBtn: Boolean = false,
    screenName: String = "",
    subTitle: String = "",
    functionPerform: () -> Unit,
    viewModel: ChannelDetailsViewModel,
    functionPerform2: (() -> Unit)?,
    painterResource: Painter?
) {
    val showSearchBar by viewModel.showSearchBar.collectAsState()
    Log.d("showSearchBar", showSearchBar.toString())
    val prefManager = PrefManager(LocalContext.current)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SlackPurpleDark),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            if (!showBackBtn)
                painterResource(R.drawable.slack_only_icon)
            else
                painterResource(R.drawable.ic_baseline_arrow_back_24),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(50.dp)
                .padding(10.dp)
                .clickable {
                    when {
                        showSearchBar -> {
                            viewModel.onEvent(ChannelEvents.ShowSearchBar(false))
                        }
                        else -> {
                            functionPerform()
                        }
                    }
                }
        )
        when {
            showSearchBar -> {
                SearchBar(placeholder = "Search for threads...")
            }
            else -> {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f),
                    horizontalAlignment = if(painterResource != null) Alignment.CenterHorizontally else Alignment.Start
                ) {
                    Text(
                        text =
                        if (screenName === "")
                            prefManager.masterChannelGsonToObj(prefManager.masterChannelValue).channelName.toString()
                        else
                            screenName,
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(text = subTitle, color = Color.LightGray)
                }
                if (painterResource != null) {
                    Image(
                        painterResource,
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(50.dp)
                            .padding(10.dp)
                            .clickable {
                                if (functionPerform2 != null) {
                                    functionPerform2()
                                }
                            }
                    )
                }
            }
        }
    }
}