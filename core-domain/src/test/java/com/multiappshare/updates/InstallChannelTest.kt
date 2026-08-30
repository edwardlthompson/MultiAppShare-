package com.multiappshare.updates

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InstallChannelTest {

    @Test
    fun sideloadAndUnknownAllowApk() {
        assertTrue(InstallChannel.allowsDirectApk(null))
        assertTrue(InstallChannel.allowsDirectApk(""))
        assertTrue(InstallChannel.allowsDirectApk("com.android.packageinstaller"))
        assertTrue(InstallChannel.allowsDirectApk("com.google.android.packageinstaller"))
    }

    @Test
    fun fdroidFamilyNeverAllowsApk() {
        assertFalse(InstallChannel.allowsDirectApk("org.fdroid.fdroid"))
        assertFalse(InstallChannel.allowsDirectApk("org.fdroid.basic"))
        assertFalse(InstallChannel.allowsDirectApk("com.looker.droidify"))
        assertFalse(InstallChannel.allowsDirectApk("org.example.fdroid.fork"))
    }

    @Test
    fun listingUrlReplacesApkOnFdroid() {
        val apk = "https://github.com/x/y/releases/download/v1/a.apk"
        assertEquals(apk, InstallChannel.updateUrl(true, apk))
        assertEquals(InstallChannel.FDROID_LISTING, InstallChannel.updateUrl(false, apk))
        assertTrue(InstallChannel.FDROID_LISTING.startsWith("https://"))
    }
}
