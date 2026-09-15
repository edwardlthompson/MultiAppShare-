package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GroupListFilterTest {

    @Test
    fun showClearWhenQueryNonEmpty() {
        assertFalse(GroupListFilter.showClear(""))
        assertTrue(GroupListFilter.showClear("So"))
    }

    @Test
    fun matchesIgnoresBlankQueryAndCase() {
        assertTrue(GroupListFilter.matches("Social", ""))
        assertTrue(GroupListFilter.matches("Social", "soc"))
        assertFalse(GroupListFilter.matches("Social", "work"))
    }
}
