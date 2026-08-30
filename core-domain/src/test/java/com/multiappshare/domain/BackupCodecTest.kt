package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BackupCodecTest {

    @Test
    fun encodeV2_includesSettingsAndLastPayload() {
        val groups = listOf(AppGroup(name = "A", apps = emptyList(), id = "gid"))
        val settings = BackupSettings(
            darkTheme = true,
            appLanguage = "fr",
            sharingDelay = 250,
            crashCaptureEnabled = true,
        )
        val last = ShareSessionSnapshot(text = "hi", mimeType = "text/plain")
        val json = BackupCodec.encode(groups, settings, last)
        val parsed = BackupCodec.parse(json)
        assertEquals(2, parsed.version)
        assertEquals("gid", parsed.groups.single().id)
        assertEquals(true, parsed.settings?.darkTheme)
        assertEquals("fr", parsed.settings?.appLanguage)
        assertEquals(250, parsed.settings?.sharingDelay)
        assertEquals(true, parsed.settings?.crashCaptureEnabled)
        assertEquals("hi", parsed.lastPayload?.text)
    }

    @Test
    fun parseV1Wrapper_leavesSettingsNull() {
        val json = """{"version":1,"groups":[{"name":"Old","apps":[],"isExpanded":false,"usageCount":0}]}"""
        val parsed = BackupCodec.parse(json)
        assertEquals(1, parsed.version)
        assertEquals("Old", parsed.groups.single().name)
        assertNull(parsed.settings)
        assertNull(parsed.lastPayload)
    }
}
