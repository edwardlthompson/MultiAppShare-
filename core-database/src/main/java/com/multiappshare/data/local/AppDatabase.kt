package com.multiappshare.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Delete
import com.multiappshare.model.AppGroup
import com.multiappshare.model.HistoryItem
import com.multiappshare.model.AppInfo
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
}

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY timestamp DESC LIMIT 50")
    suspend fun getAllHistory(): List<HistoryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: List<HistoryItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItem(item: HistoryItem)
}

class Converters {
    @TypeConverter
    fun fromAppInfoList(value: List<AppInfo>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toAppInfoList(value: String): List<AppInfo> {
        return try {
            Json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
}

@Database(entities = [AppGroup::class, HistoryItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun historyDao(): HistoryDao
}
