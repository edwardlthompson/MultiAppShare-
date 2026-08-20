package com.multiappshare.domain

object SharingDelay {
    const val MIN_MS = 0
    const val MAX_MS = 5000
    val PRESETS_MS = listOf(0, 500, 1000, 2000)

    fun clamp(ms: Int): Int = ms.coerceIn(MIN_MS, MAX_MS)
}
