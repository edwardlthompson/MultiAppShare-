package com.multiappshare.domain

object HistoryUriPermission {
    fun canOpenUri(uriString: String?, persistedUriPermissions: Set<String>): Boolean {
        val clean = uriString?.trim().orEmpty()
        return when {
            clean.isEmpty() -> false
            clean.startsWith("content://") -> clean in persistedUriPermissions
            else -> clean.startsWith("http://") || clean.startsWith("https://")
        }
    }
}
