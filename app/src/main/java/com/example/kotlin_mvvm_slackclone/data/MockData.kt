package com.example.kotlin_mvvm_slackclone.data

import com.example.kotlin_mvvm_slackclone.data.models.ChannelThread
import com.example.kotlin_mvvm_slackclone.data.models.MasterChannel
import com.example.kotlin_mvvm_slackclone.data.models.SubChannel
import com.example.kotlin_mvvm_slackclone.data.models.User

class MockData {
    companion object MockData {
        val usersList= mutableListOf(
            User(1,
                "Amin",
                "ameen123@gmail.com",
                "abc.123",
                true
            ),
            User(2,
                "Ahmed Raza",
                "ahmed@gmail.com",
                "abed.1235",
                true
            ),
            User(3,
                "Ali Khan",
                "aliKhan@gmail.com",
                "abide.1234",
                false
            ),
        )
        val masterChannelList= mutableListOf(
            MasterChannel(
                1,
                "MainChannel",
                1,
                listOf(1,2,3)),

            MasterChannel(
                2,
                "OtherChannel",
                2,
                listOf(2,3))
        )
         val subChannelList= mutableListOf(
             SubChannel(
                 1,
                 "SubChannel1",
                 1,
                 listOf(1,2),
             1,
                 false
             ),

             SubChannel(
                 2,
                 "SubChannel2",
                 1,
                 listOf(1,2),
                 1,
                 true
             )
         )
        val subChannelThreadList= mutableListOf(
            ChannelThread(
                1,
                1,
                "11 Sept 12:30 AM",
                1,
                listOf(1,2,3),
                2,
                "Some Dummy content goes here"
            ),
            ChannelThread(
                2,
                1,
                "11 Sept 12:30 AM",
                1,
                listOf(1,2,3),
                1,
                "Another Dummy content goes here",
            "https://via.placeholder.com/300x200"
            )
        )
    }
}