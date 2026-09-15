package com.multiappshare.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.multiappshare.R
import com.multiappshare.domain.SettingsEntry
import com.multiappshare.domain.SettingsSection
import com.multiappshare.ui.theme.Dimens

@Composable
internal fun SettingsEntryList(
    entries: List<SettingsEntry>,
    selectedId: String?,
    onSelect: (SettingsEntry) -> Unit,
    toggles: SettingsToggleState,
    summaries: SettingsSummaryState,
) {
    if (entries.isEmpty()) {
        Text(
            stringResource(R.string.settings_no_results),
            modifier = Modifier.padding(Dimens.Space4),
            style = MaterialTheme.typography.bodyLarge,
        )
        return
    }
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        SettingsSection.entries.forEach { section ->
            val sectionEntries = entries.filter { it.section == section }
            if (sectionEntries.isEmpty()) return@forEach
            Text(
                stringResource(SettingsCatalogUi.sectionLabel(section)),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(
                    start = Dimens.Space4,
                    end = Dimens.Space4,
                    top = Dimens.Space4,
                    bottom = Dimens.Space2,
                ),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Space4, vertical = Dimens.Space1)
                    .clip(RoundedCornerShape(Dimens.Space3))
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest),
            ) {
                sectionEntries.forEach { entry ->
                    val checked = toggleChecked(entry.id, toggles)
                    SettingsPreferenceRow(
                        title = stringResource(SettingsCatalogUi.entryTitle(entry)),
                        subtitle = settingsEntrySubtitle(entry.id, summaries),
                        icon = settingsEntryIcon(entry.id),
                        selected = entry.id == selectedId,
                        onClick = { onSelect(entry) },
                        checked = checked,
                        onCheckedChange = if (checked != null) {
                            { value -> toggleSetter(entry.id, toggles)?.invoke(value) }
                        } else {
                            null
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(Dimens.Space2))
        }
    }
}

@Composable
private fun settingsEntrySubtitle(id: String, summaries: SettingsSummaryState): String = when (id) {
    "theme" -> summaries.themeLabel
    "language" -> summaries.languageLabel
    "delay" -> summaries.delayLabel
    else -> stringResource(SettingsCatalogUi.entrySubtitle(id))
}

private fun toggleChecked(id: String, toggles: SettingsToggleState): Boolean? = when (id) {
    "dynamic_color" -> toggles.dynamicColor
    "refresh" -> toggles.highRefresh
    "haptics" -> toggles.shareHaptics
    "crash" -> toggles.crashCapture
    else -> null
}

private fun toggleSetter(id: String, toggles: SettingsToggleState): ((Boolean) -> Unit)? = when (id) {
    "dynamic_color" -> toggles.onDynamicColor
    "refresh" -> toggles.onHighRefresh
    "haptics" -> toggles.onShareHaptics
    "crash" -> toggles.onCrashCapture
    else -> null
}
