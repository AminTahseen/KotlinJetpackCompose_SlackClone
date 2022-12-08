package com.example.kotlin_mvvm_slackclone.presentation.viewmodels

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
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
import com.example.kotlin_mvvm_slackclone.presentation.events.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
@Suppress("UNNECESSARY_SAFE_CALL")
class ChannelDetailsViewModel @Inject constructor(
    private val subChannelRepository: SubChannelRepository,
    private val channelThreadRepository: ChannelThreadRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    var channelIdValue by mutableStateOf(-1)
        private set

    var subChannel: Flow<SubChannel?>? = null

    var channelThreads: MutableStateFlow<List<ChannelThread>> = MutableStateFlow(emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _refreshReactions = MutableStateFlow(false)
    val refreshReactions = _refreshReactions.asStateFlow()


    private val _showSearchBar = MutableStateFlow(false)
    val showSearchBar = _showSearchBar.asStateFlow()

    var foundChannelId by mutableStateOf(-1)
        private set

    // channel thread fields
    var channelThreadField by mutableStateOf("")
        private set

    init {
        val channelId = savedStateHandle.get<Int>("channelId")
        if (channelId != -1) {
            if (channelId != null) {
                foundChannelId = channelId
            }
        }
    }

    private fun sendUIEvent(uiEvent: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }

    fun onEvent(event: ChannelEvents) {
        when (event) {
            is ChannelEvents.ShowChannelDetails -> {
                Log.d("channelThreadListView2", MockData.subChannelThreadList.size.toString())
                channelIdValue = event.channelId
                subChannel = subChannelRepository.getSubChannelById(channelIdValue)
                getChannelThreads()

            }
            is ChannelEvents.ShowSearchBar -> {
                _showSearchBar.value = event.showSearch
            }
            is ChannelEvents.AddChannelThread -> {
                sendUIEvent(UIEvent.Navigate(Routes.ADD_CHANNEL_POST_ROUTE + "?channelId=${event.channelId}"))
            }
            is ChannelEvents.OnChannelThreadDetailsChange -> {
                channelThreadField = event.channelDetails
            }

            is ChannelEvents.OnCreateChannelThread -> {
                val threadDetails = event.channelDetails
                val channelId = event.channelId
                val userId = event.threadByUserId
                val imageUri = event.imageUri
                val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
                val threadPostDate = LocalDateTime.now().format(formatter)

                val replyCount = 0
                if (threadDetails.isNullOrEmpty() && imageUri == null) {
                    sendUIEvent(UIEvent.ShowSnackBar("Thread details are required !"))
                } else {
                    var thread: ChannelThread?
                    val reactionList = mutableListOf<MutableList<Int>>()
                    if (imageUri != null && !threadDetails.isNullOrEmpty()) {
                        thread = ChannelThread(
                            (3..100).random(),
                            userId,
                            threadPostDate,
                            channelId,
                            reactionList,
                            replyCount,
                            threadDetails,
                            imageUri
                        )
                    } else if (imageUri == null && !threadDetails.isNullOrEmpty()) {
                        thread = ChannelThread(
                            (3..100).random(),
                            userId,
                            threadPostDate,
                            channelId,
                            reactionList,
                            replyCount,
                            threadDetails,
                        )
                    } else {
                        thread = ChannelThread(
                            (3..100).random(),
                            userId,
                            threadPostDate,
                            channelId,
                            reactionList,
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
                    }
                }
            }
            is ChannelEvents.UpdateThreadReaction -> {
                val thread = event.thread
                var reactionIndex = event.reactionIndex
                val loggedInUserId = event.loggedInUserId

                var reactionList = thread.reactionList
                reactionList = addRemoveReaction(
                    reactionIndex,
                    loggedInUserId,
                    reactionList
                )
                Log.d("ReactionList :", reactionList.toString())
                channelThreads.value.find { it.id == thread.id }?.reactionList = reactionList
                Log.d("threadList :", channelThreads.value.toString())
                sendUIEvent(UIEvent.LoadDataAgain(true))
                _refreshReactions.value = !_refreshReactions.value
            }
            is ChannelEvents.ThreadDetails -> {
                val threadId = event.threadId
                val channelName=event.channelName
                sendUIEvent(UIEvent.Navigate(Routes.THREAD_POST_DETAILS_ROUTE + "?threadId=${threadId}&channelName=${channelName}"))
            }
            else -> {

            }
        }
    }

    private fun getChannelThreads() {
        viewModelScope.launch {
            channelThreadRepository.getChannelThreads(channelIdValue).collect {
                channelThreads.value = it
            }
        }
    }

    private fun addRemoveReaction(
        reactionIndex: Int,
        loggedInUserId: Int,
        reactionList: MutableList<MutableList<Int>>
    ): MutableList<MutableList<Int>> {
        if (reactionList.isNotEmpty() && reactionList.indices.contains(reactionIndex)) {
            //index does exists
            Log.d("reaction", "emoji array Exists")
            if (reactionList[reactionIndex].contains(loggedInUserId)) {
                Log.d("reaction", "emoji array contains user reaction")
                reactionList[reactionIndex].remove(loggedInUserId)
            } else {
                Log.d("reaction", "emoji array don't contain user reaction")
                reactionList[reactionIndex].add(loggedInUserId)
            }
        } else {
            // index does not exists
            Log.d("reaction", "emoji array not Exists")
            reactionList.add(
                reactionIndex,
                mutableListOf(loggedInUserId),
            )
        }
        Log.d("threadReactions", reactionList.toString())
        return reactionList
    }

    private suspend fun addThreadPost(thread: ChannelThread) {
        channelThreadRepository.createChannelThread(thread)
    }

    fun getVaultImageFromLocal(context: Context, imageUri: Uri): Uri {
        val fileUtils = FileUtils()
        val fullFilePath: String? = fileUtils.getPathFromUri(context = context, uri = imageUri)
        return Uri.parse(fullFilePath)
    }

    fun checkIfReactionExists(
        reactionList: MutableList<Int>,
        loggedInUserId: Int,
    ): Boolean {

        return reactionList.contains(loggedInUserId)
    }
}