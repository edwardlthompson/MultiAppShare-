package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class SharingHeadlineTest {

    @Test
    fun titlePrefersTheCurrentAppName() {
        assertEquals("WhatsApp", SharingHeadline.title("WhatsApp", "a photo"))
        assertEquals("a photo", SharingHeadline.title("", "a photo"))
        assertEquals("a photo", SharingHeadline.title("   ", "a photo"))
    }

    @Test
    fun nextAppLineIsNotASecondHeadline() {
        assertFalse(SharingHeadline.SHOW_NEXT_APP_LINE)
    }
}
