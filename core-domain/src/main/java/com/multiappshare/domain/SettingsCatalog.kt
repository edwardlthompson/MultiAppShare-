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
        SettingsEntry("theme", "theme", SettingsSection.APPEARANCE),
        SettingsEntry("dynamic_color", "dynamic_color", SettingsSection.APPEARANCE),
        SettingsEntry("language", "language", SettingsSection.APPEARANCE),
        SettingsEntry("refresh", "refresh", SettingsSection.APPEARANCE),
        SettingsEntry("delay", "delay", SettingsSection.SHARING_BEHAVIOR),
        SettingsEntry("haptics", "haptics", SettingsSection.SHARING_BEHAVIOR),
        SettingsEntry("battery", "battery", SettingsSection.SHARING_BEHAVIOR),
        SettingsEntry("backup", "backup", SettingsSection.DATA_MANAGEMENT),
        SettingsEntry("history", "history", SettingsSection.DATA_MANAGEMENT),
        SettingsEntry("about", "about", SettingsSection.ABOUT),
        SettingsEntry("privacy", "privacy", SettingsSection.ABOUT),
        SettingsEntry("licenses", "licenses", SettingsSection.ABOUT),
        SettingsEntry("whats_new", "whats_new", SettingsSection.ABOUT),
        SettingsEntry("feedback", "feedback", SettingsSection.ABOUT),
        SettingsEntry("crash", "crash", SettingsSection.ABOUT),
    )

    fun filterBySection(section: SettingsSection): List<SettingsEntry> =
        ENTRIES.filter { it.section == section }

    fun search(query: String?, localizedTitles: Map<String, String> = emptyMap()): List<SettingsEntry> {
        val trimmed = query?.trim().orEmpty()
        if (trimmed.isEmpty()) return ENTRIES
        return ENTRIES.filter {
            it.title.contains(trimmed, ignoreCase = true) ||
                it.id.contains(trimmed, ignoreCase = true) ||
                it.section.name.contains(trimmed, ignoreCase = true) ||
                localizedTitles[it.id]?.contains(trimmed, ignoreCase = true) == true
        }
    }
}
