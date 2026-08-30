package com.multiappshare.domain

object FontScaleLayoutHelper {
    const val MAX_ACCESSIBILITY_FONT_SCALE = 2.0f

    fun isLargeFont(fontScale: Float): Boolean = fontScale >= 1.5f

    fun resolveDialogMaxHeightFraction(fontScale: Float): Float {
        return if (isLargeFont(fontScale)) 0.90f else 0.80f
    }
}
