package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DuplicateAppDetectorTest {

    private val signal = AppInfo(packageName = "org.signal", activityName = "Main", appName = "Signal")
    private val telegram = AppInfo(packageName = "org.telegram", activityName = "Main", appName = "Telegram")
    private val matrix = AppInfo(packageName = "im.vector", activityName = "Main", appName = "Element")

    private val group1 = AppGroup(name = "Chat", apps = listOf(signal, telegram))
    private val group2 = AppGroup(name = "Social", apps = listOf(telegram, matrix))
    private val allGroups = listOf(group1, group2)

    @Test
    fun findsOtherGroupsContainingPackage() {
        val matches = DuplicateAppDetector.findOtherGroupsContaining(allGroups, "Chat", "org.telegram")
        assertEquals(listOf("Social"), matches)
    }

    @Test
    fun returnsEmptyWhenNoDuplicates() {
        val matches = DuplicateAppDetector.findOtherGroupsContaining(allGroups, "Chat", "org.signal")
        assertTrue(matches.isEmpty())
    }

    @Test
    fun findsMultipleDuplicatePackages() {
        val targets = setOf("org.signal", "org.telegram")
        val dups = DuplicateAppDetector.findDuplicatePackages(allGroups, "NewGroup", targets)
        assertEquals(listOf("Chat"), dups["org.signal"])
        assertEquals(listOf("Chat", "Social"), dups["org.telegram"])
    }
}
