package com.multiappshare.shareoverlay

import com.multiappshare.sharedefer.ShareDefer

object ShareOverlayActions {
    const val PRIMARY_STICKY = true

    fun primaryIsFinish(currentIndex: Int, totalApps: Int): Boolean =
        currentIndex + 1 >= totalApps

    fun showSkipActions(currentIndex: Int, totalApps: Int): Boolean =
        currentIndex + 1 < totalApps

    fun showTryLater(lastShareFailed: Boolean, remainingAfterCurrent: Int): Boolean =
        ShareDefer.shouldOffer(lastShareFailed, remainingAfterCurrent)

    fun progressFraction(currentIndex: Int, totalApps: Int): Float =
        (currentIndex + 1f) / totalApps.coerceAtLeast(1)
}
