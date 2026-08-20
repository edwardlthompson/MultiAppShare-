package com.multiappshare.display

/**
 * Display mode fields used to pick the fastest refresh rate at a fixed resolution.
 */
data class ModeCandidate(
    val modeId: Int,
    val width: Int,
    val height: Int,
    val refreshRate: Float,
)

/**
 * Returns the highest-refresh mode that matches [current]'s physical resolution,
 * or null if none match.
 */
fun pickFastestSameResolutionMode(
    current: ModeCandidate,
    modes: List<ModeCandidate>,
): ModeCandidate? =
    modes
        .asSequence()
        .filter { it.width == current.width && it.height == current.height }
        .maxByOrNull { it.refreshRate }
