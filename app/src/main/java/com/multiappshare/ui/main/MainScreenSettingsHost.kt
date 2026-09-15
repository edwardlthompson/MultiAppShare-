package com.multiappshare.ui.main

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.multiappshare.MainViewModel
import com.multiappshare.R
import com.multiappshare.crashcapture.CrashStore
import com.multiappshare.domain.AppLanguageTags
import com.multiappshare.domain.SharingDelay
import com.multiappshare.locale.AppLanguage
import com.multiappshare.sharehaptics.ShareHapticsViewModel
import com.multiappshare.ui.feedback.FeedbackDialog
import com.multiappshare.ui.settings.LanguageDialog
import com.multiappshare.ui.settings.SettingsScreen
import com.multiappshare.ui.settings.SettingsScreenActions
import com.multiappshare.ui.settings.SettingsSummaryState
import com.multiappshare.ui.settings.SettingsToggleState
import com.multiappshare.ui.settings.SharingDelayDialog
import com.multiappshare.ui.settings.ThemeDialog

@Composable
internal fun MainScreenSettingsHost(
    viewModel: MainViewModel,
    showLanguage: Boolean,
    onShowLanguage: (Boolean) -> Unit,
    showTheme: Boolean,
    onShowTheme: (Boolean) -> Unit,
    showDelay: Boolean,
    onShowDelay: (Boolean) -> Unit,
    showSettings: Boolean,
    onShowSettings: (Boolean) -> Unit,
    onExport: () -> Unit,
    onImport: () -> Unit,
    onHistory: () -> Unit,
    onAbout: () -> Unit,
) {
    var showFeedback by remember { mutableStateOf(false) }
    var showPrivacy by remember { mutableStateOf(false) }
    var showLicenses by remember { mutableStateOf(false) }
    var showWhatsNew by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val crashOn by viewModel.appearance.crashCaptureEnabled.collectAsState(initial = false)
    val refreshOn by viewModel.appearance.highRefreshEnabled.collectAsState(initial = true)
    val dynamicColorOn by viewModel.appearance.dynamicColorEnabled.collectAsState(initial = true)
    val hapticsVm: ShareHapticsViewModel = hiltViewModel()
    val hapticsOn by hapticsVm.enabled.collectAsState(initial = true)
    val darkPref by viewModel.appearance.darkTheme.collectAsState(initial = null)
    val themeLabel = when (darkPref) {
        null -> stringResource(R.string.theme_system)
        false -> stringResource(R.string.theme_light)
        true -> stringResource(R.string.theme_dark)
    }
    val languageTag = AppLanguageTags.sanitize(AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag())
    val languageLabel = when (languageTag) {
        null -> stringResource(R.string.language_system)
        "en" -> stringResource(R.string.language_english)
        "de" -> stringResource(R.string.language_german)
        "fr" -> stringResource(R.string.language_french)
        "es" -> stringResource(R.string.language_spanish)
        "it" -> stringResource(R.string.language_italian)
        else -> stringResource(R.string.language_system)
    }
    val delayMs = viewModel.sharingDelayMs
    val delayLabel = when (SharingDelay.presetCopy(delayMs)) {
        SharingDelay.PresetCopy.NONE -> stringResource(R.string.delay_off)
        SharingDelay.PresetCopy.SHORT -> stringResource(R.string.delay_seconds_half)
        SharingDelay.PresetCopy.ONE -> stringResource(R.string.delay_seconds_one)
        SharingDelay.PresetCopy.MANY -> stringResource(
            R.string.delay_seconds_n,
            SharingDelay.wholeSeconds(delayMs),
        )
    }
    val summaries = SettingsSummaryState(
        themeLabel = themeLabel,
        languageLabel = languageLabel,
        delayLabel = delayLabel,
    )
    val toggles = SettingsToggleState(
        crashCapture = crashOn,
        highRefresh = refreshOn,
        shareHaptics = hapticsOn,
        dynamicColor = dynamicColorOn,
        onCrashCapture = { enabled ->
            viewModel.setCrashCaptureEnabled(enabled)
            CrashStore.setEnabled(context, enabled)
        },
        onHighRefresh = { viewModel.setHighRefreshEnabled(it) },
        onShareHaptics = { hapticsVm.setEnabled(it) },
        onDynamicColor = { viewModel.setDynamicColorEnabled(it) },
    )
    if (showSettings) {
        Dialog(
            onDismissRequest = { onShowSettings(false) },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                SettingsScreen(
                    actions = SettingsScreenActions(
                        onBack = { onShowSettings(false) },
                        onTheme = { onShowTheme(true) },
                        onLanguage = { onShowLanguage(true) },
                        onSharingDelay = { onShowDelay(true) },
                        onExport = {
                            onShowSettings(false)
                            onExport()
                        },
                        onImport = {
                            onShowSettings(false)
                            onImport()
                        },
                        onHistory = onHistory,
                        onAbout = onAbout,
                        onFeedback = { showFeedback = true },
                        onPrivacy = { showPrivacy = true },
                        onLicenses = { showLicenses = true },
                        onWhatsNew = { showWhatsNew = true },
                    ),
                    toggles = toggles,
                    summaries = summaries,
                )
            }
        }
    }
    if (showFeedback) {
        FeedbackDialog(crashText = null, onDismiss = { showFeedback = false })
    }
    if (showPrivacy) {
        com.multiappshare.ui.about.PrivacyPolicyDialog(onDismiss = { showPrivacy = false })
    }
    if (showLicenses) {
        com.multiappshare.ui.about.OssLicensesDialog(onDismiss = { showLicenses = false })
    }
    if (showWhatsNew) {
        com.multiappshare.ui.about.WhatsNewDialog(onDismiss = { showWhatsNew = false })
    }
    if (showLanguage) {
        val current = AppLanguageTags.sanitize(AppCompatDelegate.getApplicationLocales()[0]?.toLanguageTag())
        LanguageDialog(
            selectedTag = current,
            onDismiss = { onShowLanguage(false) },
            onConfirm = { tag ->
                viewModel.setAppLanguage(tag)
                AppLanguage.apply(tag)
                onShowLanguage(false)
            },
        )
    }
    if (showTheme) {
        val selected by viewModel.appearance.darkTheme.collectAsState(initial = null)
        ThemeDialog(
            selected = selected,
            onDismiss = { onShowTheme(false) },
            onConfirm = { enabled ->
                viewModel.setDarkTheme(enabled)
                onShowTheme(false)
            },
        )
    }
    if (showDelay) {
        SharingDelayDialog(
            selectedMs = viewModel.sharingDelayMs,
            onDismiss = { onShowDelay(false) },
            onConfirm = { ms ->
                viewModel.setSharingDelay(ms)
                onShowDelay(false)
            },
        )
    }
}
