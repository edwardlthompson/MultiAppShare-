package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EmptyGroupsCtaHelperTest {

    @Test
    fun shouldShowEmptyCta_checksEmptyCount() {
        assertTrue(EmptyGroupsCtaHelper.shouldShowEmptyCta(0))
        assertFalse(EmptyGroupsCtaHelper.shouldShowEmptyCta(1))
        assertFalse(EmptyGroupsCtaHelper.shouldShowEmptyCta(10))
    }
}
