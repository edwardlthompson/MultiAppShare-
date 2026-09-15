package com.multiappshare.ui.feedback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
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
import com.multiappshare.about.AboutLinks
import com.multiappshare.feedback.FeedbackPreview
import com.multiappshare.githubfeedback.IssueFormUrl
import com.multiappshare.ui.launch.rememberOpenUrl

@Composable
fun FeedbackDialog(
    crashText: String?,
    onDismiss: () -> Unit,
) {
    val openUrl = rememberOpenUrl()
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var launchFailed by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.feedback_title)) },
        text = {
            Column {
                Text(stringResource(R.string.feedback_body_hint))
                if (launchFailed) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        stringResource(R.string.error_no_app_for_link),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.feedback_title_label)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next,
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text(stringResource(R.string.feedback_message_label)) },
                    minLines = 3,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Default,
                    ),
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
                    val launched = openUrl(built.url)
                    launchFailed = !launched
                    if (launched) onDismiss()
                },
                enabled = FeedbackPreview.canSend(title, body, crashText),
            ) { Text(stringResource(R.string.feedback_github)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) }
        },
    )
}
