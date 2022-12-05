package com.example.kotlin_mvvm_slackclone.domain.repository

import com.example.kotlin_mvvm_slackclone.data.models.ChannelThread
import kotlinx.coroutines.flow.Flow

interface ChannelThreadRepository {
    suspend fun createChannelThread(channelThread: ChannelThread)

    fun getChannelThreads(channelId:Int): Flow<List<ChannelThread>>

    fun getChannelThreadById(threadId:Int): Flow<ChannelThread?>
}