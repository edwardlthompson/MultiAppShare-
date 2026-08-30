package com.multiappshare.display

fun selectRefreshMode(
    highRefreshEnabled: Boolean,
    current: ModeCandidate,
    modes: List<ModeCandidate>,
): ModeCandidate? {
    if (!highRefreshEnabled) return null
    return pickFastestSameResolutionMode(current, modes)
}
