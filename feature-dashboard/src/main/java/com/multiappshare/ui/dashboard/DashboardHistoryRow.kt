package com.multiappshare.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.multiappshare.domain.HistoryPayload.reshareSnapshot
import com.multiappshare.model.HistoryItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun DashboardHistoryRow(
    item: HistoryItem,
    labels: HistoryDialogLabels,
    onReshareItem: ((HistoryItem) -> Unit)?,
) {
    val date = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(item.timestamp))
    val reshare = onReshareItem
    val canReshare = reshare != null && item.reshareSnapshot() != null
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.groupName,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                color = if (item.isError) Color.Red else MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = date, style = MaterialTheme.typography.bodySmall, maxLines = 2)
        }
        Text(
            text = String.format(labels.sharedPrefix, item.contentDescription),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
        )
        Text(
            text = item.status,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            color = if (item.isError) Color.Red else MaterialTheme.colorScheme.primary,
        )
        if (canReshare && labels.reshareRow.isNotEmpty()) {
            TextButton(
                onClick = { reshare(item) },
                modifier = Modifier
                    .defaultMinSize(minHeight = 48.dp)
                    .semantics { contentDescription = labels.reshareRow },
            ) {
                Text(labels.reshareRow)
            }
        }
        HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
    }
}
