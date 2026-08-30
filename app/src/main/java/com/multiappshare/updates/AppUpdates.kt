package com.multiappshare.updates

import android.content.Context

object AppUpdates {
    suspend fun evaluate(context: Context, currentVersion: String): LaunchPrompt? {
        val prefs = UpdatePrefs(context)
        return LaunchPromptDecider.decide(
            currentVersion = currentVersion,
            lastSeenVersion = prefs.lastSeenVersion(),
            lastCheckAt = prefs.lastCheckAt(),
            dismissedVersion = prefs.dismissedVersion(),
            now = System.currentTimeMillis(),
            fetchLatest = { GithubReleaseFetcher.fetchLatest(currentVersion) },
            markSeen = { prefs.markVersionSeen(it) },
            markChecked = { prefs.markChecked(it) },
            allowDirectApk = InstallChannel.allowsDirectApk(InstallSource.installerPackage(context)),
        )
    }
}
