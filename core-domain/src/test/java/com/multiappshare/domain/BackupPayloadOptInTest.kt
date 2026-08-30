package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BackupPayloadOptInTest {

    private val sampleSnapshot = ShareSessionSnapshot(
        text = "https://example.com/item",
        mimeType = "text/plain",
    )

    @Test
    fun returnsNullWhenOptInIsFalse() {
        val result = BackupPayloadOptIn.resolvePayloadForBackup(
            lastPayload = sampleSnapshot,
            includePayloadInBackup = false,
        )
        assertNull(result)
    }

    @Test
    fun returnsSnapshotWhenOptInIsTrue() {
        val result = BackupPayloadOptIn.resolvePayloadForBackup(
            lastPayload = sampleSnapshot,
            includePayloadInBackup = true,
        )
        assertEquals(sampleSnapshot, result)
    }

    @Test
    fun returnsNullWhenPayloadIsEmptyEvenIfOptInIsTrue() {
        val emptySnapshot = ShareSessionSnapshot(text = null, uris = emptyList())
        val result = BackupPayloadOptIn.resolvePayloadForBackup(
            lastPayload = emptySnapshot,
            includePayloadInBackup = true,
        )
        assertNull(result)
    }
}
