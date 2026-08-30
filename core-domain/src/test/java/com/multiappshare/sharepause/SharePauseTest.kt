package com.multiappshare.sharepause

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SharePauseTest {

    @Test
    fun blocksAdvanceWhilePaused() {
        assertTrue(SharePause.nextAllowed(paused = false))
        assertFalse(SharePause.nextAllowed(paused = true))
    }
}
