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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.multiappshare.R
import com.multiappshare.domain.MimeMismatchExplanation
import com.multiappshare.model.AppGroup
import com.multiappshare.ui.theme.Dimens

@Composable
internal fun ShareOverlayHeader() {
    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = Dimens.Space4, vertical = Dimens.Space2)
            .fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = Dimens.CardElevation),
    ) {
        Column(modifier = Modifier.padding(Dimens.Space4)) {
            Text(
                stringResource(R.string.share_overlay_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 6,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
internal fun CompatibleGroupsEmptyState(
    mimeType: String?,
    onAddGroup: () -> Unit,
) {
    val kindRes = when (MimeMismatchExplanation.mimeKind(mimeType)) {
        "image" -> R.string.mime_kind_image
        "video" -> R.string.mime_kind_video
        "text" -> R.string.mime_kind_text
        else -> R.string.mime_kind_other
    }
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.Share,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                stringResource(R.string.no_compatible_groups_detail, stringResource(kindRes)),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onAddGroup) {
                Text(stringResource(R.string.fab_add_group))
            }
        }
    }
}

@Composable
internal fun FilterEmptyState(onClear: () -> Unit = {}) {
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                stringResource(R.string.no_groups_match_filter),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
            TextButton(onClick = onClear) {
                Text(stringResource(R.string.filter_clear_search))
            }
        }
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
