package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class GroupNamesClipboardFormatterTest {

    private val appA = AppInfo("Signal", "org.thoughtcrime.securesms", "MainActivity")
    private val appB = AppInfo("Telegram", "org.telegram.messenger", "MainActivity")

    @Test
    fun formatGroupApps_handlesEmptyAndNonEmpty() {
        val emptyGroup = AppGroup("Empty", emptyList())
        assertEquals("Empty: (no apps)", GroupNamesClipboardFormatter.formatGroupApps(emptyGroup))

        val socialGroup = AppGroup("Social", listOf(appA, appB))
        assertEquals("Social: Signal, Telegram", GroupNamesClipboardFormatter.formatGroupApps(socialGroup))
    }

    @Test
    fun formatAllGroups_formatsMultilineString() {
        val groups = listOf(
            AppGroup("Social", listOf(appA, appB)),
            AppGroup("Work", listOf(appA)),
        )
        val expected = "Social: Signal, Telegram\nWork: Signal"
        assertEquals(expected, GroupNamesClipboardFormatter.formatAllGroups(groups))
        assertEquals("", GroupNamesClipboardFormatter.formatAllGroups(emptyList()))
    }
}
