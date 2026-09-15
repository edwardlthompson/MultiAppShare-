package com.multiappshare

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.multiappshare.domain.PassphraseForm
import com.multiappshare.domain.PassphraseStrengthLevel
import com.multiappshare.domain.PassphraseStrengthMeter
import com.multiappshare.ui.backup.PassphraseField
import com.multiappshare.ui.theme.Dimens

@Composable
fun BackupExportPassphraseDialog(
    onDismiss: () -> Unit,
    onConfirmed: (CharArray) -> Unit,
) {
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.backup_encrypted_title)) },
        text = {
            BackupExportPassphraseFields(
                pass = pass,
                onPassChange = { pass = it },
                confirm = confirm,
                onConfirmChange = { confirm = it },
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val chars = pass.toCharArray()
                    pass = ""
                    confirm = ""
                    onConfirmed(chars)
                },
                enabled = PassphraseForm.exportEnabled(pass, confirm),
            ) { Text(stringResource(R.string.backup_choose_location)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) }
        },
    )
}

@Composable
internal fun BackupExportPassphraseFields(
    pass: String,
    onPassChange: (String) -> Unit,
    confirm: String,
    onConfirmChange: (String) -> Unit,
) {
    val mismatch = PassphraseForm.mismatchVisible(pass, confirm)
    Column(modifier = Modifier.imePadding()) {
        Text(
            stringResource(R.string.backup_export_body, PassphraseForm.MIN_LENGTH),
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(Modifier.height(Dimens.Space3))
        PassphraseField(
            value = pass,
            onValueChange = onPassChange,
            labelRes = R.string.label_passphrase,
            newPassword = true,
        )
        PassphraseStrengthHint(pass)
        Spacer(Modifier.height(Dimens.Space2))
        PassphraseField(
            value = confirm,
            onValueChange = onConfirmChange,
            labelRes = R.string.label_confirm_passphrase,
            isError = mismatch,
            supportingText = if (mismatch) stringResource(R.string.passphrase_mismatch) else null,
            imeAction = ImeAction.Done,
            newPassword = true,
        )
    }
}

@Composable
fun BackupImportPassphraseDialog(
    onDismiss: () -> Unit,
    onConfirm: (CharArray) -> Unit,
    errorText: String? = null,
    onPassEdited: () -> Unit = {},
) {
    var pass by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.backup_encrypted_title)) },
        text = {
            Column {
                Text(
                    stringResource(R.string.backup_import_body, PassphraseForm.MIN_LENGTH),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(Dimens.Space3))
                PassphraseField(
                    value = pass,
                    onValueChange = {
                        pass = it
                        onPassEdited()
                    },
                    labelRes = R.string.label_passphrase,
                    isError = errorText != null,
                    supportingText = errorText,
                    imeAction = ImeAction.Done,
                    modifier = Modifier.imePadding(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(pass.toCharArray()) },
                enabled = PassphraseForm.importEnabled(pass),
            ) { Text(stringResource(R.string.backup_import_confirm)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) }
        },
    )
}

@Composable
private fun PassphraseStrengthHint(pass: String) {
    val level = remember(pass) { PassphraseStrengthMeter.evaluate(pass.toCharArray()) }
    val label = when (level) {
        PassphraseStrengthLevel.EMPTY -> R.string.passphrase_strength_empty
        PassphraseStrengthLevel.WEAK -> R.string.passphrase_strength_weak
        PassphraseStrengthLevel.FAIR -> R.string.passphrase_strength_fair
        PassphraseStrengthLevel.STRONG -> R.string.passphrase_strength_strong
    }
    val color = when (level) {
        PassphraseStrengthLevel.EMPTY -> MaterialTheme.colorScheme.onSurfaceVariant
        PassphraseStrengthLevel.WEAK -> MaterialTheme.colorScheme.error
        PassphraseStrengthLevel.FAIR -> MaterialTheme.colorScheme.tertiary
        PassphraseStrengthLevel.STRONG -> MaterialTheme.colorScheme.primary
    }
    Spacer(Modifier.height(Dimens.Space2))
    LinearProgressIndicator(
        progress = { PassphraseForm.progress(level) },
        modifier = Modifier.fillMaxWidth(),
        color = color,
    )
    Text(stringResource(label), style = MaterialTheme.typography.bodySmall, color = color)
}
