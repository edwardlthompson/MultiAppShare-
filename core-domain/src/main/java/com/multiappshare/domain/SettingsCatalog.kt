package com.multiappshare.domain

enum class SettingsSection {
    APPEARANCE,
    SHARING_BEHAVIOR,
    DATA_MANAGEMENT,
    ABOUT,
}

data class SettingsEntry(
    val id: String,
    val title: String,
    val section: SettingsSection,
)

object SettingsCatalog {
    val ENTRIES = listOf(
        SettingsEntry("theme", "Theme & Display", SettingsSection.APPEARANCE),
        SettingsEntry("language", "Language", SettingsSection.APPEARANCE),
        SettingsEntry("delay", "Sharing Delay", SettingsSection.SHARING_BEHAVIOR),
        SettingsEntry("haptics", "Haptic Feedback", SettingsSection.SHARING_BEHAVIOR),
        SettingsEntry("backup", "Backup & Restore", SettingsSection.DATA_MANAGEMENT),
        SettingsEntry("history", "History Retention", SettingsSection.DATA_MANAGEMENT),
        SettingsEntry("about", "About & FOSS Licenses", SettingsSection.ABOUT),
        SettingsEntry("feedback", "Send Feedback", SettingsSection.ABOUT),
    )

    fun filterBySection(section: SettingsSection): List<SettingsEntry> =
        ENTRIES.filter { it.section == section }
}
