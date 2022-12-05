package com.example.kotlin_mvvm_slackclone.data.dao.users

import com.example.kotlin_mvvm_slackclone.data.MockData
import com.example.kotlin_mvvm_slackclone.data.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class UsersDaoImpl: UsersDao {
    private val listFlowOfUsers = MutableStateFlow(MockData.usersList)
    override suspend fun createUser(user: User) {
        MockData.usersList.toMutableList().add(user)
    }

    override fun getUsers(): Flow<List<User>> {
        val list=MockData.usersList
        listFlowOfUsers.value=list
        return listFlowOfUsers
    }

    override fun getUserByEmailAndPass(email: String?, pass: String?): Flow<User?> {
        var user=listFlowOfUsers.map { list ->
            list.find  { it.userEmail==email && it.userPass==pass }
        }
        return user
    }

    override fun getUserById(userId: Int): Flow<User?> {
        var user=listFlowOfUsers.map { list ->
            list.find  { it.id==userId }
        }
        return user
    }
}