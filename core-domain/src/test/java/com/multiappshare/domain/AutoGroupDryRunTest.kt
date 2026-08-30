package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class AutoGroupDryRunTest {

    private val telegram = AppInfo(packageName = "org.telegram.messenger", activityName = "Main", appName = "Telegram")
    private val gmail = AppInfo(packageName = "com.google.android.gm", activityName = "Main", appName = "Gmail")

    @Test
    fun previewsAutoGroupBucketsWithoutModifyingSource() {
        val apps = listOf(telegram, gmail)
        val preview = AutoGroupDryRun.preview(
            allApps = apps,
            existingGroups = emptyList(),
            append = false,
        )

        assertEquals(2, preview.size)
        val messaging = preview.find { it.name == "Messaging" }
        val email = preview.find { it.name == "Email" }

        assertEquals(1, messaging?.apps?.size)
        assertEquals(1, email?.apps?.size)
    }

    @Test
    fun previewsMergedGroupsWhenAppendIsTrue() {
        val existing = AppGroup(name = "Messaging", apps = listOf(telegram))
        val signal = AppInfo(packageName = "org.signal", activityName = "Main", appName = "Signal Chat")

        val preview = AutoGroupDryRun.preview(
            allApps = listOf(signal),
            existingGroups = listOf(existing),
            append = true,
        )

        assertEquals(1, preview.size)
        assertEquals(2, preview[0].apps.size)
    }
}
