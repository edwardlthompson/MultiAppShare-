package com.multiappshare.ui.groups

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.multiappshare.R
import com.multiappshare.model.AppGroup

@Composable
fun RenameGroupDialog(
    group: AppGroup,
    onDismiss: () -> Unit,
    onRename: (String) -> Unit,
) {
    var groupName by remember(group.name) { mutableStateOf(group.name) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_rename_group_title)) },
        text = {
            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text(stringResource(R.string.label_group_name)) },
                singleLine = true,
            )
        },
        confirmButton = {
            Button(onClick = { if (groupName.isNotBlank()) onRename(groupName) }) {
                Text(stringResource(R.string.button_save))
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) } },
    )
}
