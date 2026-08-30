package com.multiappshare.domain

data class ReleaseChangelogEntry(
    val version: String,
    val releaseDate: String,
    val highlights: List<String>,
)

object OfflineChangelogCatalog {
    val ENTRIES = listOf(
        ReleaseChangelogEntry(
            version = "v1.0.0",
            releaseDate = "2026-08-30",
            highlights = listOf(
                "Initial full release of Multi App Share",
                "Sequential multi-app sharing workflows",
                "Encrypted local backup and restore",
                "F-Droid and GitHub Releases distribution",
            ),
        ),
    )

    fun getLatestVersion(): String = ENTRIES.first().version
}
