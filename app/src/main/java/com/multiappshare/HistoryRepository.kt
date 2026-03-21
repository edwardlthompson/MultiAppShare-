package com.multiappshare

import android.content.Context
import com.multiappshare.model.HistoryItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class HistoryRepository(context: Context) {
    private val file = File(context.filesDir, "history.json")

    fun saveHistory(history: List<HistoryItem>) {
        val jsonString = Json.encodeToString(history.take(50))
        file.writeText(jsonString)
    }

    fun loadHistory(): List<HistoryItem> {
        if (!file.exists()) return emptyList()
        return try {
            Json.decodeFromString(file.readText())
        } catch (_: Exception) {
            emptyList()
        }
    }
}
