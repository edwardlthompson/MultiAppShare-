package com.multiappshare.share

import android.net.Uri
import com.multiappshare.ShareSessionState
import com.multiappshare.domain.ShareSessionSnapshot

fun ShareSessionState.toSnapshot(nowMillis: Long): ShareSessionSnapshot =
    ShareSessionSnapshot(
        uris = uris.orEmpty().map { it.toString() },
        text = text,
        mimeType = mimeType,
        appPackages = appPackages.orEmpty(),
        currentIndex = currentIndex,
        sharingStarted = sharingStarted,
        savedAtMillis = nowMillis,
    )

fun ShareSessionSnapshot.toState(): ShareSessionState {
    val parsed = uris.mapNotNull { raw ->
        runCatching { Uri.parse(raw) }.getOrNull()?.takeIf { it.scheme != null }
    }
    return ShareSessionState(
        uris = parsed.takeIf { it.isNotEmpty() },
        text = text,
        mimeType = mimeType,
        appPackages = appPackages.takeIf { it.isNotEmpty() },
        currentIndex = currentIndex.coerceAtLeast(0),
        sharingStarted = sharingStarted,
    )
}
