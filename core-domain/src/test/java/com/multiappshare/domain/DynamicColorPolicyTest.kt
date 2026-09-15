package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DynamicColorPolicyTest {

    @Test
    fun defaultsOnAndRequiresAndroid12() {
        assertTrue(DynamicColorPolicy.DEFAULT_ENABLED)
        assertTrue(DynamicColorPolicy.useWallpaperColors(31, userEnabled = true))
        assertFalse(DynamicColorPolicy.useWallpaperColors(30, userEnabled = true))
        assertFalse(DynamicColorPolicy.useWallpaperColors(33, userEnabled = false))
    }
}
