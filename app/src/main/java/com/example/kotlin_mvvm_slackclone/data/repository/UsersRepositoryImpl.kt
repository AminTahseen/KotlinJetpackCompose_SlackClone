package com.example.kotlin_mvvm_slackclone.data.repository

import com.example.kotlin_mvvm_slackclone.data.dao.users.UsersDao
import com.example.kotlin_mvvm_slackclone.data.models.User
import com.example.kotlin_mvvm_slackclone.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow

class UsersRepositoryImpl(private val usersDao: UsersDao):UsersRepository {
    override suspend fun createUser(user: User) {
        usersDao.createUser(user=user)
    }
    override fun getUsers(): Flow<List<User>> {
        return usersDao.getUsers()
    }

    override fun getUserByEmailAndPass(email: String?, pass: String?): Flow<User?> {
        return usersDao.getUserByEmailAndPass(email = email,pass=pass)
    }

    override fun getUserById(userId: Int): Flow<User?> {
        return usersDao.getUserById(userId = userId)
    }
}