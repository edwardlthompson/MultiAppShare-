package com.multiappshare

import android.net.Uri

data class ShareSessionState(
    val uris: List<Uri>? = null,
    val text: String? = null,
    val mimeType: String? = null,
    val appPackages: List<String>? = null,
    val currentIndex: Int = 0,
    val sharingStarted: Boolean = false,
    val lastShareFailed: Boolean = false,
) {
    val inShareMode: Boolean get() = !uris.isNullOrEmpty() || text != null
}
