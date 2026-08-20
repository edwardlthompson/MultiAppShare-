package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ShareSessionSnapshotTest {

    @Test
    fun nonceIsStableForSamePayload() {
        val a = payloadNonce(listOf("content://a", "content://b"), "hi", "text/plain")
        val b = payloadNonce(listOf("content://b", "content://a"), "hi", "text/plain")
        assertEquals(a, b)
    }

    @Test
    fun nonceChangesWhenTextChanges() {
        val a = payloadNonce(listOf("content://a"), "one", "text/plain")
        val b = payloadNonce(listOf("content://a"), "two", "text/plain")
        assertTrue(a != b)
    }

    @Test
    fun canRestoreRequiresFreshPayload() {
        val now = 1_000_000L
        val fresh = ShareSessionSnapshot(text = "hello", savedAtMillis = now - 1_000)
        val stale = fresh.copy(savedAtMillis = now - SHARE_SNAPSHOT_MAX_AGE_MS - 1)
        val empty = ShareSessionSnapshot(savedAtMillis = now)
        assertTrue(fresh.canRestore(now))
        assertFalse(stale.canRestore(now))
        assertFalse(empty.canRestore(now))
    }
}
