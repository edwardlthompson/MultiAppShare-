package com.multiappshare.share

import com.multiappshare.ShareSessionState
import com.multiappshare.domain.HistoryPayload
import com.multiappshare.domain.ShareSessionSnapshot
import com.multiappshare.domain.ShareSessionStore
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ShareFailAndHistoryRestoreTest {

    @Test
    fun markShareFailed_keepsCurrentIndex() {
        val session = ShareSessionState(
            text = "x",
            mimeType = "text/plain",
            appPackages = listOf("a/.A", "b/.B"),
            currentIndex = 1,
            sharingStarted = true,
        )
        val after = session.copy(lastShareFailed = true)
        assertEquals(1, after.currentIndex)
        assertTrue(after.lastShareFailed)
        assertFalse(session.lastShareFailed)
    }

    @Test
    fun restoreHistoryPayload_opensOverlayWithoutStarting() {
        val coordinator = ShareSessionCoordinator(mockk<ShareSessionStore>(relaxed = true), nowMillis = { 0L })
        val json = HistoryPayload.encode(ShareSessionSnapshot(text = "clip", mimeType = "text/plain"))
        val restored = coordinator.restoreHistoryPayload(json)
        assertEquals("clip", restored?.text)
        assertEquals(false, restored?.sharingStarted)
        assertEquals(0, restored?.currentIndex)
    }

    @Test
    fun retryFailedStep_resetsFailedFlag() {
        val failedSession = ShareSessionState(
            text = "x",
            mimeType = "text/plain",
            appPackages = listOf("a/.A", "b/.B"),
            currentIndex = 0,
            sharingStarted = true,
            lastShareFailed = true,
        )
        val retried = failedSession.copy(lastShareFailed = false)
        assertFalse(retried.lastShareFailed)
        assertEquals(0, retried.currentIndex)
    }
}
