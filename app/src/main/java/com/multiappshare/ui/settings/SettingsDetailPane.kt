package com.multiappshare.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.multiappshare.R
import com.multiappshare.domain.BatteryOptimizationExplainer
import com.multiappshare.domain.SettingsCatalog
import com.multiappshare.domain.SettingsEntry
import com.multiappshare.ui.launch.rememberStartIntent
import com.multiappshare.ui.theme.Dimens

@Composable
internal fun SettingsDetailPane(
    selected: SettingsEntry?,
    actions: SettingsScreenActions,
) {
    val openSettings = rememberStartIntent()
    Column(modifier = Modifier.padding(Dimens.Space4)) {
        if (selected == null) {
            Text(stringResource(R.string.settings_tablet_select), style = MaterialTheme.typography.bodyLarge)
            return
        }
        Text(stringResource(SettingsCatalogUi.entryTitle(selected)), style = MaterialTheme.typography.headlineSmall)
        when (selected.id) {
            "backup" -> {
                Button(onClick = actions.onExport, modifier = Modifier.padding(top = Dimens.Space3)) {
                    Text(stringResource(R.string.menu_export_groups))
                }
                OutlinedButton(onClick = actions.onImport) {
                    Text(stringResource(R.string.menu_import_groups))
                }
            }
            "battery" -> {
                Text(
                    stringResource(R.string.settings_battery_body),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = Dimens.Space2),
                )
                Button(
                    onClick = {
                        openSettings(
                            android.content.Intent(BatteryOptimizationExplainer.SETTINGS_INTENT_ACTION),
                        )
                    },
                    modifier = Modifier.padding(top = Dimens.Space3),
                ) {
                    Text(stringResource(R.string.settings_battery_open))
                }
            }
            else -> {
                Button(
                    onClick = { invokeSettingsEntry(selected, actions) },
                    modifier = Modifier.padding(top = Dimens.Space3),
                ) {
                    Text(stringResource(R.string.button_ok))
                }
            }
        }
    }
}

internal fun invokeSettingsEntry(entry: SettingsEntry, actions: SettingsScreenActions) {
    when (entry.id) {
        "theme" -> actions.onTheme()
        "language" -> actions.onLanguage()
        "delay" -> actions.onSharingDelay()
        "backup" -> actions.onExport()
        "history" -> actions.onHistory()
        "about" -> actions.onAbout()
        "feedback" -> actions.onFeedback()
        "privacy" -> actions.onPrivacy()
        "licenses" -> actions.onLicenses()
        "whats_new" -> actions.onWhatsNew()
        else -> Unit
    }
}

internal fun settingsEntryOrNull(id: String?): SettingsEntry? =
    SettingsCatalog.ENTRIES.firstOrNull { it.id == id }
