package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PackageChangeFilterTest {

    @Test
    fun identifiesRelevantPackageActions() {
        assertTrue(PackageChangeFilter.isRelevantAction(PackageChangeFilter.ACTION_PACKAGE_ADDED))
        assertTrue(PackageChangeFilter.isRelevantAction(PackageChangeFilter.ACTION_PACKAGE_REMOVED))
        assertTrue(PackageChangeFilter.isRelevantAction(PackageChangeFilter.ACTION_PACKAGE_REPLACED))
        assertTrue(PackageChangeFilter.isRelevantAction(PackageChangeFilter.ACTION_PACKAGE_CHANGED))
    }

    @Test
    fun rejectsIrrelevantActions() {
        assertFalse(PackageChangeFilter.isRelevantAction(null))
        assertFalse(PackageChangeFilter.isRelevantAction("android.intent.action.BOOT_COMPLETED"))
        assertFalse(PackageChangeFilter.isRelevantAction("some.random.action"))
    }
}
