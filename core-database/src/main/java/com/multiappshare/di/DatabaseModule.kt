package com.multiappshare.di

import android.content.Context
import androidx.room.Room
import com.multiappshare.data.local.AppDatabase
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
internal object DatabaseModule {

    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "multiappshare_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideGroupDao(database: AppDatabase): GroupDao {
        return database.groupDao()
    }

    @Provides
    @Singleton
    fun provideHistoryDao(database: AppDatabase): HistoryDao {
        return database.historyDao()
    }
}
