package com.multiappshare

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.multiappshare.ui.settings.SettingsScreen
import com.multiappshare.ui.settings.SettingsScreenActions
import com.multiappshare.ui.settings.SettingsSummaryState
import com.multiappshare.ui.settings.SettingsToggleState
import com.multiappshare.ui.theme.MultiAppShareTheme
import org.junit.Rule
import org.junit.Test

class SettingsScreenPaparazziTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    private val actions = SettingsScreenActions(
        onBack = {},
        onTheme = {},
        onLanguage = {},
        onSharingDelay = {},
        onExport = {},
        onImport = {},
        onHistory = {},
        onAbout = {},
        onFeedback = {},
    )

    private val toggles = SettingsToggleState(
        crashCapture = false,
        highRefresh = true,
        shareHaptics = true,
        dynamicColor = true,
        onCrashCapture = {},
        onHighRefresh = {},
        onShareHaptics = {},
        onDynamicColor = {},
    )

    private val summaries = SettingsSummaryState(
        themeLabel = "System default",
        languageLabel = "System default",
        delayLabel = "Short (0.5s)",
    )

    @Test
    fun settingsScreen_light() {
        paparazzi.snapshot {
            MultiAppShareTheme(darkTheme = false, dynamicColor = false) {
                SettingsScreen(actions = actions, toggles = toggles, summaries = summaries)
            }
        }
    }

    @Test
    fun settingsScreen_dark() {
        paparazzi.snapshot {
            MultiAppShareTheme(darkTheme = true, dynamicColor = false) {
                SettingsScreen(actions = actions, toggles = toggles, summaries = summaries)
            }
        }
    }
}
