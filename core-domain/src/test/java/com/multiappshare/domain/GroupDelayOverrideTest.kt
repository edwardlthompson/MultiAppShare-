package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class GroupDelayOverrideTest {

    @Test
    fun usesGlobalWhenGroupOverrideNull() {
        assertEquals(500, GroupDelayOverride.resolveDelayMs(500, null))
        assertEquals(1000, GroupDelayOverride.resolveDelayMs(1000, null))
    }

    @Test
    fun usesGroupOverrideWhenPresent() {
        assertEquals(2000, GroupDelayOverride.resolveDelayMs(500, 2000))
        assertEquals(0, GroupDelayOverride.resolveDelayMs(500, 0))
    }

    @Test
    fun clampsValues() {
        assertEquals(5000, GroupDelayOverride.resolveDelayMs(500, 99999))
        assertEquals(0, GroupDelayOverride.resolveDelayMs(500, -100))
    }
}
