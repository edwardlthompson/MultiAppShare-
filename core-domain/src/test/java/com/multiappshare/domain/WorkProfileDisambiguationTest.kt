package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WorkProfileDisambiguationTest {

    @Test
    fun formatAppLabel_appendsWorkSuffixWhenWorkProfile() {
        assertEquals("Slack (Work)", WorkProfileDisambiguation.formatAppLabel("Slack", isWorkProfile = true))
        assertEquals("Slack", WorkProfileDisambiguation.formatAppLabel("Slack", isWorkProfile = false))
    }

    @Test
    fun isWorkProfileUser_identifiesNonZeroUserIds() {
        assertFalse(WorkProfileDisambiguation.isWorkProfileUser(0))
        assertTrue(WorkProfileDisambiguation.isWorkProfileUser(10))
        assertTrue(WorkProfileDisambiguation.isWorkProfileUser(11))
    }
}
