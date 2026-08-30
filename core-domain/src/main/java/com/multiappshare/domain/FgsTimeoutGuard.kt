package com.multiappshare.domain

object FgsTimeoutGuard {
    const val DEFAULT_MAX_FGS_DURATION_MS = 6 * 60 * 1000L

    fun shouldDemoteOrStop(elapsedMs: Long, maxAllowedMs: Long = DEFAULT_MAX_FGS_DURATION_MS): Boolean {
        return elapsedMs >= maxAllowedMs
    }
}
