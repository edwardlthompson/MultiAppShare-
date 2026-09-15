package com.multiappshare.ui.settings

/** Glance labels for preference rows (resolved strings from the host). */
data class SettingsSummaryState(
    val themeLabel: String,
    val languageLabel: String,
    val delayLabel: String,
)

internal val SETTINGS_TOGGLE_IDS = setOf("dynamic_color", "refresh", "haptics", "crash")

internal val SETTINGS_DETAIL_IDS = setOf("backup", "battery")
