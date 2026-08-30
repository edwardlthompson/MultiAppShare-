package com.multiappshare.domain

object HistoryUriPermission {
    fun canOpenUri(uriString: String?, persistedUriPermissions: Set<String>): Boolean {
        if (uriString.isNullOrBlank()) return false
        val clean = uriString.trim()
        if (clean.startsWith("content://")) {
            return clean in persistedUriPermissions
        }
        return clean.startsWith("http://") || clean.startsWith("https://")
    }
}
