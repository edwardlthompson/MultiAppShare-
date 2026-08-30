package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HistoryUriPermissionTest {

    @Test
    fun allowsContentUriWithPersistedPermission() {
        val uri = "content://media/external/images/media/123"
        val persisted = setOf(uri)
        assertTrue(HistoryUriPermission.canOpenUri(uri, persisted))
    }

    @Test
    fun rejectsContentUriWithoutPersistedPermission() {
        val uri = "content://media/external/images/media/123"
        val persisted = setOf("content://media/external/images/media/456")
        assertFalse(HistoryUriPermission.canOpenUri(uri, persisted))
    }

    @Test
    fun allowsWebUrisRegardlessOfContentGrants() {
        assertTrue(HistoryUriPermission.canOpenUri("https://example.com/item", emptySet()))
        assertTrue(HistoryUriPermission.canOpenUri("http://example.com/item", emptySet()))
    }

    @Test
    fun rejectsInvalidOrBlankUris() {
        assertFalse(HistoryUriPermission.canOpenUri(null, emptySet()))
        assertFalse(HistoryUriPermission.canOpenUri("", emptySet()))
        assertFalse(HistoryUriPermission.canOpenUri("invalid_uri", emptySet()))
    }
}
