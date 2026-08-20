package com.multiappshare.share

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri

object PersistableShareUris {

    fun persistableReadFlags(intentFlags: Int): Int? {
        val persistable = Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        val read = Intent.FLAG_GRANT_READ_URI_PERMISSION
        if (intentFlags and persistable == 0) return null
        val take = intentFlags and read
        return take.takeIf { it != 0 }
    }

    fun isContentUri(uri: Uri): Boolean = uri.scheme == ContentResolver.SCHEME_CONTENT

    fun take(resolver: ContentResolver, uris: List<Uri>?, intentFlags: Int) {
        val flags = persistableReadFlags(intentFlags) ?: return
        for (uri in uris.orEmpty()) {
            if (!isContentUri(uri)) continue
            try {
                resolver.takePersistableUriPermission(uri, flags)
            } catch (_: SecurityException) {
            } catch (_: IllegalArgumentException) {
            }
        }
    }

    fun release(resolver: ContentResolver, uris: List<Uri>?) {
        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        for (uri in uris.orEmpty()) {
            if (!isContentUri(uri)) continue
            try {
                resolver.releasePersistableUriPermission(uri, flags)
            } catch (_: SecurityException) {
            } catch (_: IllegalArgumentException) {
            }
        }
    }
}
