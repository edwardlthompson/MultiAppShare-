package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class WhatsNewHelperTest {

    @Test
    fun shouldShowWhatsNew_onlyWhenUpgradeDetected() {
        assertFalse(WhatsNewHelper.shouldShowWhatsNew(lastSeenVersionCode = 0, currentVersionCode = 100))
        assertFalse(WhatsNewHelper.shouldShowWhatsNew(lastSeenVersionCode = 100, currentVersionCode = 100))
        assertTrue(WhatsNewHelper.shouldShowWhatsNew(lastSeenVersionCode = 99, currentVersionCode = 100))
    }

    @Test
    fun getHighlightsForVersion_returnsCatalogOrFallback() {
        val knownHighlights = WhatsNewHelper.getHighlightsForVersion("v1.0.0")
        assertTrue(knownHighlights.isNotEmpty())
        val unknownHighlights = WhatsNewHelper.getHighlightsForVersion("v99.0.0")
        assertEquals(listOf("Bug fixes and performance improvements"), unknownHighlights)
    }
}
