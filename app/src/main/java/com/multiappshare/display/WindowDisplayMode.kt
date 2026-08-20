package com.multiappshare.display

import android.view.Window

/**
 * Prefer the display's fastest refresh rate at the current physical resolution
 * via [android.view.WindowManager.LayoutParams.preferredDisplayModeId].
 */
fun Window.enableFastestSameResolutionMode() {
    val display = decorView.display ?: return
    val currentMode = display.mode
    val current = ModeCandidate(
        modeId = currentMode.modeId,
        width = currentMode.physicalWidth,
        height = currentMode.physicalHeight,
        refreshRate = currentMode.refreshRate,
    )
    val candidates = display.supportedModes.map { mode ->
        ModeCandidate(
            modeId = mode.modeId,
            width = mode.physicalWidth,
            height = mode.physicalHeight,
            refreshRate = mode.refreshRate,
        )
    }
    val best = pickFastestSameResolutionMode(current, candidates)
    if (best == null || attributes.preferredDisplayModeId == best.modeId) return
    val lp = attributes
    lp.preferredDisplayModeId = best.modeId
    attributes = lp
}
