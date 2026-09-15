package com.multiappshare.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multiappshare.core.ui.highRefreshScroll
import com.multiappshare.model.HistoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    history: List<HistoryItem>,
    labels: HistoryDialogLabels,
    onDismiss: () -> Unit,
    onReshare: (() -> Unit)? = null,
    onReshareItem: ((HistoryItem) -> Unit)? = null,
    errorText: String? = null,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(labels.title) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = labels.close)
                    }
                },
                actions = {
                    if (onReshare != null && labels.reshare.isNotEmpty()) {
                        TextButton(onClick = onReshare) { Text(labels.reshare) }
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            if (!errorText.isNullOrEmpty()) {
                Text(
                    errorText,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
            if (history.isEmpty()) {
                Icon(
                    Icons.Outlined.History,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp).padding(top = 16.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(labels.empty, style = MaterialTheme.typography.titleMedium)
                if (labels.emptyCta.isNotEmpty()) {
                    Text(
                        labels.emptyCta,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().highRefreshScroll()) {
                    items(history, key = { it.id }) { item ->
                        DashboardHistoryRow(item = item, labels = labels, onReshareItem = onReshareItem)
                    }
                }
            }
        }
    }
}
