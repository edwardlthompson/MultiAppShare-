package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OfflineChangelogCatalogTest {

    @Test
    fun containsOfflineChangelogEntries() {
        val entries = OfflineChangelogCatalog.ENTRIES
        assertTrue(entries.isNotEmpty())
        val latest = OfflineChangelogCatalog.getLatestVersion()
        assertEquals("v1.0.0", latest)
        assertTrue(entries[0].highlights.isNotEmpty())
    }
}
