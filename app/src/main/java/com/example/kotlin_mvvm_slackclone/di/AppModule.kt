package com.example.kotlin_mvvm_slackclone.di

import com.example.kotlin_mvvm_slackclone.data.dao.channelThread.ChannelThreadDao
import com.example.kotlin_mvvm_slackclone.data.dao.channelThread.ChannelThreadDaoImpl
import com.example.kotlin_mvvm_slackclone.data.dao.masterChannel.MasterChannelDao
import com.example.kotlin_mvvm_slackclone.data.dao.masterChannel.MasterChannelDaoImpl
import com.example.kotlin_mvvm_slackclone.data.dao.subChannel.SubChannelDao
import com.example.kotlin_mvvm_slackclone.data.dao.subChannel.SubChannelDaoImpl
import com.example.kotlin_mvvm_slackclone.data.dao.users.UsersDao
import com.example.kotlin_mvvm_slackclone.data.dao.users.UsersDaoImpl
import com.example.kotlin_mvvm_slackclone.data.repository.ChannelThreadRepositoryImpl
import com.example.kotlin_mvvm_slackclone.data.repository.MasterChannelRepositoryImpl
import com.example.kotlin_mvvm_slackclone.data.repository.SubChannelRepositoryImpl
import com.example.kotlin_mvvm_slackclone.data.repository.UsersRepositoryImpl
import com.example.kotlin_mvvm_slackclone.domain.repository.ChannelThreadRepository
import com.example.kotlin_mvvm_slackclone.domain.repository.MasterChannelRepository
import com.example.kotlin_mvvm_slackclone.domain.repository.SubChannelRepository
import com.example.kotlin_mvvm_slackclone.domain.repository.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // users
    @Provides
    @Singleton
    fun provideUsersDao(): UsersDao {
        return UsersDaoImpl()
    }
    @Provides
    @Singleton
    fun providesUserRepository(usersDao: UsersDao):UsersRepository{
        return UsersRepositoryImpl(usersDao)
    }

    // master channels
    @Provides
    @Singleton
    fun provideMasterChannelsDao():MasterChannelDao{
        return MasterChannelDaoImpl()
    }

    @Provides
    @Singleton
    fun provideMasterChannelRepository(
        masterChannelDao: MasterChannelDao
    ):MasterChannelRepository{
        return MasterChannelRepositoryImpl(masterChannelDao)
    }

    // sub channels
    @Provides
    @Singleton
    fun provideSubChannelsDao(): SubChannelDao {
        return SubChannelDaoImpl()
    }

    @Provides
    @Singleton
    fun provideSubChannelsRepository(subChannelDao: SubChannelDao):SubChannelRepository{
        return SubChannelRepositoryImpl(subChannelDao)
    }

    // channel threads
    @Provides
    @Singleton
    fun provideChannelThreadsDao():ChannelThreadDao{
        return ChannelThreadDaoImpl()
    }

    @Provides
    @Singleton
    fun provideChannelThreadRepository(channelThreadDao: ChannelThreadDao):ChannelThreadRepository{
        return ChannelThreadRepositoryImpl(channelThreadDao)
    }
}