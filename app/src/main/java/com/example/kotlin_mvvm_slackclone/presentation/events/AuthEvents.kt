package com.example.kotlin_mvvm_slackclone.presentation.events

sealed class AuthEvents{
    data class FindTeamDomain(val channelName:String): AuthEvents()
    data class VerifySignIn(val email:String,val pass:String): AuthEvents()
    data class OnChannelNameChange(val channelNameValue: String): AuthEvents()
    data class OnEmailChange(val email: String): AuthEvents()
    data class OnPasswordChange(val password: String): AuthEvents()
    data class GetUserDetailsById(val userId:Int):AuthEvents()
}
