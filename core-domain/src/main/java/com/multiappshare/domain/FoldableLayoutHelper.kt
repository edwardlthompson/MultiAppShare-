package com.multiappshare.domain

enum class FoldPosture {
    FLAT,
    HALF_OPENED,
    FLIPPED,
}

object FoldableLayoutHelper {
    fun shouldSplitTwoPane(
        screenWidthDp: Int,
        posture: FoldPosture,
        isSeparatingHinge: Boolean,
    ): Boolean {
        if (isSeparatingHinge && posture == FoldPosture.HALF_OPENED) return true
        return screenWidthDp >= 600
    }
}
