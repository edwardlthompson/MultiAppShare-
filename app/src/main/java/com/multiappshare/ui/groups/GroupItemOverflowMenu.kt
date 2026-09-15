package com.multiappshare.ui.groups

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.multiappshare.R
import com.multiappshare.domain.GroupOverflowAction
import com.multiappshare.domain.GroupOverflowPolicy

@Composable
fun GroupItemOverflowMenu(
    expanded: Boolean,
    pinned: Boolean,
    onDismiss: () -> Unit,
    onModifyClick: () -> Unit,
    onReorderClick: () -> Unit,
    onDuplicateClick: () -> Unit,
    onRenameClick: () -> Unit,
    onMergeClick: () -> Unit,
    onPinClick: () -> Unit,
    onAddShortcutClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
        GroupOverflowPolicy.sections().forEachIndexed { index, section ->
            if (index > 0) HorizontalDivider()
            section.forEach { action ->
                val label = when (action) {
                    GroupOverflowAction.MODIFY -> stringResource(R.string.menu_modify_apps)
                    GroupOverflowAction.REORDER -> stringResource(R.string.menu_reorder_apps)
                    GroupOverflowAction.RENAME -> stringResource(R.string.menu_rename_group)
                    GroupOverflowAction.DUPLICATE -> stringResource(R.string.menu_duplicate_group)
                    GroupOverflowAction.MERGE -> stringResource(R.string.menu_merge_group)
                    GroupOverflowAction.PIN -> stringResource(
                        if (pinned) R.string.menu_unpin_group else R.string.menu_pin_group,
                    )
                    GroupOverflowAction.SHORTCUT -> stringResource(R.string.menu_add_home_shortcut)
                    GroupOverflowAction.DELETE -> stringResource(R.string.menu_delete_group)
                }
                val run = when (action) {
                    GroupOverflowAction.MODIFY -> onModifyClick
                    GroupOverflowAction.REORDER -> onReorderClick
                    GroupOverflowAction.RENAME -> onRenameClick
                    GroupOverflowAction.DUPLICATE -> onDuplicateClick
                    GroupOverflowAction.MERGE -> onMergeClick
                    GroupOverflowAction.PIN -> onPinClick
                    GroupOverflowAction.SHORTCUT -> onAddShortcutClick
                    GroupOverflowAction.DELETE -> onDeleteClick
                }
                DropdownMenuItem(
                    text = {
                        if (action == GroupOverflowAction.DELETE) {
                            Text(label, color = MaterialTheme.colorScheme.error)
                        } else {
                            Text(label)
                        }
                    },
                    onClick = {
                        onDismiss()
                        run()
                    },
                )
            }
        }
    }
}
