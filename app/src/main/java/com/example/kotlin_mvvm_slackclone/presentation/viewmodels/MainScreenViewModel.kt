package com.example.kotlin_mvvm_slackclone.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_mvvm_slackclone.data.MockData
import com.example.kotlin_mvvm_slackclone.data.models.SubChannel
import com.example.kotlin_mvvm_slackclone.domain.repository.SubChannelRepository
import com.example.kotlin_mvvm_slackclone.presentation.events.AuthEvents
import com.example.kotlin_mvvm_slackclone.presentation.events.ChannelEvents
import com.example.kotlin_mvvm_slackclone.utils.PrefManager
import com.example.kotlin_mvvm_slackclone.utils.Routes
import com.example.kotlin_mvvm_slackclone.utils.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    application: Application,
    private val subChannelRepository: SubChannelRepository
): ViewModel() {
    private val prefManager= PrefManager(application.applicationContext)

    val subChannelsPublicList=subChannelRepository
        .getSubChannelChannels(prefManager.masterChannelGsonToObj(prefManager.masterChannelValue).id,false)

    init {
        Log.d("listSizeee",subChannelsPublicList.toString())
    }
    var channelNameField by mutableStateOf("")
        private set
    var channelIsPrivate by mutableStateOf(false)
        private set
    private val _uiEvent= Channel<UIEvent> ()
    val uiEvent=_uiEvent.receiveAsFlow()

    fun navigateToAddChannelScreen(masterChannelId:Int){
        sendUIEvent(UIEvent.NavigateToNewIntent(Routes.ADD_CHANNEL_ROUTE))
    }

    private fun sendUIEvent(uiEvent: UIEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
    fun onEvent(event: ChannelEvents){
        when(event){
            is ChannelEvents.OnChannelNameChange->{
                channelNameField=event.channelName
            }
            is ChannelEvents.OnChannelVisibilityChange->{
                channelIsPrivate=event.isPrivate
            }
            is ChannelEvents.OnCreateChannel->{
                val channelName=event.channelName
                val isPrivate=event.isPrivate
                val createdByUserId=event.createdByUserId
                val masterChannelId=event.masterChannelId
                if(channelName.isNullOrEmpty()){
                    sendUIEvent(UIEvent.ShowSnackBar("A channel name is required !"))
                }else {
                    val list= mutableListOf<Int>()
                    val subChannel = SubChannel(
                        (3..100).random(), channelName,
                        createdByUserId,
                        list,
                        masterChannelId
                    )
                    viewModelScope.launch {
                        createSubChannel(subChannel)
                        sendUIEvent(UIEvent.ShowSnackBar("$channelName created successfully"))
                        Log.d("mockDataSize", MockData.subChannelList.size.toString())
                        sendUIEvent(UIEvent.LoadDataAgain(true))
                    }
                    Log.d("data is :","$channelName, $isPrivate, $createdByUserId")
                }
            }
            is ChannelEvents.ShowChannelDetails->{
                // show intent and send data to channel activity
                sendUIEvent(UIEvent.NavigateToNewIntentWithId(Routes.CHANNEL_DETAILS_ROUTE,event.channelId))
            }

        }
    }

    private suspend fun createSubChannel(subChannel: SubChannel){
        subChannelRepository.createSubChannel(subChannel)
    }
}
