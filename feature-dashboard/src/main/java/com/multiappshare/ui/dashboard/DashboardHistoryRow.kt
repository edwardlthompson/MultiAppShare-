package com.multiappshare.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.multiappshare.domain.HistoryPayload.reshareSnapshot
import com.multiappshare.domain.HistoryPresentation
import com.multiappshare.model.HistoryItem

@Composable
internal fun DashboardHistoryRow(
    item: HistoryItem,
    labels: HistoryDialogLabels,
    onReshareItem: ((HistoryItem) -> Unit)?,
) {
    val locale = LocalLocale.current.platformLocale
    val date = HistoryPresentation.formatTimestamp(item.timestamp, locale)
    val reshare = onReshareItem
    val canReshare = reshare != null && item.reshareSnapshot() != null
    val errorColor = MaterialTheme.colorScheme.error
    val nameCd = if (item.isError && labels.error.isNotEmpty()) {
        "${item.groupName}. ${labels.error}"
    } else {
        item.groupName
    }
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.semantics(mergeDescendants = true) { contentDescription = nameCd },
        ) {
            if (item.isError) {
                Icon(
                    Icons.Filled.Error,
                    contentDescription = null,
                    tint = errorColor,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = item.groupName,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                color = if (item.isError) errorColor else MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(text = date, style = MaterialTheme.typography.bodySmall, maxLines = 2)
        }
        Text(
            text = HistoryPresentation.sharedLine(labels.sharedPrefix, item.contentDescription),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
        )
        Text(
            text = item.status,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            color = if (item.isError) errorColor else MaterialTheme.colorScheme.onSurfaceVariant,
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
