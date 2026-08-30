package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GroupTemplateCodecTest {

    private val sampleApp = AppInfo(packageName = "org.signal", activityName = "Main", appName = "Signal")
    private val sampleGroup = AppGroup(
        name = "Secure Chat",
        apps = listOf(sampleApp),
        usageCount = 42,
        id = "group-123",
    )

    @Test
    fun exportsGroupTemplateAndZeroesUsageCount() {
        val json = GroupTemplateCodec.exportTemplate(sampleGroup)
        assertTrue(json.contains("Secure Chat"))
        assertTrue(json.contains("org.signal"))

        val importedResult = GroupTemplateCodec.importTemplate(json)
        assertTrue(importedResult.isSuccess)
        val imported = importedResult.getOrThrow()
        assertEquals("Secure Chat", imported.name)
        assertEquals(0, imported.usageCount)
        assertEquals(1, imported.apps.size)
        assertEquals("org.signal", imported.apps[0].packageName)
    }

    @Test
    fun failsGracefullyOnInvalidJson() {
        val invalid = "{ invalid json }"
        val result = GroupTemplateCodec.importTemplate(invalid)
        assertTrue(result.isFailure)
    }
}
