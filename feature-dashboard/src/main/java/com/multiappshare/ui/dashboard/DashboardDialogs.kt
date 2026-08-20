package com.multiappshare.ui.dashboard

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.multiappshare.core.ui.highRefreshScroll
import com.multiappshare.model.HistoryItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HistoryDialogLabels(
    val title: String,
    val empty: String,
    val sharedPrefix: String,
    val close: String,
    val reshare: String = "",
)

@Composable
fun DashboardHistoryDialog(
    history: List<HistoryItem>,
    labels: HistoryDialogLabels,
    onDismiss: () -> Unit,
    onReshare: (() -> Unit)? = null,
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
                        val date = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(item.timestamp))
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = item.groupName,
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.isError) Color.Red else MaterialTheme.colorScheme.onSurface,
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(text = date, style = MaterialTheme.typography.bodySmall)
                            }
                            Text(
                                text = String.format(labels.sharedPrefix, item.contentDescription),
                                style = MaterialTheme.typography.bodySmall,
                            )
                            Text(
                                text = item.status,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (item.isError) Color.Red else MaterialTheme.colorScheme.primary,
                            )
                            HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
                        }
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
                            Intent(Intent.ACTION_VIEW, "https://venmo.com/code?user_id=1857304970395648420".toUri()),
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
