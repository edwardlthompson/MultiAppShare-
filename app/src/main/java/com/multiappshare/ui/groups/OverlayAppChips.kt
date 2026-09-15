package com.multiappshare.ui.groups

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.multiappshare.domain.OverlayDestinationChips
import com.multiappshare.model.AppInfo

@Composable
fun OverlayAppChips(
    apps: List<AppInfo>,
    @Suppress("UNUSED_PARAMETER") packageManager: PackageManager,
) {
    val shown = apps.take(OverlayDestinationChips.visibleCount(apps.size))
    Row(
        modifier = Modifier.padding(start = 48.dp, top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        shown.forEach { app ->
            AsyncImage(
                model = app,
                contentDescription = app.appName,
                modifier = Modifier.size(32.dp),
            )
        }
    }
}
