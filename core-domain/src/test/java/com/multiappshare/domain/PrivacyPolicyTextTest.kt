package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PrivacyPolicyTextTest {

    @Test
    fun containsStandardPrivacyClauses() {
        assertTrue(PrivacyPolicyText.SECTIONS.isNotEmpty())
        for ((heading, body) in PrivacyPolicyText.SECTIONS) {
            assertTrue(heading.isNotBlank())
            assertTrue(body.isNotBlank())
            assertFalse(body.contains("Firebase"))
            assertFalse(body.contains("Google Analytics"))
        }
    }
}
