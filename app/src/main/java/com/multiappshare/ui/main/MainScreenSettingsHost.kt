package com.multiappshare.ui.main

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.multiappshare.MainViewModel
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
