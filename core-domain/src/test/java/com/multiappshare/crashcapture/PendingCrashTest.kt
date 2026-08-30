package com.multiappshare.crashcapture

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PendingCrashTest {

    @Test
    fun persistOnlyWhenEnabledAndNonBlank() {
        assertFalse(PendingCrash.shouldPersist(false, "boom"))
        assertFalse(PendingCrash.shouldPersist(true, null))
        assertFalse(PendingCrash.shouldPersist(true, "  "))
        assertTrue(PendingCrash.shouldPersist(true, "boom"))
    }

    @Test
    fun sanitizeRedactsEmail() {
        val out = PendingCrash.sanitize("crash at a@b.com")
        assertFalse(out.contains("a@b.com"))
    }
}
