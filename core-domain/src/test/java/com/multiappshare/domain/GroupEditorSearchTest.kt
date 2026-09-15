package com.multiappshare.domain

import com.multiappshare.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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

    @Test
    fun toggleAddsAndRemovesTheSameActivity() {
        val signal = sampleApps[0]
        val added = GroupEditorSearch.withToggled(emptyList(), signal)
        assertTrue(GroupEditorSearch.isSelected(added, signal))
        val removed = GroupEditorSearch.withToggled(added, signal)
        assertFalse(GroupEditorSearch.isSelected(removed, signal))
    }

    @Test
    fun chipLabelKeepsTheFullAppName() {
        assertEquals("Photos - Google", GroupEditorSearch.chipLabel("Photos - Google"))
    }

    @Test
    fun selectAllAddsVisibleAppsOnce() {
        val selected = GroupEditorSearch.withAllVisible(listOf(sampleApps[0]), sampleApps)
        assertEquals(3, selected.size)
        val again = GroupEditorSearch.withAllVisible(selected, sampleApps)
        assertEquals(3, again.size)
    }

    @Test
    fun emptySearchShowsWhenQueryMisses() {
        assertTrue(GroupEditorSearch.showEmptySearch("zzz", 0))
        assertFalse(GroupEditorSearch.showEmptySearch("", 0))
        assertFalse(GroupEditorSearch.showEmptySearch("sig", 1))
    }
}
