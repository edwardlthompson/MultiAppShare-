package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MimeMismatchExplanationTest {

    @Test
    fun checksGroupCompatibility() {
        assertTrue(
            MimeMismatchExplanation.isGroupCompatible(
                groupAppPackageKeys = setOf("com.example.app/MainActivity"),
                compatiblePackageKeys = setOf("com.example.app/MainActivity", "com.other/Main"),
            ),
        )
        assertFalse(
            MimeMismatchExplanation.isGroupCompatible(
                groupAppPackageKeys = setOf("com.example.app/MainActivity"),
                compatiblePackageKeys = setOf("com.other/Main"),
            ),
        )
        assertFalse(
            MimeMismatchExplanation.isGroupCompatible(
                groupAppPackageKeys = emptySet(),
                compatiblePackageKeys = setOf("com.other/Main"),
            ),
        )
    }

    @Test
    fun humanizesMimeKinds() {
        assertEquals("image", MimeMismatchExplanation.mimeKind("image/jpeg"))
        assertEquals("video", MimeMismatchExplanation.mimeKind("video/mp4"))
        assertEquals("text", MimeMismatchExplanation.mimeKind("text/plain"))
        assertEquals("other", MimeMismatchExplanation.mimeKind("*/*"))
        assertEquals("other", MimeMismatchExplanation.mimeKind(null))
    }
}
