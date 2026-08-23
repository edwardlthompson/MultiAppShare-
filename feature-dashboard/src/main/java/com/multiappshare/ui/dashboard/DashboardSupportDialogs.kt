package com.multiappshare.ui.dashboard

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

data class DonateNudgeLabels(
    val title: String,
    val body: String,
    val donate: String,
    val notNow: String,
)

data class UpdateDialogLabels(
    val title: String,
    val body: String,
    val install: String,
    val later: String,
)

@Composable
fun DashboardDonateNudgeDialog(
    labels: DonateNudgeLabels,
    onDonate: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(labels.title) },
        text = { Text(labels.body) },
        confirmButton = {
            TextButton(onClick = onDonate) { Text(labels.donate) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(labels.notNow) }
        },
    )
}

@Composable
fun DashboardUpdateDialog(
    labels: UpdateDialogLabels,
    onInstall: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(labels.title) },
        text = { Text(labels.body) },
        confirmButton = {
            TextButton(onClick = onInstall) { Text(labels.install) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(labels.later) }
        },
    )
}
