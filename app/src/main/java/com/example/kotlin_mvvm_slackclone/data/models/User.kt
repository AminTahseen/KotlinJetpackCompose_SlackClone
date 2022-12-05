package com.example.kotlin_mvvm_slackclone.data.models

data class User(
    val id:Int,
    val userName:String?,
    val userEmail:String?,
    val userPass:String?,
    val isOnline:Boolean=false,
    val imageURL:String= "https://ui-avatars.com/api/?name=$userName&background=fdcb6e&color=000",
)
