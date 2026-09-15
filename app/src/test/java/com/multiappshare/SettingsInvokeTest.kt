package com.multiappshare

import com.multiappshare.domain.SettingsCatalog
import com.multiappshare.ui.settings.SettingsScreenActions
import com.multiappshare.ui.settings.invokeSettingsEntry
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsInvokeTest {

    @Test
    fun routesCatalogIdsToActions() {
        val hits = mutableListOf<String>()
        val actions = SettingsScreenActions(
            onBack = { hits += "back" },
            onTheme = { hits += "theme" },
            onLanguage = { hits += "language" },
            onSharingDelay = { hits += "delay" },
            onExport = { hits += "export" },
            onImport = { hits += "import" },
            onHistory = { hits += "history" },
            onAbout = { hits += "about" },
            onFeedback = { hits += "feedback" },
            onPrivacy = { hits += "privacy" },
            onLicenses = { hits += "licenses" },
            onWhatsNew = { hits += "whats_new" },
        )
        SettingsCatalog.ENTRIES.forEach { invokeSettingsEntry(it, actions) }
        assertEquals(
            listOf(
                "theme",
                "language",
                "delay",
                "export",
                "history",
                "about",
                "privacy",
                "licenses",
                "whats_new",
                "feedback",
            ),
            hits,
        )
    }
}
