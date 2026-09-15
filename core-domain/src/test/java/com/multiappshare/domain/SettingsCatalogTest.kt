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
        assertEquals(4, appearance.size)
        assertTrue(appearance.any { it.id == "theme" })
        assertTrue(appearance.any { it.id == "dynamic_color" })
        assertTrue(appearance.any { it.id == "language" })
        assertTrue(appearance.any { it.id == "refresh" })
        assertTrue(SettingsCatalog.filterBySection(SettingsSection.SHARING_BEHAVIOR).isNotEmpty())
        assertTrue(SettingsCatalog.filterBySection(SettingsSection.DATA_MANAGEMENT).isNotEmpty())
        assertTrue(SettingsCatalog.filterBySection(SettingsSection.ABOUT).isNotEmpty())
    }

    @Test
    fun searchesEntriesByQuery() {
        val results = SettingsCatalog.search("haptics")
        assertEquals(1, results.size)
        assertEquals("haptics", results[0].id)

        val delay = SettingsCatalog.search("delay")
        assertTrue(delay.any { it.id == "delay" })

        val empty = SettingsCatalog.search(null)
        assertEquals(SettingsCatalog.ENTRIES.size, empty.size)
    }

    @Test
    fun includesGhostFeatureSettingsRows() {
        val ids = SettingsCatalog.ENTRIES.map { it.id }.toSet()
        assertTrue(ids.containsAll(setOf("battery", "privacy", "licenses", "whats_new")))
    }

    @Test
    fun searchMatchesLocalizedTitles() {
        val results = SettingsCatalog.search(
            "tema",
            mapOf("theme" to "Tema y pantalla"),
        )
        assertEquals(1, results.size)
        assertEquals("theme", results[0].id)
    }
}
