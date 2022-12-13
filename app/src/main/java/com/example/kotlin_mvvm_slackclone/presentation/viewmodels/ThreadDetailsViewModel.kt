package com.example.kotlin_mvvm_slackclone.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlin_mvvm_slackclone.data.models.ChannelThread
import com.example.kotlin_mvvm_slackclone.data.models.SubChannel
import com.example.kotlin_mvvm_slackclone.data.models.User
import com.example.kotlin_mvvm_slackclone.domain.repository.ChannelThreadRepository
import com.example.kotlin_mvvm_slackclone.domain.repository.SubChannelRepository
import com.example.kotlin_mvvm_slackclone.domain.repository.UsersRepository
import com.example.kotlin_mvvm_slackclone.presentation.events.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThreadDetailsViewModel@Inject constructor(
    private val channelThreadRepository: ChannelThreadRepository,
    private val usersRepository: UsersRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    var foundThreadId by mutableStateOf(-1)
        private set
    var foundChannelName by mutableStateOf("")
    var thread: ChannelThread? = null
    init {
        val threadId = savedStateHandle.get<Int>("threadId")
        val channelName=savedStateHandle.get<String>("channelName")
        if (threadId != -1 && channelName!="") {
            if (threadId != null && channelName!=null) {
                foundThreadId=threadId
                foundChannelName=channelName
                viewModelScope.launch {
                    getThreadDetails(foundThreadId)
                }
            }
        }
    }
    private fun sendUIEvent(uiEvent: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }

    private suspend fun getThreadDetails(threadId:Int){
        channelThreadRepository.getChannelThreadById(threadId)
            .collect{
            thread=it
        }
    }

}