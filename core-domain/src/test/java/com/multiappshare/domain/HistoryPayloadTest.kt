package com.multiappshare.domain

import com.multiappshare.model.HistoryItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HistoryPayloadTest {

    @Test
    fun encodeDecode_roundTripsTextPayload() {
        val snap = ShareSessionSnapshot(
            text = "hello",
            mimeType = "text/plain",
            currentIndex = 3,
            sharingStarted = true,
        )
        val raw = HistoryPayload.encode(snap)
        val decoded = HistoryPayload.decode(raw)
        assertEquals("hello", decoded?.text)
        assertEquals("text/plain", decoded?.mimeType)
        assertEquals(0, decoded?.currentIndex)
        assertEquals(false, decoded?.sharingStarted)
        assertTrue(decoded?.hasPayload() == true)
    }

    @Test
    fun reshareSnapshot_nullWhenMissingOrEmpty() {
        val empty = HistoryItem(timestamp = 1, groupName = "G", contentDescription = "c", status = "s")
        assertNull(empty.let { HistoryPayload.run { it.reshareSnapshot() } })
        val blank = empty.copy(payloadJson = "{}")
        assertNull(HistoryPayload.run { blank.reshareSnapshot() })
    }
}
