package com.multiappshare.domain

object SharingDelay {
    const val MIN_MS = 0
    const val MAX_MS = 5000
    val PRESETS_MS = listOf(0, 500, 1000, 2000)

    enum class PresetCopy { NONE, SHORT, ONE, MANY }

    fun clamp(ms: Int): Int = ms.coerceIn(MIN_MS, MAX_MS)

    fun presetCopy(ms: Int): PresetCopy = when (ms) {
        0 -> PresetCopy.NONE
        500 -> PresetCopy.SHORT
        1000 -> PresetCopy.ONE
        else -> PresetCopy.MANY
    }

    fun wholeSeconds(ms: Int): Int = ms / 1000
}
