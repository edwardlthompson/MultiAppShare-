package com.multiappshare.share

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import com.multiappshare.ShareSessionState

object ClipboardShare {
    fun read(context: Context): ShareSessionState? {
        val manager = context.getSystemService(ClipboardManager::class.java) ?: return null
        val clip = try {
            manager.primaryClip
        } catch (_: SecurityException) {
            null
        }
        val item = if (clip != null && clip.itemCount > 0) clip.getItemAt(0) else null
        val uri = item?.uri
        val text = item?.coerceToText(context)?.toString()
        return when {
            uri != null && uri.scheme != null -> ShareSessionState(
                uris = listOf(uri),
                mimeType = context.contentResolver.getType(uri) ?: "*/*",
                sharingStarted = false,
            )
            !text.isNullOrBlank() -> ShareSessionState(
                text = text,
                mimeType = "text/plain",
                sharingStarted = false,
            )
            else -> null
        }
    }

    fun persistableFlags(): Int =
        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
}
