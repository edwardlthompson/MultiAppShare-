package com.multiappshare.updates

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class UpdatePrefsTest {

    @Test
    fun dismissAndSeenStayOnDevicePrefs() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = UpdatePrefs(context)
        assertNull(prefs.lastSeenVersion())
        assertNull(prefs.dismissedVersion())
        prefs.markVersionSeen("1.9.4")
        prefs.markChecked(1_000L, "1.9.5")
        assertEquals("1.9.4", prefs.lastSeenVersion())
        assertEquals("1.9.5", prefs.dismissedVersion())
        assertEquals(1_000L, prefs.lastCheckAt())
    }
}
