package com.multiappshare.domain

object WhatsNewHelper {
    fun shouldShowWhatsNew(
        lastSeenVersionCode: Int,
        currentVersionCode: Int,
    ): Boolean {
        if (lastSeenVersionCode <= 0) return false
        return currentVersionCode > lastSeenVersionCode
    }

    fun getHighlightsForVersion(versionName: String): List<String> {
        val entry = OfflineChangelogCatalog.ENTRIES.find { it.version == versionName }
        return entry?.highlights ?: listOf("Bug fixes and performance improvements")
    }
}
