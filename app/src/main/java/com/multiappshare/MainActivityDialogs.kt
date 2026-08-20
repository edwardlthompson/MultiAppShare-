package com.multiappshare

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.multiappshare.core.ui.highRefreshScroll
import com.multiappshare.model.HistoryItem
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun EmptyGroupsPlaceholder(
    onAddGroup: () -> Unit = {},
    onAutoGroup: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.empty_groups_message),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onAddGroup) {
                    Text(stringResource(R.string.empty_groups_button_create))
                }
                TextButton(onClick = onAutoGroup) {
                    Text(stringResource(R.string.empty_groups_button_auto))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.empty_groups_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun DeleteGroupDialog(groupName: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_group_title)) },
        text = { Text(stringResource(R.string.delete_group_message, groupName)) },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(stringResource(R.string.button_delete), color = MaterialTheme.colorScheme.error) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) }
        },
    )
}

@Composable
fun HistoryDialog(history: List<HistoryItem>, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.history_title)) },
        text = {
            if (history.isEmpty()) {
                Text(stringResource(R.string.history_empty))
            } else {
                LazyColumn(modifier = Modifier.height(400.dp).highRefreshScroll()) {
                    items(history) { item ->
                        val locale = LocalLocale.current.platformLocale
                        val date = SimpleDateFormat("MMM dd, HH:mm", locale).format(Date(item.timestamp))
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = item.groupName,
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.isError) Color.Red else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(text = date, style = MaterialTheme.typography.bodySmall)
                            }
                            Text(
                                text = stringResource(R.string.history_shared_prefix, item.contentDescription),
                                style = MaterialTheme.typography.bodySmall,
                            )
                            Text(
                                text = item.status,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (item.isError) Color.Red else MaterialTheme.colorScheme.primary
                            )
                            HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text(stringResource(R.string.button_close)) }
        },
    )
}

@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val version = try {
        val pInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, 0)
        }
        pInfo.versionName
    } catch (_: Exception) {
        null
    }
    val versionLine = version ?: stringResource(R.string.version_unknown)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.about_title)) },
        text = {
            Column {
                Text(stringResource(R.string.version_line, versionLine), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.about_tagline))
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    stringResource(R.string.about_privacy),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.about_developer_contact), fontWeight = FontWeight.Bold)
                Text(
                    text = stringResource(R.string.about_telegram_label),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(Intent.ACTION_VIEW, "https://t.me/EdwardLeeThompson".toUri()))
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.about_support_developer), fontWeight = FontWeight.Bold)
                Text(
                    text = stringResource(R.string.about_venmo_link),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(Intent.ACTION_VIEW, "https://venmo.com/code?user_id=1857304970395648420".toUri()))
                    },
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text(stringResource(R.string.button_ok)) }
        },
    )
}
