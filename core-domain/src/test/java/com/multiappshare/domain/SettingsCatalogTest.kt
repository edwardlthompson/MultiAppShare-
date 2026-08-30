package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsCatalogTest {

    @Test
    fun containsStructuredSettingsEntries() {
        val entries = SettingsCatalog.ENTRIES
        assertTrue(entries.isNotEmpty())
        for (entry in entries) {
            assertTrue(entry.id.isNotBlank())
            assertTrue(entry.title.isNotBlank())
        }
    }

    @Test
    fun filtersEntriesBySection() {
        val appearance = SettingsCatalog.filterBySection(SettingsSection.APPEARANCE)
        assertEquals(2, appearance.size)
        assertTrue(appearance.any { it.id == "theme" })
        assertTrue(appearance.any { it.id == "language" })
    }

    @Test
    fun searchesEntriesByQuery() {
        val results = SettingsCatalog.search("haptic")
        assertEquals(1, results.size)
        assertEquals("haptics", results[0].id)

        val empty = SettingsCatalog.search(null)
        assertEquals(SettingsCatalog.ENTRIES.size, empty.size)
    }
}
