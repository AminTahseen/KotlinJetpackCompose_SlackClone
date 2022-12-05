package com.example.kotlin_mvvm_slackclone.data.repository

import com.example.kotlin_mvvm_slackclone.data.dao.subChannel.SubChannelDao
import com.example.kotlin_mvvm_slackclone.data.models.SubChannel
import com.example.kotlin_mvvm_slackclone.domain.repository.SubChannelRepository
import kotlinx.coroutines.flow.Flow

class SubChannelRepositoryImpl(private val subChannelDao: SubChannelDao)
    :SubChannelRepository{
    override suspend fun createSubChannel(subChannel: SubChannel) {
        subChannelDao.createSubChannel(subChannel = subChannel)
    }

    override fun getSubChannelChannels(masterChannelId: Int,visibility:Boolean): Flow<List<SubChannel>> {
        return subChannelDao.getSubChannelChannels(masterChannelId,visibility)
    }

    override fun getSubChannelByName(channelName: String): Flow<SubChannel?> {
        return subChannelDao.getSubChannelByName(channelName = channelName)
    }

    override fun getSubChannelById(channelId: Int): Flow<SubChannel?> {
        return subChannelDao.getSubChannelById(channelId)
    }
}