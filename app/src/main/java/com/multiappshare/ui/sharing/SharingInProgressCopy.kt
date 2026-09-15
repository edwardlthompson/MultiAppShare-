package com.multiappshare.ui.sharing

import android.net.Uri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.multiappshare.R
import com.multiappshare.domain.SharingHeadline
import com.multiappshare.model.AppInfo
import com.multiappshare.shareoverlay.ShareOverlayActions
import com.multiappshare.shareprogress.ShareProgressAnnounce

@Composable
internal fun SharingInProgressCopy(
    heroApp: AppInfo?,
    currentLabel: String,
    mimeType: String?,
    text: String?,
    uris: List<Uri>?,
    currentIndex: Int,
    totalApps: Int,
    paused: Boolean,
    lastShareFailed: Boolean,
) {
    if (heroApp != null) {
        AsyncImage(
            model = heroApp,
            contentDescription = currentLabel,
            modifier = Modifier.size(72.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
    val contentNoun = sharingContentNoun(mimeType, text, uris)
    Text(
        SharingHeadline.title(currentLabel, contentNoun),
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.height(8.dp))
    val progress = ShareProgressAnnounce.snapshot(currentIndex, totalApps, currentLabel)
    val progressSpoken = if (progress == null) {
        ""
    } else if (progress.target.isEmpty()) {
        stringResource(R.string.sharing_step_format, progress.step, progress.total)
    } else {
        stringResource(
            R.string.sharing_progress_announce,
            progress.step,
            progress.total,
            progress.target,
        )
    }
    Text(
        stringResource(R.string.sharing_overlay_subtitle, contentNoun, currentIndex + 1, totalApps),
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier.semantics {
            liveRegion = LiveRegionMode.Polite
            if (progressSpoken.isNotEmpty()) contentDescription = progressSpoken
        },
    )
    Spacer(modifier = Modifier.height(8.dp))
    LinearProgressIndicator(
        progress = { ShareOverlayActions.progressFraction(currentIndex, totalApps) },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
    )
    if (SharingHeadline.SHOW_NEXT_APP_LINE && currentLabel.isNotEmpty()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            stringResource(R.string.sharing_next_app_format, currentLabel),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
    }
    if (paused) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(R.string.sharing_paused),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.titleSmall,
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        stringResource(R.string.sharing_preview_hint),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        stringResource(R.string.sharing_return_instruction),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge,
    )
    if (lastShareFailed && currentLabel.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(R.string.sharing_open_failed, currentLabel),
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
internal fun sharingContentNoun(mimeType: String?, text: String?, uris: List<Uri>?): String {
    return when {
        mimeType?.startsWith("image/") == true -> {
            if (uris != null && uris.size > 1) {
                stringResource(R.string.sharing_content_photos_n, uris.size)
            } else {
                stringResource(R.string.sharing_content_photo)
            }
        }
        mimeType?.startsWith("video/") == true -> {
            if (uris != null && uris.size > 1) {
                stringResource(R.string.sharing_content_videos_n, uris.size)
            } else {
                stringResource(R.string.sharing_content_video)
            }
        }
        text != null && uris.isNullOrEmpty() -> stringResource(R.string.sharing_content_text)
        else -> {
            if (uris != null && uris.size > 1) {
                stringResource(R.string.sharing_content_media_n, uris.size)
            } else {
                stringResource(R.string.sharing_content_media)
            }
        }
    }
}
