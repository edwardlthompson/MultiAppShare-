package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CrashReviewSnoozeHelperTest {

    @Test
    fun shouldShowPrompt_evaluatesVersionSnooze() {
        assertFalse(CrashReviewSnoozeHelper.shouldShowPrompt(null, 100, hasUnreviewedCrashes = false))
        assertTrue(CrashReviewSnoozeHelper.shouldShowPrompt(null, 100, hasUnreviewedCrashes = true))
        assertFalse(CrashReviewSnoozeHelper.shouldShowPrompt(100, 100, hasUnreviewedCrashes = true))
        assertTrue(CrashReviewSnoozeHelper.shouldShowPrompt(99, 100, hasUnreviewedCrashes = true))
    }

    @Test
    fun snoozeForCurrentVersion_returnsVersionCode() {
        assertEquals(105, CrashReviewSnoozeHelper.snoozeForCurrentVersion(105))
    }
}
