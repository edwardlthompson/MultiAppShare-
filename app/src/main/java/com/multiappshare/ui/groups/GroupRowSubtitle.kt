package com.multiappshare.ui.groups

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.multiappshare.R
import com.multiappshare.domain.GroupRowMeta

@Composable
internal fun GroupRowSubtitle(appCount: Int, lastSharedMillis: Long?) {
    val count = stringResource(R.string.tablet_group_app_count, appCount)
    val days = GroupRowMeta.daysSince(lastSharedMillis, System.currentTimeMillis())
    val recency = when (days) {
        null -> stringResource(R.string.group_row_never_shared)
        0 -> stringResource(R.string.group_row_shared_today)
        1 -> stringResource(R.string.group_row_shared_yesterday)
        else -> stringResource(R.string.group_row_shared_days, days)
    }
    Text(
        stringResource(R.string.group_row_meta, count, recency),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
internal fun GroupAddAppsButton(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(stringResource(R.string.group_add_apps))
    }
}
