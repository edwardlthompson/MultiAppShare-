package com.multiappshare.domain

import com.multiappshare.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class AppSuccessSorterTest {

    private val signal = AppInfo(packageName = "org.signal", activityName = "Main", appName = "Signal")
    private val telegram = AppInfo(packageName = "org.telegram", activityName = "Main", appName = "Telegram")
    private val matrix = AppInfo(packageName = "im.vector", activityName = "Main", appName = "Element")

    @Test
    fun sortsAppsByMostRecentSuccessTimestampDescending() {
        val apps = listOf(matrix, signal, telegram)
        val timestamps = mapOf(
            "org.signal" to 1000L,
            "org.telegram" to 2000L,
            "im.vector" to 500L,
        )

        val sorted = AppSuccessSorter.sortByLastSuccess(apps, timestamps)
        assertEquals(listOf("Telegram", "Signal", "Element"), sorted.map { it.appName })
    }

    @Test
    fun fallsBackToAlphabeticalForUntrackedApps() {
        val apps = listOf(telegram, signal, matrix)
        val timestamps = mapOf(
            "org.telegram" to 2000L,
        )

        val sorted = AppSuccessSorter.sortByLastSuccess(apps, timestamps)
        assertEquals(listOf("Telegram", "Element", "Signal"), sorted.map { it.appName })
    }
}
