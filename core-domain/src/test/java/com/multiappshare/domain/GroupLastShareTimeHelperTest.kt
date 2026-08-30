package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class GroupLastShareTimeHelperTest {

    @Test
    fun formatRelativeTime_handlesVariousIntervals() {
        val now = 1_000_000_000L
        assertEquals("Never shared", GroupLastShareTimeHelper.formatRelativeTime(null, now))
        assertEquals("Never shared", GroupLastShareTimeHelper.formatRelativeTime(0L, now))
        assertEquals("Just now", GroupLastShareTimeHelper.formatRelativeTime(now - 30_000L, now))
        assertEquals("5m ago", GroupLastShareTimeHelper.formatRelativeTime(now - 5 * 60 * 1000L, now))
        assertEquals("2h ago", GroupLastShareTimeHelper.formatRelativeTime(now - 2 * 60 * 60 * 1000L, now))
        assertEquals("3d ago", GroupLastShareTimeHelper.formatRelativeTime(now - 3 * 24 * 60 * 60 * 1000L, now))
    }
}
