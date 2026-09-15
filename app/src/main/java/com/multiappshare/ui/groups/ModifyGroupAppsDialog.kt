package com.multiappshare.ui.groups

import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.multiappshare.R
import com.multiappshare.core.ui.highRefreshScroll
import com.multiappshare.domain.FormFieldPolicy
import com.multiappshare.domain.GroupEditorSearch
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import com.multiappshare.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyGroupAppsDialog(
    allApps: List<AppInfo>,
    group: AppGroup,
    onDismiss: () -> Unit,
    onSaveApps: (List<AppInfo>) -> Unit,
    onRefresh: () -> Unit,
    @Suppress("UNUSED_PARAMETER") packageManager: PackageManager,
) {
    val selectedApps = remember(group.name) { mutableStateListOf<AppInfo>().apply { addAll(group.apps) } }
    var searchQuery by remember(group.name) { mutableStateOf("") }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(R.string.modify_group_title, group.name)) },
                        navigationIcon = {
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.cd_settings_back),
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = onRefresh) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = stringResource(R.string.cd_refresh_app_list),
                                )
                            }
                            TextButton(onClick = { onSaveApps(selectedApps.toList()) }) {
                                Text(stringResource(R.string.button_save))
                            }
                        },
                    )
                },
            ) { padding ->
                Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
                    if (selectedApps.isNotEmpty()) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).highRefreshScroll(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            items(selectedApps) { app ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(GroupEditorSearch.chipLabel(app.appName))
                                    IconButton(onClick = { selectedApps.remove(app) }) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = stringResource(
                                                R.string.cd_remove_selected_app,
                                                GroupEditorSearch.chipLabel(app.appName),
                                            ),
                                        )
                                    }
                                }
                            }
                        }
                    }
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text(stringResource(R.string.label_search_apps)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    )
                    val filteredApps = GroupEditorSearch.filter(allApps, searchQuery)
                    TextButton(
                        onClick = {
                            val next = GroupEditorSearch.withAllVisible(selectedApps.toList(), filteredApps)
                            selectedApps.clear()
                            selectedApps.addAll(next)
                        },
                    ) {
                        Text(stringResource(R.string.button_select_all))
                    }
                    if (GroupEditorSearch.showEmptySearch(searchQuery, filteredApps.size)) {
                        Text(
                            stringResource(R.string.no_apps_match_search),
                            modifier = Modifier.padding(vertical = Dimens.Space2),
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize().highRefreshScroll()) {
                            items(filteredApps) { app ->
                                val isSelected = GroupEditorSearch.isSelected(selectedApps, app)
                                val toggle: () -> Unit = {
                                    val next = GroupEditorSearch.withToggled(selectedApps.toList(), app)
                                    selectedApps.clear()
                                    selectedApps.addAll(next)
                                    Unit
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = FormFieldPolicy.MIN_ROW_TOUCH_DP.dp)
                                        .clickable(onClick = toggle)
                                        .padding(horizontal = Dimens.Space2),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    AsyncImage(
                                        model = app,
                                        contentDescription = null,
                                        modifier = Modifier.size(Dimens.Avatar),
                                    )
                                    Spacer(modifier = Modifier.width(Dimens.Space2))
                                    Text(text = app.appName, modifier = Modifier.weight(1f))
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = { checked ->
                                            if (checked != isSelected) toggle()
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
