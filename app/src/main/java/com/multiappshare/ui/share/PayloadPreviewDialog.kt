package com.multiappshare.ui.share

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.multiappshare.R
import com.multiappshare.payloadpreview.PayloadPreview

@Composable
fun PayloadPreviewDialog(
    mime: String?,
    text: String?,
    uriCount: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.preview_title)) },
        text = {
            Column {
                Text(stringResource(R.string.preview_mime, PayloadPreview.mimeLabel(mime)))
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.preview_uris, uriCount))
                val snippet = PayloadPreview.textSnippet(text)
                if (snippet.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.preview_text, snippet))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(stringResource(R.string.preview_continue)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) }
        },
    )
}
