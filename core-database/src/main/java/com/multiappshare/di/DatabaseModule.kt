package com.multiappshare.di

import android.content.Context
import androidx.room.Room
import com.multiappshare.core.database.BuildConfig
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
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        val builder = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "multiappshare_db"
        )
        // Release: never wipe user groups/history on schema mismatch — add Migration X→Y when bumping DB version.
        // Debug: destructive rebuild OK for fast iteration (see BUILD_PLAN F.3).
        if (BuildConfig.DEBUG) {
            builder.fallbackToDestructiveMigration()
        }
        return builder.build()
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
