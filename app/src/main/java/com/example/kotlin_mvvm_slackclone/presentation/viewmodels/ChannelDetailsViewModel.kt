package com.example.kotlin_mvvm_slackclone.presentation.viewmodels

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_mvvm_slackclone.common.utils.FileUtils
import com.example.kotlin_mvvm_slackclone.data.MockData
import com.example.kotlin_mvvm_slackclone.data.models.ChannelThread
import com.example.kotlin_mvvm_slackclone.data.models.SubChannel
import com.example.kotlin_mvvm_slackclone.domain.repository.ChannelThreadRepository
import com.example.kotlin_mvvm_slackclone.domain.repository.SubChannelRepository
import com.example.kotlin_mvvm_slackclone.presentation.events.ChannelEvents
import com.example.kotlin_mvvm_slackclone.utils.Routes
import com.example.kotlin_mvvm_slackclone.utils.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
@Suppress("UNNECESSARY_SAFE_CALL")
class ChannelDetailsViewModel @Inject constructor(
    private val subChannelRepository: SubChannelRepository,
    private val channelThreadRepository: ChannelThreadRepository,
): ViewModel() {

    private val _uiEvent= Channel<UIEvent> ()
    val uiEvent=_uiEvent.receiveAsFlow()
    var channelIdValue by mutableStateOf(-1)
        private set

    var subChannel: Flow<SubChannel?>? =null

   var channelThreads= emptyFlow<List<ChannelThread>>()

    private val _isLoading= MutableStateFlow(false)
    val isLoading=_isLoading.asStateFlow()

    private val _showSearchBar= MutableStateFlow(false)
    val showSearchBar=_showSearchBar.asStateFlow()

    // channel thread fields
    var channelThreadField by mutableStateOf("")
        private set

    init {
        Log.d("channelThreadListView",MockData.subChannelThreadList.size.toString())
    }
//    fun loadStuff(){
//        viewModelScope.launch {
//            _isLoading.value=true
//            delay(3000L)
//            _isLoading.value=false
//        }
//    }

    private fun sendUIEvent(uiEvent: UIEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
    fun onEvent(event: ChannelEvents){
        when(event) {
            is ChannelEvents.ShowChannelDetails->{
                Log.d("channelThreadListView2",MockData.subChannelThreadList.size.toString())
                channelIdValue=event.channelId
                subChannel=subChannelRepository.getSubChannelById(channelIdValue)
                channelThreads=channelThreadRepository.getChannelThreads(channelIdValue)

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
                val imageUri=event.imageUri
                val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
                val threadPostDate = LocalDateTime.now().format(formatter)

                val replyCount=0
                if(threadDetails.isNullOrEmpty() && imageUri==null){
                    sendUIEvent(UIEvent.ShowSnackBar("Thread details are required !"))
                }else{
                    var thread: ChannelThread? =null
                    if(imageUri!=null && !threadDetails.isNullOrEmpty())
                    {
                      thread = ChannelThread(
                            (3..100).random(),
                            userId,
                            threadPostDate,
                            channelId,
                            emptyList(),
                            replyCount,
                            threadDetails,
                            imageUri
                        )
                    }else if(imageUri==null && !threadDetails.isNullOrEmpty()){
                        thread = ChannelThread(
                            (3..100).random(),
                            userId,
                            threadPostDate,
                            channelId,
                            emptyList(),
                            replyCount,
                            threadDetails,
                        )
                    }else{
                        thread = ChannelThread(
                            (3..100).random(),
                            userId,
                            threadPostDate,
                            channelId,
                            emptyList(),
                            replyCount,
                            null,
                            imageUri
                        )
                    }
                    viewModelScope.launch {
                        if (thread != null) {
                            addThreadPost(thread)
                        }
                        Log.d("threadSize", MockData.subChannelThreadList.size.toString())
                        sendUIEvent(UIEvent.ShowSnackBar("Thread created successfully"))
                        sendUIEvent(UIEvent.Loader)
                    }
                }
            }
            is ChannelEvents.UpdateThreadReaction->{
                val thread=event.thread
                val reactionIndex=event.reactionIndex
                val loggedInUserId=event.loggedInUserId
                Log.d("ReactionIndex :",reactionIndex.toString())
                when{
                    thread.reactionList.isEmpty()->{

                    }
                }
            }
            else->{

            }
        }
    }

    private suspend fun addThreadPost(thread: ChannelThread){
        channelThreadRepository.createChannelThread(thread)
    }

    fun getVaultImageFromLocal(context: Context,imageUri: Uri):Uri{
        val fileUtils= FileUtils()
        val fullFilePath: String? =fileUtils.getPathFromUri(context = context, uri = imageUri)
        return Uri.parse(fullFilePath)
    }
}