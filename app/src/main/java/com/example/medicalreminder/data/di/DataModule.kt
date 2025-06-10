package com.example.medicalreminder.data.di

import android.content.Context
import com.example.medicalreminder.data.local.ReminderDao
import com.example.medicalreminder.data.local.ReminderDatabase
import com.example.medicalreminder.data.repository.ReminderRepoImpl
import com.example.medicalreminder.domain.repository.ReminderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideReminderDao(@ApplicationContext context: Context) : ReminderDao {
        val dp = ReminderDatabase.getInstance(context)
        return dp.getReminderDao()
    }

    @Provides
    fun provideReminderRepo(reminderDao: ReminderDao): ReminderRepository {
        return ReminderRepoImpl(reminderDao)
    }

}