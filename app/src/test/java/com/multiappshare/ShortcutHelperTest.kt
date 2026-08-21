package com.multiappshare

import com.multiappshare.model.AppGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShortcutHelperTest {

    @Test
    fun groupUri_includesIdAndName() {
        val group = AppGroup(name = "Social", apps = emptyList(), id = "abc-123")
        val uri = ShortcutHelper.groupUri(group)
        assertEquals(DeeplinkContract.SCHEME, uri.scheme)
        assertEquals(DeeplinkContract.HOST_GROUP, uri.host)
        assertEquals("abc-123", uri.getQueryParameter(DeeplinkContract.QUERY_GROUP_ID))
        assertEquals("Social", uri.getQueryParameter(DeeplinkContract.QUERY_GROUP_NAME))
        assertEquals("abc-123", ShortcutHelper.shortcutId(group))
    }

    @Test
    fun shortcutId_fallsBackToNameWhenBlank() {
        val group = AppGroup(name = "Work", apps = emptyList(), id = "")
        assertEquals("Work", ShortcutHelper.shortcutId(group))
        assertTrue(ShortcutHelper.groupUri(group).getQueryParameter(DeeplinkContract.QUERY_GROUP_ID) == null)
    }
}
