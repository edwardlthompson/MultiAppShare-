package com.multiappshare.ui.backup

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.multiappshare.R
import com.multiappshare.domain.ImportConfirmationHelper
import com.multiappshare.domain.ImportStrategy

@Composable
fun ImportStrategyDialog(
    existingGroupCount: Int,
    onDismiss: () -> Unit,
    onChosen: (ImportStrategy) -> Unit,
) {
    var pendingReplace by remember { mutableStateOf(false) }
    if (pendingReplace) {
        AlertDialog(
            onDismissRequest = { pendingReplace = false },
            title = { Text(stringResource(R.string.import_replace_confirm_title)) },
            text = {
                Text(
                    pluralStringResource(R.plurals.import_replace_warning, existingGroupCount, existingGroupCount),
                )
            },
            confirmButton = {
                TextButton(onClick = { onChosen(ImportStrategy.REPLACE) }) {
                    Text(
                        stringResource(R.string.import_strategy_replace),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingReplace = false }) {
                    Text(stringResource(R.string.button_cancel))
                }
            },
        )
        return
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.import_strategy_title)) },
        text = { Text(stringResource(R.string.import_strategy_body)) },
        confirmButton = {
            TextButton(onClick = { onChosen(ImportStrategy.MERGE) }) {
                Text(stringResource(R.string.import_strategy_merge))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    if (ImportConfirmationHelper.shouldPromptConfirmation(existingGroupCount, true)) {
                        pendingReplace = true
                    } else {
                        onChosen(ImportStrategy.REPLACE)
                    }
                },
            ) {
                Text(
                    stringResource(R.string.import_strategy_replace),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
    )
}
