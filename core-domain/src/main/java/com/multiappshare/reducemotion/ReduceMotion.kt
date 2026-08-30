package com.multiappshare.reducemotion

object ReduceMotion {
    fun skipBurst(animatorScale: Float, transitionScale: Float): Boolean =
        animatorScale <= 0f || transitionScale <= 0f
}
