package com.multiappshare.ui.main

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.multiappshare.MainViewModel
import com.multiappshare.crashcapture.CrashStore
import com.multiappshare.domain.AppLanguageTags
import com.multiappshare.locale.AppLanguage
import com.multiappshare.ui.settings.LanguageDialog
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
) {
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
        val selected by viewModel.darkTheme.collectAsState(initial = null)
        val crashOn by viewModel.crashCaptureEnabled.collectAsState(initial = false)
        val refreshOn by viewModel.highRefreshEnabled.collectAsState(initial = true)
        val context = LocalContext.current
        ThemeDialog(
            selected = selected,
            crashCapture = crashOn,
            highRefresh = refreshOn,
            onDismiss = { onShowTheme(false) },
            onConfirm = { enabled, crash, refresh ->
                viewModel.setDarkTheme(enabled)
                viewModel.setCrashCaptureEnabled(crash)
                viewModel.setHighRefreshEnabled(refresh)
                CrashStore.setEnabled(context, crash)
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
