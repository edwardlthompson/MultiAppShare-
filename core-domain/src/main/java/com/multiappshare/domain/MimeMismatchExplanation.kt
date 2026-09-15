package com.multiappshare.domain

object MimeMismatchExplanation {
    fun isGroupCompatible(groupAppPackageKeys: Set<String>, compatiblePackageKeys: Set<String>): Boolean {
        if (groupAppPackageKeys.isEmpty()) return false
        return groupAppPackageKeys.any { it in compatiblePackageKeys }
    }

    fun mimeKind(mimeType: String?): String {
        val mime = mimeType?.trim().orEmpty().lowercase()
        return when {
            mime.startsWith("image/") -> "image"
            mime.startsWith("video/") -> "video"
            mime.startsWith("text/") -> "text"
            else -> "other"
        }
    }
}
