package com.multiappshare.sharehaptics

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ShareHapticsTest {

    @Test
    fun performsOnlyWhenEnabled() {
        assertTrue(ShareHaptics.shouldPerform(enabled = true))
        assertFalse(ShareHaptics.shouldPerform(enabled = false))
    }
}
