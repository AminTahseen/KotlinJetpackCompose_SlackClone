package com.example.kotlin_mvvm_slackclone.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.kotlin_mvvm_slackclone.domain.repository.UsersRepository
import com.example.kotlin_mvvm_slackclone.utils.Routes
import com.example.kotlin_mvvm_slackclone.presentation.events.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.kotlin_mvvm_slackclone.data.models.User
import com.example.kotlin_mvvm_slackclone.domain.repository.MasterChannelRepository
import com.example.kotlin_mvvm_slackclone.presentation.events.AuthEvents
import com.example.kotlin_mvvm_slackclone.utils.PrefManager
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val usersRepository: UsersRepository,
    private val masterChannelRepository: MasterChannelRepository,
    savedStateHandle: SavedStateHandle
):ViewModel() {
    private val prefManager=PrefManager(application.applicationContext)

    var foundChannelName by mutableStateOf("")
        private set
    // input fields
    var channelNameField by mutableStateOf("")
        private set
    var emailField by mutableStateOf("")
        private set
    var passwordField by mutableStateOf("")
        private set

    init {
        val channelName = savedStateHandle.get<String>("channelName")
        if (channelName!=""){
            if (channelName != null) {
                foundChannelName=channelName
            }
        }
    }
    private val _uiEvent= Channel<UIEvent> ()
    val uiEvent=_uiEvent.receiveAsFlow()
    var user: Flow<User?>? =null

    fun onEvent(event: AuthEvents){
        when(event){
            is AuthEvents.VerifySignIn ->{
                verifySignIn()
            }
            is AuthEvents.OnChannelNameChange ->{
                channelNameField=event.channelNameValue
            }
            is AuthEvents.FindTeamDomain ->{
                findTeam()
            }
            is AuthEvents.OnEmailChange ->{
                emailField=event.email
            }
            is AuthEvents.OnPasswordChange ->{
                passwordField=event.password
            }
            is AuthEvents.GetUserDetailsById->{
                user=usersRepository.getUserById(event.userId)
            }
        }
    }
    private fun verifySignIn() {
        if (emailField.isNullOrEmpty()||passwordField.isNullOrEmpty()) {
            sendUIEvent(UIEvent.ShowSnackBar("One or more fields cannot be empty !"))
        }else{
            viewModelScope.launch {
                verifyUserLogin()
            }
        }
    }
    private fun findTeam(){
        Log.d("mutableCheck",channelNameField)
        if(channelNameField.isNullOrEmpty()) {
            sendUIEvent(UIEvent.ShowSnackBar("Field cannot be empty !"))
        }else {
            viewModelScope.launch {
                findTeamDomain()
            }
        }
    }
    private suspend fun verifyUserLogin(){
        val user=usersRepository.getUserByEmailAndPass(emailField,passwordField)
        user.collect {
            if(it!=null){
                val userGsonString=prefManager.userObjToGsonString(it)
                prefManager.loggedInUserValue=userGsonString
                sendUIEvent(UIEvent.NavigateToNewIntent(Routes.LOGGED_IN_INTENT))
            }else{
                sendUIEvent(UIEvent.ShowSnackBar("No User Exists !"))
            }
        }
    }
    private suspend fun findTeamDomain(){
        val channel=masterChannelRepository.getMasterChannelByName(channelNameField)
        channel.collect {
            if(it!=null){
                val masterChannelGSONString=prefManager.masterChannelObjToGsonString(it)
                prefManager.masterChannelValue=masterChannelGSONString
                sendUIEvent(UIEvent.Navigate(Routes.LOGIN_VERIFY_USER_SCREEN+"?channelName=${it.channelName}"))
            }else{
                sendUIEvent(UIEvent.ShowSnackBar("No Such Channel Exists !"))
            }
        }
    }
    private fun sendUIEvent(uiEvent: UIEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }

}