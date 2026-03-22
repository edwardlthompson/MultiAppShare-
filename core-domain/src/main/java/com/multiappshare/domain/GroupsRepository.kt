package com.multiappshare.domain

import android.content.Context
import com.multiappshare.data.local.GroupDao
import com.multiappshare.model.AppGroup
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
internal data class BackupWrapper(
    val version: Int = 1,
    val groups: List<AppGroup>
)

class GroupsRepository(
    private val groupDao: GroupDao,
    context: Context
) {
    private val file = File(context.filesDir, "groups.json")

    suspend fun saveGroups(groups: List<AppGroup>) {
        // 1. Save to Room
        groupDao.insertGroups(groups)
        
        // 2. Auto-save transparent backup to JSON
        saveToJsonBackup(groups)
    }

    private fun saveToJsonBackup(groups: List<AppGroup>) {
        try {
            val backup = BackupWrapper(version = 1, groups = groups)
            val jsonString = Json.encodeToString(backup)
            file.writeText(jsonString)
        } catch (e: Exception) {
            // Fail silently or log
        }
    }

    suspend fun loadGroups(): List<AppGroup> {
        val dbGroups = groupDao.getAllGroups()
        if (dbGroups.isNotEmpty()) {
            return dbGroups
        }
        
        // Fallback or Migration from Legacy JSON
        if (!file.exists()) return emptyList()
        return try {
            val jsonText = file.readText()
            val groups = if (jsonText.contains("\"version\"")) {
                val backup = Json.decodeFromString<BackupWrapper>(jsonText)
                backup.groups
            } else {
                Json.decodeFromString<List<AppGroup>>(jsonText)
            }
            // Migrate to Room
            if (groups.isNotEmpty()) {
                groupDao.insertGroups(groups)
            }
            groups
        } catch (_: Exception) {
            emptyList()
        }
    }
}
