package com.multiappshare.ui.feedback

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.multiappshare.R
import com.multiappshare.about.AboutLinks
import com.multiappshare.feedback.FeedbackPreview
import com.multiappshare.githubfeedback.IssueFormUrl

@Composable
fun FeedbackDialog(
    crashText: String?,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.feedback_title)) },
        text = {
            Column {
                Text(stringResource(R.string.feedback_body_hint))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.feedback_title_label)) },
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text(stringResource(R.string.feedback_message_label)) },
                    minLines = 3,
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val built = IssueFormUrl.build(
                        AboutLinks.ISSUES_REPO,
                        FeedbackPreview.title(title),
                        FeedbackPreview.body(body, crashText),
                    )
                    if (built.url.isEmpty()) return@TextButton
                    val launched = runCatching {
                        context.startActivity(Intent(Intent.ACTION_VIEW, built.url.toUri()))
                    }.isSuccess
                    if (launched) onDismiss()
                },
            ) { Text(stringResource(R.string.feedback_github)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) }
        },
    )
}
