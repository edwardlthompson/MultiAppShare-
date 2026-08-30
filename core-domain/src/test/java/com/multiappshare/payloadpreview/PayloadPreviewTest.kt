package com.multiappshare.payloadpreview

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PayloadPreviewTest {

    @Test
    fun showsOnlyBeforeFirstHandoff() {
        assertTrue(PayloadPreview.shouldShow(hasPayload = true, sharingStarted = false))
        assertFalse(PayloadPreview.shouldShow(hasPayload = true, sharingStarted = true))
        assertFalse(PayloadPreview.shouldShow(hasPayload = false, sharingStarted = false))
    }

    @Test
    fun mimeAndUriAndSnippet() {
        assertEquals("*/*", PayloadPreview.mimeLabel(null))
        assertEquals("image/jpeg", PayloadPreview.mimeLabel("image/jpeg"))
        assertEquals(2, PayloadPreview.uriCount(listOf("a", "b")))
        assertEquals(0, PayloadPreview.uriCount(null))
        assertEquals("hello", PayloadPreview.textSnippet("  hello  "))
        val long = "x".repeat(200)
        val snippet = PayloadPreview.textSnippet(long)
        assertTrue(snippet.endsWith("…"))
        assertTrue(snippet.length <= PayloadPreview.TEXT_LIMIT + 1)
    }
}
