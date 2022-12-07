package com.example.kotlin_mvvm_slackclone.data.models

import android.net.Uri

data class ChannelThread(
    val id:Int,
    val threadPostedByUserId:Int,
    val threadPostedDate:String,
    val channelId:Int,
    val reactionList: List<List<Int>>,
    val replyCount:Int,
    val mainThreadContent:String?="",
    val mainThreadImageURI: Uri?=null
    )
