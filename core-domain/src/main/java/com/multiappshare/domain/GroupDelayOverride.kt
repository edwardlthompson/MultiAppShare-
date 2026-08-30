package com.multiappshare.domain

object GroupDelayOverride {
    fun resolveDelayMs(globalDelayMs: Int, groupDelayMs: Int?): Int {
        if (groupDelayMs == null) return SharingDelay.clamp(globalDelayMs)
        return SharingDelay.clamp(groupDelayMs)
    }
}
