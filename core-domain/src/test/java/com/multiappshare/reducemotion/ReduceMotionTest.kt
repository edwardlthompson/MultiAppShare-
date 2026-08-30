package com.multiappshare.reducemotion

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReduceMotionTest {

    @Test
    fun skipsWhenSystemScaleDisabled() {
        assertTrue(ReduceMotion.skipBurst(animatorScale = 0f, transitionScale = 1f))
        assertTrue(ReduceMotion.skipBurst(animatorScale = 1f, transitionScale = 0f))
        assertFalse(ReduceMotion.skipBurst(animatorScale = 1f, transitionScale = 1f))
        assertFalse(ReduceMotion.skipBurst(animatorScale = 0.5f, transitionScale = 0.5f))
    }
}
