package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.ZoneOffset
import java.util.Locale

class HistoryPresentationTest {

    @Test
    fun timestampFollowsLocaleNotEnglishPattern() {
        val ts = 1_700_000_000_000L
        val zone = ZoneOffset.UTC
        val us = HistoryPresentation.formatTimestamp(ts, Locale.US, zone)
        val german = HistoryPresentation.formatTimestamp(ts, Locale.GERMANY, zone)
        assertNotEquals(us, german)
        assertFalse(us.contains("Nov"))
        assertTrue(german.contains("14") || german.contains("11"))
    }

    @Test
    fun sharedLineFormatsDescription() {
        assertEquals("Shared Text", HistoryPresentation.sharedLine("Shared %s", "Text"))
        assertEquals("photo", HistoryPresentation.sharedLine("", "photo"))
    }
}
