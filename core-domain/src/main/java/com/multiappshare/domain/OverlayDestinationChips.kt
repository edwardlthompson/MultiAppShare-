package com.multiappshare.domain

object OverlayDestinationChips {
    const val MAX_ICONS = 6

    fun shouldShow(inShareMode: Boolean, appCount: Int): Boolean =
        inShareMode && appCount > 0

    fun visibleCount(appCount: Int): Int = appCount.coerceAtMost(MAX_ICONS)
}
