package com.example.kotlin_mvvm_slackclone.data.dao.channelThread

import com.example.kotlin_mvvm_slackclone.data.MockData
import com.example.kotlin_mvvm_slackclone.data.models.ChannelThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class ChannelThreadDaoImpl:ChannelThreadDao {
    private val channelThreadList= MutableStateFlow(MockData.subChannelThreadList.reversed())

    override suspend fun createChannelThread(channelThread: ChannelThread) {
        MockData.subChannelThreadList.toMutableList().add(channelThread)
    }

    override fun getChannelThreads(channelId: Int): Flow<List<ChannelThread>> {
        val list=channelThreadList.map {
            it.filter { channelThread ->
                channelThread.channelId==channelId
            }
        }
        return list
    }

    override fun getChannelThreadById(threadId: Int): Flow<ChannelThread?> {
        TODO("Not yet implemented")
    }
}