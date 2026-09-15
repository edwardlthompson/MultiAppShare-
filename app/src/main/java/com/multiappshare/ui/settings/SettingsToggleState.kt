package com.multiappshare.ui.settings

data class SettingsToggleState(
    val crashCapture: Boolean,
    val highRefresh: Boolean,
    val shareHaptics: Boolean,
    val dynamicColor: Boolean,
    val onCrashCapture: (Boolean) -> Unit,
    val onHighRefresh: (Boolean) -> Unit,
    val onShareHaptics: (Boolean) -> Unit,
    val onDynamicColor: (Boolean) -> Unit,
)
