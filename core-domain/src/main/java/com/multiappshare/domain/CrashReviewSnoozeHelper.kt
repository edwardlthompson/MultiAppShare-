package com.multiappshare.domain

object CrashReviewSnoozeHelper {
    fun shouldShowPrompt(
        lastSnoozedVersionCode: Int?,
        currentVersionCode: Int,
        hasUnreviewedCrashes: Boolean,
    ): Boolean {
        if (!hasUnreviewedCrashes) return false
        return lastSnoozedVersionCode == null || currentVersionCode > lastSnoozedVersionCode
    }

    fun snoozeForCurrentVersion(currentVersionCode: Int): Int = currentVersionCode
}
