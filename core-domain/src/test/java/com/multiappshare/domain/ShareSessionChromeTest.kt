package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ShareSessionChromeTest {

    @Test
    fun progressTitleOnlyWhileSharingAQueue() {
        assertFalse(ShareSessionChrome.showProgressTitle(false, 5))
        assertFalse(ShareSessionChrome.showProgressTitle(true, 0))
        assertTrue(ShareSessionChrome.showProgressTitle(true, 5))
    }

    @Test
    fun barAndNotificationShareTheSameIdentity() {
        val bar = ShareSessionChrome.identity("WhatsApp", 1, 5)
        val notification = ShareSessionChrome.identity("WhatsApp", 1, 5)
        assertEquals(ShareSessionChrome.Identity(2, 5, "WhatsApp"), bar)
        assertEquals(bar, notification)
        assertNull(ShareSessionChrome.identity("  ", 0, 5))
        assertNull(ShareSessionChrome.identity("WhatsApp", 0, 0))
    }
}
