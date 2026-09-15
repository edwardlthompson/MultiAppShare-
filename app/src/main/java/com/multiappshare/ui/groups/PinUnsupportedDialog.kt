package com.multiappshare.ui.groups

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.multiappshare.R

@Composable
fun PinUnsupportedDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.pin_unsupported_title)) },
        text = { Text(stringResource(R.string.toast_pin_unsupported)) },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_ok)) }
        },
    )
}
