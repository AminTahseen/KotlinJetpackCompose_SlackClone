package com.example.kotlin_mvvm_slackclone.presentation.viewmodels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_mvvm_slackclone.data.MockData
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
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
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

    // channel thread fields
    var channelThreadField by mutableStateOf("")
        private set

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
            is ChannelEvents.OnChannelThreadDetailsChange->{
                channelThreadField=event.channelDetails
            }
            is ChannelEvents.OnCreateChannelThread->{
                val threadDetails=event.channelDetails
                val channelId=event.channelId
                val userId=event.threadByUserId
                val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
                val threadPostDate = LocalDateTime.now().format(formatter)

                val reactionList= listOf(0,0,0)
                val replyCount=0
                if(threadDetails.isNullOrEmpty()){
                    sendUIEvent(UIEvent.ShowSnackBar("Thread details is required !"))
                }else{
                    val thread=ChannelThread(
                        (3..100).random(),
                        userId,
                        threadPostDate,
                        channelId,
                        reactionList,
                        replyCount,
                        threadDetails,
                    "abcd"
                    )
                    viewModelScope.launch {
                        Log.d("threadIs",thread.toString())
                        addThreadPost(thread)
                        sendUIEvent(UIEvent.ShowSnackBar("Thread created successfully"))
                    }
                }
            }
        }
    }
    private suspend fun addThreadPost(thread: ChannelThread){
        channelThreadRepository.createChannelThread(thread)
    }
}