package com.multiappshare.domain

import org.junit.Assert.assertTrue
import org.junit.Test

class FeatureSpecHonestyTest {

    @Test
    fun userVisibleLocksNameAComposable() {
        FeatureSpecHonesty.REVIEW_LOCKS.forEach { lock ->
            assertTrue(lock.slug, FeatureSpecHonesty.userVisibleHasComposable(lock))
        }
    }
}
