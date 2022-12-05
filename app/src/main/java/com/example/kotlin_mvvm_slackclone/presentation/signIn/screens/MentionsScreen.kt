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
import androidx.compose.ui.unit.dp
import com.example.kotlin_mvvm_slackclone.common.AppbarLoggedIn

@Composable
fun MentionsScreen(){
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(modifier = Modifier,
        scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            AppbarLoggedIn(functionPerform = {

            })
            Text("Mentions Screen")
        }
    }
}