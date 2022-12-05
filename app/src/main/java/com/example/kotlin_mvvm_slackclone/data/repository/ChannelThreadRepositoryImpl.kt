package com.example.kotlin_mvvm_slackclone.data.repository

import com.example.kotlin_mvvm_slackclone.data.dao.channelThread.ChannelThreadDao
import com.example.kotlin_mvvm_slackclone.data.models.ChannelThread
import com.example.kotlin_mvvm_slackclone.domain.repository.ChannelThreadRepository
import kotlinx.coroutines.flow.Flow

class ChannelThreadRepositoryImpl(private val channelThreadDao: ChannelThreadDao)
    :ChannelThreadRepository {
    override suspend fun createChannelThread(channelThread: ChannelThread) {
        return channelThreadDao.createChannelThread(channelThread=channelThread)
    }

    override fun getChannelThreads(channelId: Int): Flow<List<ChannelThread>> {
        return channelThreadDao.getChannelThreads(channelId=channelId)
    }

    override fun getChannelThreadById(threadId: Int): Flow<ChannelThread?> {
        return channelThreadDao.getChannelThreadById(threadId = threadId)
    }
}