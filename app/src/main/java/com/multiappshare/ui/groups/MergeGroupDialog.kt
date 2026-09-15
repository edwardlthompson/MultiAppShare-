package com.multiappshare.ui.groups

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
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
import com.multiappshare.domain.MergePreview
import com.multiappshare.model.AppGroup

@Composable
fun MergeGroupDialog(
    source: AppGroup,
    targets: List<AppGroup>,
    onDismiss: () -> Unit,
    onMergeInto: (AppGroup) -> Unit,
    errorText: String? = null,
) {
    var draft by remember(source.name) { mutableStateOf(targets.firstOrNull()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_merge_group_title, source.name)) },
        text = {
            Column {
                if (errorText != null) {
                    Text(errorText, color = MaterialTheme.colorScheme.error)
                }
                Text(stringResource(R.string.dialog_merge_into))
                draft?.let { target ->
                    Text(
                        stringResource(
                            R.string.merge_preview_apps,
                            MergePreview.combinedAppCount(source.apps, target.apps),
                        ),
                    )
                }
                targets.forEach { target ->
                    Row(
                        modifier = Modifier.selectable(
                            selected = draft?.name == target.name,
                            onClick = { draft = target },
                            role = Role.RadioButton,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = draft?.name == target.name,
                            onClick = { draft = target },
                        )
                        Text(target.name)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { draft?.let(onMergeInto) },
                enabled = draft != null,
            ) { Text(stringResource(R.string.button_merge)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) }
        },
    )
}
