package com.multiappshare.ui.groups

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.multiappshare.R
import com.multiappshare.domain.GroupNameHelper

@Composable
fun CreateGroupDialog(
    onDismiss: () -> Unit,
    onCreateGroup: (String) -> Unit,
    nameError: String? = null,
    onNameEdited: () -> Unit = {},
) {
    var groupName by remember { mutableStateOf("") }
    val canSubmit = GroupNameHelper.canSubmit(groupName)
    val submit = { if (canSubmit) onCreateGroup(groupName) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_create_group_title)) },
        text = {
            OutlinedTextField(
                value = groupName,
                onValueChange = {
                    groupName = it
                    onNameEdited()
                },
                label = { Text(stringResource(R.string.label_group_name)) },
                singleLine = true,
                isError = nameError != null,
                supportingText = {
                    if (nameError != null) Text(nameError)
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { submit() }),
                modifier = Modifier.imePadding(),
            )
        },
        confirmButton = {
            Button(onClick = submit, enabled = canSubmit) {
                Text(stringResource(R.string.button_create))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) }
        },
    )
}
