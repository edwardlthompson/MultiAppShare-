package com.multiappshare.ui.settings

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import com.multiappshare.R
import com.multiappshare.domain.DesktopWindowingHelper
import com.multiappshare.domain.SettingsCatalog
import com.multiappshare.domain.SettingsEntry
import com.multiappshare.domain.WindowSizeClass
import com.multiappshare.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    actions: SettingsScreenActions,
    toggles: SettingsToggleState,
    summaries: SettingsSummaryState,
) {
    var search by remember { mutableStateOf("") }
    var selectedId by remember { mutableStateOf<String?>(null) }
    val localized = SettingsCatalog.ENTRIES.associate { it.id to stringResource(SettingsCatalogUi.entryTitle(it)) }
    val entries = SettingsCatalog.search(search, localized)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.menu_settings), modifier = Modifier.semantics { heading() }) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (selectedId in SETTINGS_DETAIL_IDS) {
                                selectedId = null
                            } else {
                                actions.onBack()
                            }
                        },
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_settings_back),
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val widthDp = maxWidth.value.toInt()
                val twoPane = DesktopWindowingHelper.classifyWindowSize(widthDp) != WindowSizeClass.COMPACT
                val onSelect: (SettingsEntry) -> Unit = { entry ->
                    when {
                        entry.id in SETTINGS_TOGGLE_IDS -> Unit
                        entry.id in SETTINGS_DETAIL_IDS -> selectedId = entry.id
                        !twoPane -> invokeSettingsEntry(entry, actions)
                        else -> selectedId = entry.id
                    }
                }
                val showingDetailPush = !twoPane && selectedId in SETTINGS_DETAIL_IDS
                if (showingDetailPush) {
                    SettingsDetailPane(settingsEntryOrNull(selectedId), actions)
                } else if (!twoPane) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        SettingsSearchField(search) { search = it }
                        SettingsEntryList(entries, selectedId, onSelect, toggles, summaries)
                    }
                } else {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            SettingsSearchField(search) { search = it }
                            SettingsEntryList(entries, selectedId, onSelect, toggles, summaries)
                        }
                        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                            SettingsDetailPane(settingsEntryOrNull(selectedId), actions)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSearchField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Space4, vertical = Dimens.Space2),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        label = { Text(stringResource(R.string.settings_search_hint)) },
    )
}
