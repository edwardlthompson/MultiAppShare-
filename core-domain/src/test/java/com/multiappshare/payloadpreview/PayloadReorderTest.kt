package com.multiappshare.payloadpreview

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PayloadReorderTest {

    @Test
    fun offersOnlyForMultipleUris() {
        assertFalse(PayloadReorder.shouldOffer(0))
        assertFalse(PayloadReorder.shouldOffer(1))
        assertTrue(PayloadReorder.shouldOffer(2))
    }

    @Test
    fun moveSwapsAndIgnoresBadIndexes() {
        assertEquals(listOf("b", "a", "c"), PayloadReorder.move(listOf("a", "b", "c"), 0, 1))
        assertEquals(listOf("a", "b"), PayloadReorder.move(listOf("a", "b"), -1, 0))
        assertEquals(listOf("a", "b"), PayloadReorder.move(listOf("a", "b"), 0, 0))
    }
}
