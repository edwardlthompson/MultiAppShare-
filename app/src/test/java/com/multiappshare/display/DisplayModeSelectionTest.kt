package com.multiappshare.display

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DisplayModeSelectionTest {

    @Test
    fun picksHighestRefreshAtSameResolution() {
        val current = ModeCandidate(1, 1080, 2400, 60f)
        val modes = listOf(
            ModeCandidate(1, 1080, 2400, 60f),
            ModeCandidate(2, 1080, 2400, 120f),
            ModeCandidate(3, 1440, 3200, 90f),
            ModeCandidate(4, 1080, 2400, 90f),
        )
        val best = pickFastestSameResolutionMode(current, modes)
        assertEquals(2, best?.modeId)
        assertEquals(120f, best?.refreshRate)
    }

    @Test
    fun returnsNullWhenNoMatchingResolution() {
        val current = ModeCandidate(1, 1080, 2400, 60f)
        val modes = listOf(ModeCandidate(2, 1440, 3200, 120f))
        assertNull(pickFastestSameResolutionMode(current, modes))
    }
}
