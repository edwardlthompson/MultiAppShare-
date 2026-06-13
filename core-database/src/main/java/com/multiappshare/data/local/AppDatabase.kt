package com.multiappshare.data.local

import android.util.Log
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import com.multiappshare.model.HistoryItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Dao
interface GroupDao {
    @Query("SELECT * FROM groups")
    suspend fun getAllGroups(): List<AppGroup>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<AppGroup>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: AppGroup)

    @Delete
    suspend fun deleteGroup(group: AppGroup)

    @Query("DELETE FROM groups")
    suspend fun deleteAllGroups()

    @Transaction
    suspend fun replaceAllGroups(groups: List<AppGroup>) {
        deleteAllGroups()
        if (groups.isNotEmpty()) {
            insertGroups(groups)
        }
    }
}

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY timestamp DESC LIMIT 50")
    suspend fun getAllHistory(): List<HistoryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: List<HistoryItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItem(item: HistoryItem)

    @Query("DELETE FROM history")
    suspend fun deleteAllHistory()

    @Transaction
    suspend fun replaceAllHistory(history: List<HistoryItem>) {
        deleteAllHistory()
        if (history.isNotEmpty()) {
            insertHistory(history)
        }
    }
}

internal class Converters {
    @TypeConverter
    fun fromAppInfoList(value: List<AppInfo>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toAppInfoList(value: String): List<AppInfo> {
        return try {
            Json.decodeFromString(value)
        } catch (e: Exception) {
            Log.w("AppDatabaseConverters", "Failed to decode app list JSON", e)
            emptyList()
        }
    }
}

@Database(entities = [AppGroup::class, HistoryItem::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun historyDao(): HistoryDao
}
