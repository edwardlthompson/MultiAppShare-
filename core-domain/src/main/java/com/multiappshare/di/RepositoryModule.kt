package com.multiappshare.di

import android.content.Context
import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.HistoryRepository
import com.multiappshare.domain.SettingsRepository
import com.multiappshare.data.local.GroupDao
import com.multiappshare.data.local.HistoryDao
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
    fun provideGroupsRepository(groupDao: GroupDao, @ApplicationContext context: Context): GroupsRepository {
        return GroupsRepository(groupDao, context)
    }

    @Provides
    @Singleton
    fun provideHistoryRepository(historyDao: HistoryDao, @ApplicationContext context: Context): HistoryRepository {
        return HistoryRepository(historyDao, context)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepository(context)
    }
}
