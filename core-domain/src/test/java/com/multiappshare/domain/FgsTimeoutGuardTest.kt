package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FgsTimeoutGuardTest {

    @Test
    fun allowsNormalSharingDurations() {
        assertFalse(FgsTimeoutGuard.shouldDemoteOrStop(0L))
        assertFalse(FgsTimeoutGuard.shouldDemoteOrStop(30_000L))
        assertFalse(FgsTimeoutGuard.shouldDemoteOrStop(5 * 60 * 1000L))
    }

    @Test
    fun flagsServiceWhenExceedingAndroid15TimeoutThreshold() {
        assertTrue(FgsTimeoutGuard.shouldDemoteOrStop(6 * 60 * 1000L))
        assertTrue(FgsTimeoutGuard.shouldDemoteOrStop(10 * 60 * 1000L))
    }
}
