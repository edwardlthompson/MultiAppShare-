package com.multiappshare.ui.groups

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.multiappshare.R
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo

@Composable
fun ReorderAppsDialog(
    group: AppGroup,
    onDismiss: () -> Unit,
    onSaveOrder: (List<AppInfo>) -> Unit,
) {
    val apps = remember(group.name) { mutableStateListOf<AppInfo>().apply { addAll(group.apps) } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_reorder_apps_title)) },
        text = {
            LazyColumn(modifier = Modifier.height(400.dp)) {
                itemsIndexed(apps, key = { _, app -> app.packageName + "/" + app.activityName }) { index, app ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            IconButton(
                                onClick = {
                                    if (index > 0) {
                                        val item = apps.removeAt(index)
                                        apps.add(index - 1, item)
                                    }
                                },
                                enabled = index > 0,
                                modifier = Modifier.size(36.dp),
                            ) {
                                Icon(Icons.Default.KeyboardArrowUp, contentDescription = stringResource(R.string.cd_move_up))
                            }
                            IconButton(
                                onClick = {
                                    if (index < apps.size - 1) {
                                        val item = apps.removeAt(index)
                                        apps.add(index + 1, item)
                                    }
                                },
                                enabled = index < apps.size - 1,
                                modifier = Modifier.size(36.dp),
                            ) {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = stringResource(R.string.cd_move_down))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = app.appName, modifier = Modifier.weight(1f))
                    }
                    if (index < apps.size - 1) HorizontalDivider()
                }
            }
        },
        confirmButton = { Button(onClick = { onSaveOrder(apps.toList()) }) { Text(stringResource(R.string.button_save_order)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) } },
    )
}

@Composable
fun SortGroupsDialog(
    groups: List<AppGroup>,
    onDismiss: () -> Unit,
    onSaveOrder: (List<AppGroup>) -> Unit,
) {
    val sortedGroups = remember { mutableStateListOf<AppGroup>().apply { addAll(groups) } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_sort_groups_title)) },
        text = {
            LazyColumn(modifier = Modifier.height(400.dp)) {
                itemsIndexed(sortedGroups, key = { _, group -> group.name }) { index, group ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            IconButton(
                                onClick = {
                                    if (index > 0) {
                                        val item = sortedGroups.removeAt(index)
                                        sortedGroups.add(index - 1, item)
                                    }
                                },
                                enabled = index > 0,
                                modifier = Modifier.size(36.dp),
                            ) {
                                Icon(Icons.Default.KeyboardArrowUp, contentDescription = stringResource(R.string.cd_move_up))
                            }
                            IconButton(
                                onClick = {
                                    if (index < sortedGroups.size - 1) {
                                        val item = sortedGroups.removeAt(index)
                                        sortedGroups.add(index + 1, item)
                                    }
                                },
                                enabled = index < sortedGroups.size - 1,
                                modifier = Modifier.size(36.dp),
                            ) {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = stringResource(R.string.cd_move_down))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = group.name, modifier = Modifier.weight(1f))
                    }
                    if (index < sortedGroups.size - 1) HorizontalDivider()
                }
            }
        },
        confirmButton = { Button(onClick = { onSaveOrder(sortedGroups.toList()) }) { Text(stringResource(R.string.button_save_order)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) } },
    )
}
