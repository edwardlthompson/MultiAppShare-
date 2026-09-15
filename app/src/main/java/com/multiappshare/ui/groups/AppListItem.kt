package com.multiappshare.ui.groups

import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.multiappshare.domain.AppAvatarPlaceholder
import com.multiappshare.domain.WorkProfileDisambiguation
import com.multiappshare.model.AppInfo
import com.multiappshare.ui.theme.Dimens

@Composable
fun AppListItem(app: AppInfo, @Suppress("UNUSED_PARAMETER") packageManager: PackageManager) {
    val initial = AppAvatarPlaceholder.initial(app.appName)
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.Space1),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SubcomposeAsyncImage(
            model = app,
            contentDescription = null,
            modifier = Modifier.size(Dimens.Avatar),
        ) {
            val showPlaceholder = painter.state !is AsyncImagePainter.State.Success
            if (showPlaceholder) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(initial, style = MaterialTheme.typography.labelMedium)
                }
            } else {
                SubcomposeAsyncImageContent()
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = WorkProfileDisambiguation.formatAppLabel(app.appName, app.isWorkProfile))
    }
}
