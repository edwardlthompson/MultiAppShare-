package com.multiappshare.share

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ShareDeeplinkBuilderTest {

    @Test
    fun buildsAndParsesUri() {
        val uri = ShareDeeplinkBuilder.buildUri(3)
        assertEquals("multiappshare://share_step?index=3", uri)
        assertEquals(3, ShareDeeplinkBuilder.parseStepIndex(uri))
    }

    @Test
    fun returnsNullForInvalidUri() {
        assertNull(ShareDeeplinkBuilder.parseStepIndex(null))
        assertNull(ShareDeeplinkBuilder.parseStepIndex("https://example.com"))
        assertNull(ShareDeeplinkBuilder.parseStepIndex("multiappshare://group?id=123"))
        assertNull(ShareDeeplinkBuilder.parseStepIndex("multiappshare://share_step?index=abc"))
    }
}
