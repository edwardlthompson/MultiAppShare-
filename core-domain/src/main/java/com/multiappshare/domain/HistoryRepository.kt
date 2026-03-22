package com.multiappshare.domain

import android.content.Context
import com.multiappshare.data.local.HistoryDao
import com.multiappshare.model.HistoryItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class HistoryBackupWrapper(
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
        // 1. Save to Room
        historyDao.insertHistory(limited)
        
        // 2. Auto-save transparent backup to JSON
        saveToJsonBackup(limited)
    }

    private fun saveToJsonBackup(history: List<HistoryItem>) {
        try {
            val backup = HistoryBackupWrapper(version = 1, history = history)
            val jsonString = Json.encodeToString(backup)
            file.writeText(jsonString)
        } catch (e: Exception) {
            // Fail silently or log
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
            val history = if (jsonText.contains("\"version\"")) {
                val backup = Json.decodeFromString<HistoryBackupWrapper>(jsonText)
                backup.history
            } else {
                Json.decodeFromString<List<HistoryItem>>(jsonText)
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
