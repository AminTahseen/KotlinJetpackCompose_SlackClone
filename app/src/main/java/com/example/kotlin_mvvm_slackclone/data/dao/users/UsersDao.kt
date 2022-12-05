package com.example.kotlin_mvvm_slackclone.data.dao.users

import com.example.kotlin_mvvm_slackclone.data.models.User
import kotlinx.coroutines.flow.Flow

interface UsersDao {
    suspend fun createUser(user: User)

    fun getUsers():Flow<List<User>>

    fun getUserByEmailAndPass(email:String?,pass:String?):Flow<User?>

    fun getUserById(userId:Int):Flow<User?>
}