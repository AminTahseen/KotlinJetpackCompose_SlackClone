package com.example.kotlin_mvvm_slackclone.presentation.events

sealed class ChannelEvents{
    data class OnChannelNameChange(val channelName: String): ChannelEvents()
    data class OnChannelVisibilityChange(val isPrivate:Boolean=false):ChannelEvents()
    data class OnCreateChannel
        (
        val channelName: String,
        val isPrivate: Boolean,
        val createdByUserId:Int,
        val masterChannelId:Int
        ):ChannelEvents()
    data class ShowChannelDetails(val channelId:Int):ChannelEvents()
    //channel thread events
    data class ShowSearchBar(val showSearch:Boolean):ChannelEvents()
    data class AddChannelThread(val channelId:Int):ChannelEvents()
    data class OnChannelThreadDetailsChange(val channelDetails:String):ChannelEvents()
    data class OnCreateChannelThread(
        val channelDetails:String,
        val threadByUserId:Int,
        val channelId:Int
    ):ChannelEvents()
}
