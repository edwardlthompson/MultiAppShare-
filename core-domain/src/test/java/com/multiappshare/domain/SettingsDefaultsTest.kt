package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsDefaultsTest {

    @Test
    fun crashSaveDefaultsOffAndHighRefreshOn() {
        assertFalse(SettingsDefaults.CRASH_CAPTURE_ENABLED)
        assertTrue(SettingsDefaults.HIGH_REFRESH_ENABLED)
    }
}
