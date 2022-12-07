package com.example.kotlin_mvvm_slackclone.data.models

data class MasterChannel(
    val id:Int,
    val channelName:String?,
    val channelCreatedByUserId:Int,
    val channelMembersId:MutableList<Int>

)
