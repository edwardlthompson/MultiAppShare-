package com.multiappshare

import com.multiappshare.domain.SettingsRepository

internal class MainViewModelAppearance(settingsRepository: SettingsRepository) {
    val darkTheme = settingsRepository.isDarkThemeEnabled
    val crashCaptureEnabled = settingsRepository.isCrashCaptureEnabled
    val highRefreshEnabled = settingsRepository.isHighRefreshEnabled
    val dynamicColorEnabled = settingsRepository.isDynamicColorEnabled
    val pinnedGroupNames = settingsRepository.pinnedGroupNames
}
