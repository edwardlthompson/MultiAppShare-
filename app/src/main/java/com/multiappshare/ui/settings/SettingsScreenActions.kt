package com.multiappshare.ui.settings

data class SettingsScreenActions(
    val onBack: () -> Unit,
    val onTheme: () -> Unit,
    val onLanguage: () -> Unit,
    val onSharingDelay: () -> Unit,
    val onExport: () -> Unit,
    val onImport: () -> Unit,
    val onHistory: () -> Unit,
    val onAbout: () -> Unit,
    val onFeedback: () -> Unit,
    val onPrivacy: () -> Unit = {},
    val onLicenses: () -> Unit = {},
    val onWhatsNew: () -> Unit = {},
)
