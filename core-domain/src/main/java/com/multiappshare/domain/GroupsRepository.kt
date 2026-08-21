package com.multiappshare.domain

import android.content.Context
import com.multiappshare.data.local.GroupDao
import com.multiappshare.model.AppGroup
import timber.log.Timber
import java.io.File

class GroupsRepository(
    private val groupDao: GroupDao,
    context: Context,
) {
    private val file = File(context.filesDir, "groups.json")

    suspend fun saveGroups(groups: List<AppGroup>) {
        val withIds = groups.map { GroupIds.ensure(it) }
        groupDao.replaceAllGroups(withIds)
        saveToJsonBackup(withIds)
    }

    private fun saveToJsonBackup(groups: List<AppGroup>) {
        try {
            file.writeText(BackupCodec.encode(groups))
        } catch (e: Exception) {
            Timber.e(e, "Failed to write groups.json shadow backup")
        }
    }

    fun encodeBackupPayload(
        groups: List<AppGroup>,
        settings: BackupSettings? = null,
        lastPayload: ShareSessionSnapshot? = null,
    ): String = BackupCodec.encode(groups, settings, lastPayload)

    fun parsePlaintextBackup(jsonText: String): List<AppGroup> =
        BackupCodec.parse(jsonText).groups.map { GroupIds.ensure(it) }

    fun parseBackupDocument(jsonText: String): BackupWrapper = BackupCodec.parse(jsonText)

    suspend fun loadGroups(): List<AppGroup> {
        val dbGroups = groupDao.getAllGroups()
        if (dbGroups.isNotEmpty()) {
            return dbGroups
        }
        if (!file.exists()) return emptyList()
        return try {
            val groups = parsePlaintextBackup(file.readText())
            if (groups.isNotEmpty()) {
                groupDao.insertGroups(groups)
            }
            groups
        } catch (_: Exception) {
            emptyList()
        }
    }
}
