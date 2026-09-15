package com.multiappshare.ui.settings

import androidx.annotation.StringRes
import com.multiappshare.R
import com.multiappshare.domain.SettingsEntry
import com.multiappshare.domain.SettingsSection

object SettingsCatalogUi {
    private val ENTRY_TITLES = mapOf(
        "theme" to R.string.menu_theme,
        "dynamic_color" to R.string.settings_dynamic_color,
        "language" to R.string.menu_language,
        "refresh" to R.string.display_high_refresh,
        "delay" to R.string.menu_sharing_delay,
        "haptics" to R.string.settings_share_haptics,
        "backup" to R.string.settings_entry_backup,
        "history" to R.string.menu_history,
        "about" to R.string.menu_about,
        "feedback" to R.string.feedback_title,
        "crash" to R.string.settings_crash_capture,
        "battery" to R.string.settings_battery,
        "privacy" to R.string.settings_privacy,
        "licenses" to R.string.settings_licenses,
        "whats_new" to R.string.settings_whats_new,
    )

    @StringRes
    fun sectionLabel(section: SettingsSection): Int = when (section) {
        SettingsSection.APPEARANCE -> R.string.settings_section_appearance
        SettingsSection.SHARING_BEHAVIOR -> R.string.settings_section_sharing
        SettingsSection.DATA_MANAGEMENT -> R.string.settings_section_data
        SettingsSection.ABOUT -> R.string.settings_section_about
    }

    @StringRes
    fun entryTitle(entry: SettingsEntry): Int = ENTRY_TITLES[entry.id] ?: R.string.menu_settings

    private val ENTRY_SUBTITLES = mapOf(
        "dynamic_color" to R.string.settings_sub_dynamic_color,
        "refresh" to R.string.settings_sub_refresh,
        "haptics" to R.string.settings_sub_haptics,
        "crash" to R.string.settings_sub_crash,
        "backup" to R.string.settings_sub_backup,
        "battery" to R.string.settings_sub_battery,
        "history" to R.string.settings_sub_history,
        "about" to R.string.settings_sub_about,
        "privacy" to R.string.settings_sub_privacy,
        "licenses" to R.string.settings_sub_licenses,
        "whats_new" to R.string.settings_sub_whats_new,
        "feedback" to R.string.settings_sub_feedback,
    )

    @StringRes
    fun entrySubtitle(id: String): Int = ENTRY_SUBTITLES[id] ?: R.string.settings_tablet_select
}
