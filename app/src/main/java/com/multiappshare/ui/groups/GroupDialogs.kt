package com.multiappshare.ui.groups

import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.multiappshare.R
import com.multiappshare.core.ui.highRefreshScroll
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo

@Composable
fun CreateGroupDialog(onDismiss: () -> Unit, onCreateGroup: (String) -> Unit) {
    var groupName by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_create_group_title)) },
        text = {
            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text(stringResource(R.string.label_group_name)) },
                singleLine = true,
            )
        },
        confirmButton = {
            Button(onClick = { if (groupName.isNotBlank()) onCreateGroup(groupName) }) {
                Text(stringResource(R.string.button_create))
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) } },
    )
}

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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.modify_group_title, group.name), modifier = Modifier.weight(1f))
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.cd_refresh_app_list))
                }
            }
        },
        text = {
            Column {
                if (selectedApps.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).highRefreshScroll(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        items(selectedApps) { app ->
                            AssistChip(
                                onClick = { selectedApps.remove(app) },
                                label = { Text(app.appName.split(" - ").first()) },
                                leadingIcon = { Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp)) },
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(stringResource(R.string.label_search_apps)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.height(300.dp).highRefreshScroll()) {
                    val filteredApps = allApps.filter { it.appName.contains(searchQuery, ignoreCase = true) }
                    items(filteredApps) { app ->
                        val isSelected = selectedApps.any {
                            it.packageName == app.packageName && it.activityName == app.activityName
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable {
                                if (isSelected) {
                                    selectedApps.removeAll {
                                        it.packageName == app.packageName && it.activityName == app.activityName
                                    }
                                } else {
                                    selectedApps.add(app)
                                }
                            }.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AsyncImage(model = app, contentDescription = null, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = app.appName, modifier = Modifier.weight(1f))
                            Checkbox(checked = isSelected, onCheckedChange = null)
                        }
                    }
                }
            }
        },
        confirmButton = { Button(onClick = { onSaveApps(selectedApps.toList()) }) { Text(stringResource(R.string.button_save)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) } },
    )
}
