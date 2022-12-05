package com.example.kotlin_mvvm_slackclone.data.repository

import com.example.kotlin_mvvm_slackclone.data.dao.masterChannel.MasterChannelDao
import com.example.kotlin_mvvm_slackclone.data.models.MasterChannel
import com.example.kotlin_mvvm_slackclone.domain.repository.MasterChannelRepository
import kotlinx.coroutines.flow.Flow

class MasterChannelRepositoryImpl(private val masterChannelDao: MasterChannelDao):MasterChannelRepository {
    override suspend fun createMaster(masterChannel: MasterChannel) {
        masterChannelDao.createMaster(masterChannel = masterChannel)
    }

    override fun getMasterChannels(): Flow<List<MasterChannel>> {
        return masterChannelDao.getMasterChannels()
    }

    override fun getMasterChannelByName(channelName: String): Flow<MasterChannel?> {
        return masterChannelDao.getMasterChannelByName(channelName = channelName)
    }
}