package com.multiappshare.sharedefer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ShareDeferTest {

    @Test
    fun offersOnlyWhenFailedAndMoreRemain() {
        assertFalse(ShareDefer.shouldOffer(lastShareFailed = false, remainingAfterCurrent = 2))
        assertFalse(ShareDefer.shouldOffer(lastShareFailed = true, remainingAfterCurrent = 0))
        assertTrue(ShareDefer.shouldOffer(lastShareFailed = true, remainingAfterCurrent = 1))
    }

    @Test
    fun movesCurrentToEndAndKeepsIndex() {
        val result = ShareDefer.moveCurrentToEnd(listOf("a", "b", "c"), 0)
        assertEquals(listOf("b", "c", "a"), result.packages)
        assertEquals(0, result.currentIndex)
    }

    @Test
    fun lastItemAndBadIndexAreNoOp() {
        assertEquals(listOf("a", "b"), ShareDefer.moveCurrentToEnd(listOf("a", "b"), 1).packages)
        assertEquals(listOf("a"), ShareDefer.moveCurrentToEnd(listOf("a"), 0).packages)
        assertEquals(listOf("a", "b"), ShareDefer.moveCurrentToEnd(listOf("a", "b"), -1).packages)
    }
}
