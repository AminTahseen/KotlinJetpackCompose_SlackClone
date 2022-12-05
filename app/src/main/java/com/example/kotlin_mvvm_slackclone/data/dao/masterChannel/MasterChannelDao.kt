package com.example.kotlin_mvvm_slackclone.data.dao.masterChannel

import com.example.kotlin_mvvm_slackclone.data.models.MasterChannel
import kotlinx.coroutines.flow.Flow

interface MasterChannelDao {
    suspend fun createMaster(masterChannel: MasterChannel)

    fun getMasterChannels(): Flow<List<MasterChannel>>

    fun getMasterChannelByName(channelName:String):Flow<MasterChannel?>
}