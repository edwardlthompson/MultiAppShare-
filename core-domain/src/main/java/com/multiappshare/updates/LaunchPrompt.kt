package com.multiappshare.updates

sealed class LaunchPrompt {
    data object Donate : LaunchPrompt()
    data class Update(
        val version: String,
        val url: String,
        val listingOnly: Boolean = false,
    ) : LaunchPrompt()
}

object LaunchPromptDecider {
    suspend fun decide(
        currentVersion: String,
        lastSeenVersion: String?,
        lastCheckAt: Long?,
        dismissedVersion: String?,
        now: Long,
        fetchLatest: suspend () -> GithubRelease?,
        markSeen: (String) -> Unit,
        markChecked: (Long) -> Unit,
        allowDirectApk: Boolean = true,
    ): LaunchPrompt? {
        if (currentVersion.isBlank() || ProductUpdate.shouldNudgeDonate(lastSeenVersion, currentVersion)) {
            return donateOrNull(currentVersion, lastSeenVersion)
        }
        markSeen(currentVersion)
        return checkForUpdate(
            lastCheckAt,
            dismissedVersion,
            now,
            currentVersion,
            fetchLatest,
            markChecked,
            allowDirectApk,
        )
    }

    private fun donateOrNull(currentVersion: String, lastSeenVersion: String?): LaunchPrompt? {
        return if (ProductUpdate.shouldNudgeDonate(lastSeenVersion, currentVersion)) {
            LaunchPrompt.Donate
        } else {
            null
        }
    }

    private suspend fun checkForUpdate(
        lastCheckAt: Long?,
        dismissedVersion: String?,
        now: Long,
        currentVersion: String,
        fetchLatest: suspend () -> GithubRelease?,
        markChecked: (Long) -> Unit,
        allowDirectApk: Boolean,
    ): LaunchPrompt? {
        if (!ProductUpdate.shouldCheckDaily(lastCheckAt, now)) {
            return null
        }
        val release = runCatching { fetchLatest() }.getOrNull()
        val prompt = updateFromRelease(currentVersion, dismissedVersion, release, allowDirectApk)
        if (prompt == null) {
            markChecked(now)
        }
        return prompt
    }

    private fun updateFromRelease(
        currentVersion: String,
        dismissedVersion: String?,
        release: GithubRelease?,
        allowDirectApk: Boolean,
    ): LaunchPrompt? {
        val asset = release?.let { ProductUpdate.selectApkAsset(it.assets) } ?: return null
        val latest = asset.version
        return if (!ProductUpdate.shouldPromptUpdate(currentVersion, latest, dismissedVersion)) {
            null
        } else {
            val apk = asset.url.ifBlank { null }
                ?: release.htmlUrl.ifBlank { null }
                ?: ProductUpdate.RELEASES_PAGE
            val url = InstallChannel.updateUrl(allowDirectApk, apk)
            LaunchPrompt.Update(latest, url, listingOnly = !allowDirectApk)
        }
    }
}
