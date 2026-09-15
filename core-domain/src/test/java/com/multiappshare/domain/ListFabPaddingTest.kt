package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ListFabPaddingTest {

    @Test
    fun homeListClearsTheSingleFab() {
        assertTrue(ListFabPadding.SINGLE_FAB_DP >= 80)
        assertEquals(ListFabPadding.SINGLE_FAB_DP, ListFabPadding.bottomDp(inShareMode = false))
    }

    @Test
    fun shareModeNeedsNoFabClearance() {
        assertEquals(0, ListFabPadding.bottomDp(inShareMode = true))
    }
}
