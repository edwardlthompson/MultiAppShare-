package com.multiappshare.ui.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.multiappshare.R
import com.multiappshare.model.AppGroup

@Composable
internal fun ShareOverlayHeader() {
    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.share_overlay_title),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.share_overlay_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 6,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.92f),
            )
        }
    }
}

@Composable
internal fun CompatibleGroupsEmptyState(mimeType: String?) {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            stringResource(R.string.no_compatible_groups_detail, mimeType ?: "*/*"),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
internal fun FilterEmptyState() {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            stringResource(R.string.no_groups_match_filter),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

internal fun filterCompatibleGroups(
    groups: List<AppGroup>,
    uris: List<Uri>?,
    mimeType: String?,
    getCompatiblePackages: (String, String) -> Set<String>,
): List<AppGroup> {
    val shareAction = if (uris != null && uris.size > 1) {
        Intent.ACTION_SEND_MULTIPLE
    } else {
        Intent.ACTION_SEND
    }
    val compatibleCat = getCompatiblePackages(shareAction, mimeType ?: "*/*")
    return groups.filter { group ->
        group.apps.any { app ->
            val key = "${app.packageName}/${app.activityName}"
            val fallbackKey = "${app.packageName}/"
            key in compatibleCat || compatibleCat.any { it.startsWith(fallbackKey) }
        }
    }
}
