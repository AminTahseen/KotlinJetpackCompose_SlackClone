package com.example.kotlin_mvvm_slackclone.data.dao.subChannel

import com.example.kotlin_mvvm_slackclone.data.MockData
import com.example.kotlin_mvvm_slackclone.data.models.SubChannel
import kotlinx.coroutines.flow.*

class SubChannelDaoImpl : SubChannelDao {
    private val listFlowOfSubChannels = MutableStateFlow(MockData.subChannelList)

    override suspend fun createSubChannel(subChannel: SubChannel) {
        MockData.subChannelList.add(subChannel)
    }

    override fun getSubChannelChannels(
        masterChannelId: Int,
        visibility: Boolean
    ): Flow<List<SubChannel>> {
        val list = listFlowOfSubChannels.map {
            it.filter { subChannel ->
                subChannel.mainMasterChannelId === masterChannelId
                        && subChannel.isPrivateChannel === visibility
            }
        }
        return list
    }

    override fun getSubChannelByName(channelName: String): Flow<SubChannel?> {
        TODO("Not yet implemented")
    }

    override fun getSubChannelById(channelId: Int): Flow<SubChannel?> {
        var channel = listFlowOfSubChannels.map { list ->
            list.find { it.id == channelId }
        }
        return channel
    }
}