package com.example.kotlin_mvvm_slackclone.domain.repository

import com.example.kotlin_mvvm_slackclone.data.models.SubChannel
import kotlinx.coroutines.flow.Flow

interface SubChannelRepository {
    suspend fun createSubChannel(subChannel: SubChannel)

    fun getSubChannelChannels(masterChannelId:Int,visibility:Boolean): Flow<List<SubChannel>>

    fun getSubChannelByName(channelName:String): Flow<SubChannel?>

    fun getSubChannelById(channelId:Int): Flow<SubChannel?>

}