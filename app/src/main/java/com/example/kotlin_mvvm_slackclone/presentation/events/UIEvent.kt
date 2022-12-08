package com.example.kotlin_mvvm_slackclone.presentation.events

sealed class UIEvent{
    data class Navigate(val route:String): UIEvent()
    data class ShowSnackBar(val message:String): UIEvent()
    data class NavigateToNewIntent(val intent:String): UIEvent()
    data class NavigateToNewIntentWithId(val intent:String,val id:Int): UIEvent()
    data class LoadDataAgain(val load:Boolean) : UIEvent()

}
