package com.multiappshare.domain

import com.multiappshare.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class GroupEditorSearchTest {

    private val sampleApps = listOf(
        AppInfo(packageName = "org.signal", activityName = "MainActivity", appName = "Signal"),
        AppInfo(packageName = "org.telegram.messenger", activityName = "LaunchActivity", appName = "Telegram"),
        AppInfo(packageName = "com.whatsapp", activityName = "ChatActivity", appName = "WhatsApp"),
    )

    @Test
    fun returnsAllAppsWhenQueryIsBlankOrNull() {
        assertEquals(sampleApps, GroupEditorSearch.filter(sampleApps, null))
        assertEquals(sampleApps, GroupEditorSearch.filter(sampleApps, ""))
        assertEquals(sampleApps, GroupEditorSearch.filter(sampleApps, "   "))
    }

    @Test
    fun filtersByAppName() {
        val result = GroupEditorSearch.filter(sampleApps, "sig")
        assertEquals(1, result.size)
        assertEquals("Signal", result[0].appName)
    }

    @Test
    fun filtersByPackageName() {
        val result = GroupEditorSearch.filter(sampleApps, "telegram")
        assertEquals(1, result.size)
        assertEquals("Telegram", result[0].appName)
    }
}
