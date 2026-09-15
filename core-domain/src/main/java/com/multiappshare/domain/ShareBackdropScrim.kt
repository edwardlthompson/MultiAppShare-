package com.multiappshare.domain

object ShareBackdropScrim {
    const val MATERIAL_MIN = 0.32f
    const val MATERIAL_MAX = 0.60f
    const val DEFAULT = 0.54f
    const val LOW_RAM = 0.60f

    fun alpha(lowRam: Boolean): Float {
        val raw = if (lowRam) LOW_RAM else DEFAULT
        return raw.coerceIn(MATERIAL_MIN, MATERIAL_MAX)
    }
}
