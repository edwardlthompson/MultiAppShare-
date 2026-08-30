package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MimeMismatchExplanationTest {

    @Test
    fun formatsExplanationString() {
        val exp = MimeMismatchExplanation.formatExplanation("Social", "image/png")
        assertEquals("No apps in \"Social\" support content type image/png", exp)
    }

    @Test
    fun formatsExplanationStringFallbackMime() {
        val exp = MimeMismatchExplanation.formatExplanation("Social", "")
        assertEquals("No apps in \"Social\" support content type */*", exp)
    }

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
}
