package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppInfoShortcutHelperTest {

    @Test
    fun buildUri_createsPackageUri() {
        assertEquals("package:org.thoughtcrime.securesms", AppInfoShortcutHelper.buildUri("org.thoughtcrime.securesms"))
        assertEquals("package:com.example.app", AppInfoShortcutHelper.buildUri(" com.example.app "))
    }

    @Test
    fun isValidPackageName_validatesPackageNames() {
        assertTrue(AppInfoShortcutHelper.isValidPackageName("com.example.app"))
        assertTrue(AppInfoShortcutHelper.isValidPackageName("org.thoughtcrime.securesms"))
        assertFalse(AppInfoShortcutHelper.isValidPackageName(null))
        assertFalse(AppInfoShortcutHelper.isValidPackageName(""))
        assertFalse(AppInfoShortcutHelper.isValidPackageName("invalidpackage"))
    }
}
