package com.multiappshare

import android.content.Context
import com.multiappshare.model.AppGroup
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class GroupsRepository(context: Context) {
    private val file = File(context.filesDir, "groups.json")

    fun saveGroups(groups: List<AppGroup>) {
        val jsonString = Json.encodeToString(groups)
        file.writeText(jsonString)
    }

    fun loadGroups(): List<AppGroup> {
        if (!file.exists()) return emptyList()
        return try {
            Json.decodeFromString(file.readText())
        } catch (_: Exception) {
            emptyList()
        }
    }
}
