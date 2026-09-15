package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ShareSessionKeysTest {

    @Test
    fun enterAdvancesUnlessPaused() {
        assertEquals(ShareSessionKeyAction.NEXT, ShareSessionKeys.action("Enter", paused = false))
        assertEquals(ShareSessionKeyAction.IGNORE, ShareSessionKeys.action("Enter", paused = true))
    }

    @Test
    fun escapeCancelsAndBackIsUnclaimed() {
        assertEquals(ShareSessionKeyAction.CANCEL, ShareSessionKeys.action("Escape", paused = false))
        assertEquals(ShareSessionKeyAction.IGNORE, ShareSessionKeys.action("Back", paused = false))
        assertFalse(ShareSessionKeys.INTERCEPTS_BACK)
    }
}
