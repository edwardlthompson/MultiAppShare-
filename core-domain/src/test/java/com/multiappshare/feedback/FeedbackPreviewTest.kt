package com.multiappshare.feedback

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FeedbackPreviewTest {

    @Test
    fun blankTitleBecomesFeedback() {
        assertEquals("Feedback", FeedbackPreview.title("  "))
        assertEquals("Short", FeedbackPreview.title("Short"))
    }

    @Test
    fun bodySanitizesAndOptionallyAppendsCrash() {
        val body = FeedbackPreview.body("hi me@x.com", "token=ghp_abcdefghijklmnopqrstuv")
        assertFalse(body.contains("me@x.com"))
        assertFalse(body.contains("ghp_"))
        assertTrue(body.contains("--- crash ---"))
    }

    @Test
    fun omitsCrashSectionWhenNotAttached() {
        val body = FeedbackPreview.body("hello", null)
        assertEquals("hello", body)
        assertFalse(body.contains("--- crash ---"))
    }
}
