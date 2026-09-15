package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OverlayDestinationChipsTest {

    @Test
    fun shareOverlayShowsAppIcons() {
        assertTrue(OverlayDestinationChips.shouldShow(inShareMode = true, appCount = 3))
        assertFalse(OverlayDestinationChips.shouldShow(inShareMode = false, appCount = 3))
        assertFalse(OverlayDestinationChips.shouldShow(inShareMode = true, appCount = 0))
        assertEquals(6, OverlayDestinationChips.visibleCount(12))
        assertEquals(2, OverlayDestinationChips.visibleCount(2))
    }
}
