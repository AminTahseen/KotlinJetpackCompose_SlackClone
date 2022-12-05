package com.example.kotlin_mvvm_slackclone.data.dao.masterChannel

import com.example.kotlin_mvvm_slackclone.data.MockData
import com.example.kotlin_mvvm_slackclone.data.models.MasterChannel
import kotlinx.coroutines.flow.*

class MasterChannelDaoImpl:MasterChannelDao {
    private val listFlowOfMasterChannel = MutableStateFlow(MockData.masterChannelList)

    override suspend fun createMaster(masterChannel: MasterChannel) {
        MockData.masterChannelList.toMutableList().add(masterChannel)
    }

    override fun getMasterChannels(): Flow<List<MasterChannel>> {
        return listFlowOfMasterChannel
    }

    override fun getMasterChannelByName(channelName: String): Flow<MasterChannel?> {
      var masterChannel=listFlowOfMasterChannel.map { list ->
          list.find  { it.channelName==channelName }
      }
       return masterChannel
    }
}