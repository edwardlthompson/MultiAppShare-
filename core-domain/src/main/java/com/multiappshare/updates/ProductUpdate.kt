package com.multiappshare.updates

object ProductUpdate {
    const val MS_DAY = 86_400_000L
    const val VENMO_URL = "https://venmo.com/code?user_id=1857304970395648420"
    const val RELEASES_API =
        "https://api.github.com/repos/edwardlthompson/MultiAppShare-/releases/latest"
    const val RELEASES_PAGE =
        "https://github.com/edwardlthompson/MultiAppShare-/releases/latest"

    private val apkVersion = Regex(
        """(?i)multi-?app-?share-v?(\d+\.\d+\.\d+)(?:-(?:release|foss))?\.apk$""",
    )

    data class NamedAsset(val name: String, val url: String)
    data class ProductAsset(val version: String, val url: String)

    fun shouldCheckDaily(lastCheckAt: Long?, now: Long): Boolean {
        if (lastCheckAt == null || lastCheckAt < 0L) return true
        return now - lastCheckAt >= MS_DAY
    }

    fun isNewerVersion(current: String, latest: String): Boolean {
        fun parts(v: String) = v.split('.').map { it.toIntOrNull() ?: 0 }
        val a = parts(current)
        val b = parts(latest)
        for (i in 0..2) {
            val diff = (a.getOrElse(i) { 0 }) - (b.getOrElse(i) { 0 })
            if (diff != 0) return diff < 0
        }
        return false
    }

    fun parseApkVersion(name: String): String? {
        return apkVersion.find(name.trim())?.groupValues?.get(1)
    }

    fun selectApkAsset(assets: List<NamedAsset>): ProductAsset? {
        for (asset in assets) {
            val version = parseApkVersion(asset.name) ?: continue
            if (asset.url.isNotBlank()) return ProductAsset(version, asset.url)
        }
        return null
    }

    fun shouldNudgeDonate(lastSeenVersion: String?, currentVersion: String): Boolean {
        return currentVersion.isNotBlank() &&
            !lastSeenVersion.isNullOrBlank() &&
            lastSeenVersion.trim() != currentVersion.trim()
    }

    fun shouldPromptUpdate(
        currentVersion: String,
        latestVersion: String?,
        dismissedVersion: String?,
    ): Boolean {
        return !latestVersion.isNullOrBlank() &&
            isNewerVersion(currentVersion, latestVersion) &&
            dismissedVersion != latestVersion
    }
}
