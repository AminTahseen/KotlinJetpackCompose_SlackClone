package com.example.kotlin_mvvm_slackclone.data.dao.subChannel

import com.example.kotlin_mvvm_slackclone.data.models.SubChannel
import kotlinx.coroutines.flow.Flow

interface SubChannelDao {
    suspend fun createSubChannel(subChannel: SubChannel)

    fun getSubChannelChannels(masterChannelId:Int,visibility:Boolean): Flow<List<SubChannel>>

    fun getSubChannelByName(channelName:String): Flow<SubChannel?>

    fun getSubChannelById(channelId:Int): Flow<SubChannel?>
}