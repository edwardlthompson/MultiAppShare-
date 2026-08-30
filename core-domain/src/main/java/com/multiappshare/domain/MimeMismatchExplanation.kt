package com.multiappshare.domain

object MimeMismatchExplanation {
    fun formatExplanation(groupName: String, mimeType: String): String {
        val safeMime = mimeType.trim().ifBlank { "*/*" }
        return "No apps in \"$groupName\" support content type $safeMime"
    }

    fun isGroupCompatible(groupAppPackageKeys: Set<String>, compatiblePackageKeys: Set<String>): Boolean {
        if (groupAppPackageKeys.isEmpty()) return false
        return groupAppPackageKeys.any { it in compatiblePackageKeys }
    }
}
