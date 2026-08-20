package com.multiappshare.ui.groups

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.multiappshare.R
import com.multiappshare.model.AppGroup

@Composable
fun GroupWorkspace(
    groups: List<AppGroup>,
    selectedGroup: AppGroup?,
    onSelectGroup: (AppGroup) -> Unit,
    onModifyClick: (AppGroup) -> Unit,
    onReorderClick: (AppGroup) -> Unit,
    onDuplicateClick: (AppGroup) -> Unit,
    onRenameClick: (AppGroup) -> Unit = {},
    onMergeClick: (AppGroup) -> Unit = {},
    onDeleteClick: (AppGroup) -> Unit,
    onToggleExpanded: (AppGroup) -> Unit,
    onGroupClick: (AppGroup) -> Unit,
    onAddShortcutClick: (AppGroup) -> Unit,
    inShareMode: Boolean,
    packageManager: PackageManager,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val twoPane = !inShareMode && maxWidth >= 600.dp
        if (!twoPane) {
            GroupList(
                groups = groups,
                selectedGroupName = null,
                onSelectClick = null,
                onModifyClick = onModifyClick,
                onReorderClick = onReorderClick,
                onDuplicateClick = onDuplicateClick,
                onRenameClick = onRenameClick,
                onMergeClick = onMergeClick,
                onDeleteClick = onDeleteClick,
                onToggleExpanded = onToggleExpanded,
                onGroupClick = onGroupClick,
                onAddShortcutClick = onAddShortcutClick,
                inShareMode = inShareMode,
                packageManager = packageManager,
            )
            return@BoxWithConstraints
        }
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                GroupList(
                    groups = groups,
                    selectedGroupName = selectedGroup?.name,
                    onSelectClick = onSelectGroup,
                    onModifyClick = onModifyClick,
                    onReorderClick = onReorderClick,
                    onDuplicateClick = onDuplicateClick,
                    onRenameClick = onRenameClick,
                    onMergeClick = onMergeClick,
                    onDeleteClick = onDeleteClick,
                    onToggleExpanded = onToggleExpanded,
                    onGroupClick = onGroupClick,
                    onAddShortcutClick = onAddShortcutClick,
                    inShareMode = false,
                    packageManager = packageManager,
                )
            }
            Column(modifier = Modifier.weight(1f).fillMaxHeight().padding(16.dp)) {
                val group = selectedGroup
                if (group == null) {
                    Text(stringResource(R.string.tablet_select_group), style = MaterialTheme.typography.bodyLarge)
                } else {
                    Text(group.name, style = MaterialTheme.typography.headlineSmall)
                    Text(
                        stringResource(R.string.tablet_group_app_count, group.apps.size),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Button(onClick = { onModifyClick(group) }, modifier = Modifier.padding(top = 12.dp)) {
                        Text(stringResource(R.string.menu_modify_apps))
                    }
                    OutlinedButton(onClick = { onReorderClick(group) }) {
                        Text(stringResource(R.string.menu_reorder_apps))
                    }
                    OutlinedButton(onClick = { onDuplicateClick(group) }) {
                        Text(stringResource(R.string.menu_duplicate_group))
                    }
                    OutlinedButton(onClick = { onRenameClick(group) }) {
                        Text(stringResource(R.string.menu_rename_group))
                    }
                    OutlinedButton(onClick = { onMergeClick(group) }) {
                        Text(stringResource(R.string.menu_merge_group))
                    }
                    OutlinedButton(onClick = { onDeleteClick(group) }) {
                        Text(stringResource(R.string.menu_delete_group))
                    }
                }
            }
        }
    }
}
