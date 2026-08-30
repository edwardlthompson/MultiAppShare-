package com.multiappshare.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.multiappshare.R

@Composable
fun ThemeDialog(
    selected: Boolean?,
    crashCapture: Boolean,
    highRefresh: Boolean,
    shareHaptics: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Boolean?, Boolean, Boolean, Boolean) -> Unit,
) {
    var draft by remember(selected) { mutableStateOf(selected) }
    var crashDraft by remember(crashCapture) { mutableStateOf(crashCapture) }
    var refreshDraft by remember(highRefresh) { mutableStateOf(highRefresh) }
    var hapticsDraft by remember(shareHaptics) { mutableStateOf(shareHaptics) }
    val options = listOf<Pair<Boolean?, Int>>(
        null to R.string.theme_system,
        false to R.string.theme_light,
        true to R.string.theme_dark,
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.theme_title)) },
        text = {
            Column {
                options.forEach { (value, label) ->
                    val isSelected = draft == value
                    Row(
                        modifier = Modifier.selectable(
                            selected = isSelected,
                            onClick = { draft = value },
                            role = Role.RadioButton,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = isSelected, onClick = { draft = value })
                        Text(stringResource(label))
                    }
                }
                Row(
                    modifier = Modifier.selectable(
                        selected = crashDraft,
                        onClick = { crashDraft = !crashDraft },
                        role = Role.Checkbox,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(checked = crashDraft, onCheckedChange = null)
                    Text(stringResource(R.string.settings_crash_capture))
                }
                Row(
                    modifier = Modifier.selectable(
                        selected = refreshDraft,
                        onClick = { refreshDraft = !refreshDraft },
                        role = Role.Checkbox,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(checked = refreshDraft, onCheckedChange = null)
                    Text(stringResource(R.string.display_high_refresh))
                }
                Row(
                    modifier = Modifier.selectable(
                        selected = hapticsDraft,
                        onClick = { hapticsDraft = !hapticsDraft },
                        role = Role.Checkbox,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(checked = hapticsDraft, onCheckedChange = null)
                    Text(stringResource(R.string.settings_share_haptics))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(draft, crashDraft, refreshDraft, hapticsDraft) }) {
                Text(stringResource(R.string.button_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) }
        },
    )
}
