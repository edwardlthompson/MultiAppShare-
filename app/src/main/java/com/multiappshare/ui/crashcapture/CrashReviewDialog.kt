package com.multiappshare.ui.crashcapture

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.multiappshare.R

@Composable
fun CrashReviewDialog(
    onDismiss: () -> Unit,
    onSendFeedback: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.crash_review_title)) },
        text = { Text(stringResource(R.string.crash_review_body)) },
        confirmButton = {
            TextButton(onClick = onSendFeedback) { Text(stringResource(R.string.feedback_title)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_close)) }
        },
    )
}
