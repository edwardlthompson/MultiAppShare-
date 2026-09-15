package com.multiappshare.ui.groups

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.multiappshare.R
import com.multiappshare.domain.AutoGroupPreviewRow
import com.multiappshare.ui.settings.SettingsDialogHelper

@Composable
fun AutoGroupPreviewDialog(
    append: Boolean,
    rows: List<AutoGroupPreviewRow>,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.autogroup_preview_title)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                SettingsDialogHelper(
                    stringResource(
                        if (append) R.string.autogroup_preview_append else R.string.autogroup_preview_replace,
                    ),
                )
                if (rows.isEmpty()) {
                    Text(stringResource(R.string.autogroup_preview_empty))
                } else {
                    rows.forEach { row ->
                        Text(stringResource(R.string.autogroup_preview_row, row.name, row.appCount))
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = rows.isNotEmpty()) {
                Text(stringResource(R.string.fab_auto_group))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) }
        },
    )
}
