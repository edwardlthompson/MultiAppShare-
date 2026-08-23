package com.multiappshare.ui.dashboard

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.multiappshare.core.ui.highRefreshScroll
import com.multiappshare.model.HistoryItem
import com.multiappshare.updates.ProductUpdate

data class HistoryDialogLabels(
    val title: String,
    val empty: String,
    val sharedPrefix: String,
    val close: String,
    val reshare: String = "",
    val reshareRow: String = "",
)

@Composable
fun DashboardHistoryDialog(
    history: List<HistoryItem>,
    labels: HistoryDialogLabels,
    onDismiss: () -> Unit,
    onReshare: (() -> Unit)? = null,
    onReshareItem: ((HistoryItem) -> Unit)? = null,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(labels.title) },
        text = {
            if (history.isEmpty()) {
                Text(labels.empty)
            } else {
                LazyColumn(modifier = Modifier.height(400.dp).highRefreshScroll()) {
                    items(history, key = { it.id }) { item ->
                        DashboardHistoryRow(item = item, labels = labels, onReshareItem = onReshareItem)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text(labels.close) }
        },
        dismissButton = {
            if (onReshare != null && labels.reshare.isNotEmpty()) {
                Button(onClick = onReshare) { Text(labels.reshare) }
            }
        },
    )
}

data class AboutDialogLabels(
    val title: String,
    val versionLine: String,
    val tagline: String,
    val privacy: String,
    val developerContact: String,
    val telegramLabel: String,
    val supportDeveloper: String,
    val venmoLink: String,
    val ok: String,
)

@Composable
fun DashboardAboutDialog(labels: AboutDialogLabels, onDismiss: () -> Unit) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(labels.title) },
        text = {
            Column {
                Text(labels.versionLine, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(labels.tagline)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    labels.privacy,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(labels.developerContact, fontWeight = FontWeight.Bold)
                Text(
                    text = labels.telegramLabel,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(Intent.ACTION_VIEW, "https://t.me/EdwardLeeThompson".toUri()))
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(labels.supportDeveloper, fontWeight = FontWeight.Bold)
                Text(
                    text = labels.venmoLink,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, ProductUpdate.VENMO_URL.toUri()),
                        )
                    },
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text(labels.ok) }
        },
    )
}
