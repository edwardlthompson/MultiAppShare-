package com.multiappshare.domain

import org.junit.Assert.assertTrue
import org.junit.Test

class OverflowFeedbackHelperTest {

    @Test
    fun formatsValidFeedbackIssueUrl() {
        val url = OverflowFeedbackHelper.formatFeedbackUrl("1.0.0", 34)
        assertTrue(url.startsWith("https://github.com/edwardlthompson/MultiAppShare-/issues/new?"))
        assertTrue(url.contains("title="))
        assertTrue(url.contains("body="))
    }
}
