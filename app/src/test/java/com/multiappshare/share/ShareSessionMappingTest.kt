package com.multiappshare.share

import com.multiappshare.ShareSessionState
import com.multiappshare.domain.canRestore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShareSessionMappingTest {

    @Test
    fun roundTripPreservesTextAndMime() {
        val state = ShareSessionState(
            text = "hello",
            mimeType = "text/plain",
            currentIndex = 2,
            paused = true,
        )
        val snap = state.toSnapshot(nowMillis = 50)
        val restored = snap.toState()
        assertEquals("hello", restored.text)
        assertEquals("text/plain", restored.mimeType)
        assertEquals(2, restored.currentIndex)
        assertTrue(restored.paused)
        assertTrue(snap.canRestore(50))
    }
}
