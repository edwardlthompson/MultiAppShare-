package com.multiappshare.ui.groups

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.unit.dp
import com.multiappshare.R
import com.multiappshare.domain.GroupColorLabel
import com.multiappshare.domain.GroupNameHelper
import com.multiappshare.domain.GroupNotesHelper
import com.multiappshare.model.AppGroup

@Composable
fun RenameGroupDialog(
    group: AppGroup,
    onDismiss: () -> Unit,
    onRename: (String, String) -> Unit,
    nameError: String? = null,
    onNameEdited: () -> Unit = {},
) {
    var groupName by remember(group.name) { mutableStateOf(GroupColorLabel.stripEmoji(group.name)) }
    var emoji by remember(group.name) { mutableStateOf(GroupColorLabel.extractEmoji(group.name)) }
    var notes by remember(group.notes) { mutableStateOf(group.notes) }
    val canSubmit = GroupNameHelper.canSubmit(groupName)
    val submit = {
        if (canSubmit) onRename(GroupColorLabel.formatWithEmoji(emoji, groupName), notes)
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_rename_group_title)) },
        text = {
            Column(modifier = Modifier.imePadding()) {
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
                        imeAction = ImeAction.Next,
                    ),
                )
                Spacer(Modifier.height(8.dp))
                Text(stringResource(R.string.label_group_emoji))
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    GroupColorLabel.PRESET_EMOJIS.forEach { preset ->
                        FilterChip(
                            selected = emoji == preset,
                            onClick = { emoji = if (emoji == preset) null else preset },
                            label = { Text(preset) },
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it.take(GroupNotesHelper.MAX_NOTE_LENGTH) },
                    label = { Text(stringResource(R.string.label_group_notes)) },
                    supportingText = { Text("${notes.length}/${GroupNotesHelper.MAX_NOTE_LENGTH}") },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(onDone = { submit() }),
                )
            }
        },
        confirmButton = {
            Button(onClick = submit, enabled = canSubmit) {
                Text(stringResource(R.string.button_save))
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) } },
    )
}
