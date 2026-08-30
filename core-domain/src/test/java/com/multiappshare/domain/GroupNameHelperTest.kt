package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GroupNameHelperTest {

    @Test
    fun uniqueCopyNameAppendsCopy() {
        val groups = listOf(AppGroup(name = "Social", apps = emptyList()))
        assertEquals("Social (copy)", GroupNameHelper.uniqueCopyName("Social", groups))
    }

    @Test
    fun uniqueCopyNameIncrementsWhenCopyExists() {
        val groups = listOf(
            AppGroup(name = "Social", apps = emptyList()),
            AppGroup(name = "Social (copy)", apps = emptyList()),
        )
        assertEquals("Social (copy 2)", GroupNameHelper.uniqueCopyName("Social", groups))
    }

    @Test
    fun isDuplicateExcludingIgnoresCurrentName() {
        val groups = listOf(AppGroup(name = "Social", apps = emptyList()))
        assertFalse(GroupNameHelper.isDuplicateExcluding("Social", groups, "Social"))
        assertTrue(GroupNameHelper.isDuplicateExcluding("Social", groups, "Work"))
    }

    @Test
    fun sharingDelayClamp() {
        assertEquals(0, SharingDelay.clamp(-10))
        assertEquals(5000, SharingDelay.clamp(9000))
        assertEquals(500, SharingDelay.clamp(500))
    }

    @Test
    fun sanitizeLanguageRejectsUnknown() {
        assertEquals("fr", AppLanguageTags.sanitize(" FR "))
        assertEquals("en", AppLanguageTags.sanitize("en-US"))
        assertEquals(null, AppLanguageTags.sanitize("ja"))
        assertTrue(AppLanguageTags.sanitize(null) == null)
    }
}
