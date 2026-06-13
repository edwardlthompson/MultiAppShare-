package com.multiappshare.domain

import android.content.Context
import com.multiappshare.data.local.HistoryDao
import com.multiappshare.model.HistoryItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.File

@Serializable
internal data class HistoryBackupWrapper(
    val version: Int = 1,
    val history: List<HistoryItem>
)

class HistoryRepository(
    private val historyDao: HistoryDao,
    context: Context
) {
    private val file = File(context.filesDir, "history.json")

    suspend fun saveHistory(history: List<HistoryItem>) {
        val limited = history.take(50)
        historyDao.replaceAllHistory(limited)
        saveToJsonBackup(limited)
    }

    private fun saveToJsonBackup(history: List<HistoryItem>) {
        try {
            val backup = HistoryBackupWrapper(version = 1, history = history)
            val jsonString = Json.encodeToString(backup)
            file.writeText(jsonString)
        } catch (e: Exception) {
            Timber.e(e, "Failed to write history.json shadow backup")
        }
    }

    suspend fun loadHistory(): List<HistoryItem> {
        val dbHistory = historyDao.getAllHistory()
        if (dbHistory.isNotEmpty()) {
            return dbHistory
        }
        
        // Fallback or Migration from Legacy JSON
        if (!file.exists()) return emptyList()
        return try {
            val jsonText = file.readText()
            val trimmed = jsonText.trim()
            val history = try {
                Json.decodeFromString<HistoryBackupWrapper>(trimmed).history
            } catch (_: SerializationException) {
                Json.decodeFromString<List<HistoryItem>>(trimmed)
            }
            // Migrate to Room
            if (history.isNotEmpty()) {
                historyDao.insertHistory(history)
            }
            history
        } catch (_: Exception) {
            emptyList()
        }
    }
}
