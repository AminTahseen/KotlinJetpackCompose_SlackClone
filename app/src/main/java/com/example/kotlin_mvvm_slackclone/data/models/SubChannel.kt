package com.example.kotlin_mvvm_slackclone.data.models

data class SubChannel(
    val id:Int,
    val channelName:String?,
    val channelCreatedByUserId:Int,
    val channelMembersId:MutableList<Int>,
    val mainMasterChannelId:Int,
    val isPrivateChannel:Boolean=false
)
