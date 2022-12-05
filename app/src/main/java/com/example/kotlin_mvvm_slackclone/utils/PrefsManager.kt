package com.example.kotlin_mvvm_slackclone.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.kotlin_mvvm_slackclone.data.models.MasterChannel
import com.example.kotlin_mvvm_slackclone.data.models.User
import com.google.gson.Gson

class PrefManager(context: Context) {
    private val gson = Gson()
    private val appPref = "SlackAppPrefs"

    private val masterChannelKey="MASTER_CHANNEL_KEY"
    private val loggedInUserKey="LOGGED_IN_USER_KEY"

    private val preferences: SharedPreferences = context.getSharedPreferences(appPref,Context.MODE_PRIVATE)

    var masterChannelValue: String
        get() = preferences.getString(masterChannelKey,"null").toString()
        set(value) = preferences.edit().putString(masterChannelKey, value).apply()

    var loggedInUserValue: String
        get() = preferences.getString(loggedInUserKey,"null").toString()
        set(value) = preferences.edit().putString(loggedInUserKey, value).apply()

    fun masterChannelObjToGsonString(obj:MasterChannel):String{
        return gson.toJson(obj)
    }
    fun masterChannelGsonToObj(jsonString: String):MasterChannel{
        return gson.fromJson(jsonString,MasterChannel::class.java)
    }
    fun userObjToGsonString(obj:User):String{
        return gson.toJson(obj)
    }
    fun userGsonToObj(jsonString: String):User{
        return gson.fromJson(jsonString,User::class.java)
    }

}