package com.example.kotlin_mvvm_slackclone.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_mvvm_slackclone.data.models.ChannelThread
import com.example.kotlin_mvvm_slackclone.data.models.SubChannel
import com.example.kotlin_mvvm_slackclone.domain.repository.ChannelThreadRepository
import com.example.kotlin_mvvm_slackclone.domain.repository.SubChannelRepository
import com.example.kotlin_mvvm_slackclone.domain.repository.UsersRepository
import com.example.kotlin_mvvm_slackclone.presentation.events.ChannelEvents
import com.example.kotlin_mvvm_slackclone.utils.Routes
import com.example.kotlin_mvvm_slackclone.utils.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChannelDetailsViewModel @Inject constructor(
    application: Application,
    private val subChannelRepository: SubChannelRepository,
    private val channelThreadRepository: ChannelThreadRepository,
): ViewModel() {

    private val _uiEvent= Channel<UIEvent> ()
    val uiEvent=_uiEvent.receiveAsFlow()
    var channelIdValue by mutableStateOf(-1)
        private set

    var subChannel: Flow<SubChannel?>? =null
    var channelThreads:Flow<List<ChannelThread>>?=null

    private val _isLoading= MutableStateFlow(false)
    val isLoading=_isLoading.asStateFlow()

    private val _showSearchBar= MutableStateFlow(false)
    val showSearchBar=_showSearchBar.asStateFlow()


    fun loadStuff(){
        viewModelScope.launch {
            _isLoading.value=true
            delay(3000L)
            _isLoading.value=false
        }
    }

    private fun sendUIEvent(uiEvent: UIEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
    fun onEvent(event: ChannelEvents){
        when(event) {
            is ChannelEvents.ShowChannelDetails->{
                channelIdValue=event.channelId
                subChannel=subChannelRepository.getSubChannelById(channelIdValue)
                channelThreads=channelThreadRepository.getChannelThreads(channelId = event.channelId)
            }
            is ChannelEvents.ShowSearchBar->{
                _showSearchBar.value=event.showSearch
            }
            is ChannelEvents.AddChannelThread->{
                sendUIEvent(UIEvent.NavigateToNewIntentWithId(Routes.ADD_CHANNEL_POST_ROUTE,event.channelId))
            }
        }
    }
}