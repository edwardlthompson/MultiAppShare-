package com.multiappshare.domain

object CrashReviewSnoozeHelper {
    fun shouldShowPrompt(
        lastSnoozedVersionCode: Int?,
        currentVersionCode: Int,
        hasUnreviewedCrashes: Boolean,
    ): Boolean {
        if (!hasUnreviewedCrashes) return false
        if (lastSnoozedVersionCode == null) return true
        return currentVersionCode > lastSnoozedVersionCode
    }

    fun snoozeForCurrentVersion(currentVersionCode: Int): Int = currentVersionCode
}
