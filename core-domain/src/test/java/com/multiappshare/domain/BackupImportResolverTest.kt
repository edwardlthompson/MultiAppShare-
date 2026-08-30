package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class BackupImportResolverTest {

    private val signal = AppInfo(packageName = "org.signal", activityName = "Main", appName = "Signal")
    private val telegram = AppInfo(packageName = "org.telegram", activityName = "Main", appName = "Telegram")
    private val matrix = AppInfo(packageName = "im.vector", activityName = "Main", appName = "Element")

    private val existingChat = AppGroup(name = "Chat", apps = listOf(signal), usageCount = 5)
    private val existingWork = AppGroup(name = "Work", apps = listOf(matrix), usageCount = 2)

    private val importedChat = AppGroup(name = "Chat", apps = listOf(telegram), usageCount = 10)
    private val importedSocial = AppGroup(name = "Social", apps = listOf(signal, telegram), usageCount = 1)

    @Test
    fun replacesExistingGroupsWhenStrategyIsReplace() {
        val result = BackupImportResolver.resolve(
            existingGroups = listOf(existingChat, existingWork),
            importedGroups = listOf(importedChat, importedSocial),
            strategy = ImportStrategy.REPLACE,
        )
        assertEquals(2, result.size)
        assertEquals(listOf("Chat", "Social"), result.map { it.name })
    }

    @Test
    fun mergesGroupsWhenStrategyIsMerge() {
        val result = BackupImportResolver.resolve(
            existingGroups = listOf(existingChat, existingWork),
            importedGroups = listOf(importedChat, importedSocial),
            strategy = ImportStrategy.MERGE,
        )
        assertEquals(3, result.size)
        val chat = result.first { it.name.equals("Chat", ignoreCase = true) }
        assertEquals(2, chat.apps.size)
        assertEquals(10, chat.usageCount)
    }
}
