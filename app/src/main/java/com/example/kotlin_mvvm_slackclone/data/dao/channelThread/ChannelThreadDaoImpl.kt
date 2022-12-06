package com.example.kotlin_mvvm_slackclone.data.dao.channelThread

import android.util.Log
import com.example.kotlin_mvvm_slackclone.data.MockData
import com.example.kotlin_mvvm_slackclone.data.models.ChannelThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class ChannelThreadDaoImpl:ChannelThreadDao {
    private val channelThreadList= MutableStateFlow(MockData.subChannelThreadList)

    override suspend fun createChannelThread(channelThread: ChannelThread) {
        Log.d("InsideThis","heree")
        Log.d("InsideThis",channelThread.toString())
        MockData.subChannelThreadList.add(channelThread)
        Log.d("InsideThis",MockData.subChannelThreadList.size.toString())

    }

    override fun getChannelThreads(channelId: Int): Flow<List<ChannelThread>> {
        val list=channelThreadList.map {
            it.filter { channelThread ->
                channelThread.channelId===channelId
            }
        }
        Log.d("InsideThis",MockData.subChannelThreadList.toString())
        Log.d("InsideThis",list.toString())

        return list
    }

    override fun getChannelThreadById(threadId: Int): Flow<ChannelThread?> {
        TODO("Not yet implemented")
    }
}