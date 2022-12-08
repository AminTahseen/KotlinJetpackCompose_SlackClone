package com.example.kotlin_mvvm_slackclone.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.kotlin_mvvm_slackclone.data.models.SubChannel
import com.example.kotlin_mvvm_slackclone.domain.repository.ChannelThreadRepository
import com.example.kotlin_mvvm_slackclone.domain.repository.SubChannelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ThreadDetailsViewModel@Inject constructor(
    private val subChannelRepository: SubChannelRepository,
    private val channelThreadRepository: ChannelThreadRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var foundThreadId by mutableStateOf(-1)
        private set
    var foundChannelName by mutableStateOf("")
    init {
        val threadId = savedStateHandle.get<Int>("threadId")
        val channelName=savedStateHandle.get<String>("channelName")
        if (threadId != -1 && channelName!="") {
            if (threadId != null && channelName!=null) {
                foundThreadId=threadId
                foundChannelName=channelName
            }
        }
    }
}