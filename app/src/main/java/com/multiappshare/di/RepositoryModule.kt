package com.multiappshare.di

import android.content.Context
import com.multiappshare.GroupsRepository
import com.multiappshare.HistoryRepository
import com.multiappshare.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGroupsRepository(@ApplicationContext context: Context): GroupsRepository {
        return GroupsRepository(context)
    }

    @Provides
    @Singleton
    fun provideHistoryRepository(@ApplicationContext context: Context): HistoryRepository {
        return HistoryRepository(context)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepository(context)
    }
}
