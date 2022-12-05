package com.example.kotlin_mvvm_slackclone.domain.repository

import com.example.kotlin_mvvm_slackclone.data.models.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    suspend fun createUser(user: User)

    fun getUsers():Flow<List<User>>

    fun getUserByEmailAndPass(email:String?,pass:String?):Flow<User?>

    fun getUserById(userId:Int):Flow<User?>

}