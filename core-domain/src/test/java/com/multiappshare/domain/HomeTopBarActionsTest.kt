package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeTopBarActionsTest {

    @Test
    fun reorderAppearsWhenTwoOrMoreGroupsExist() {
        assertFalse(HomeTopBarActions.showReorder(0))
        assertFalse(HomeTopBarActions.showReorder(1))
        assertTrue(HomeTopBarActions.showReorder(2))
        assertTrue(HomeTopBarActions.canReorderWithoutOverflow(3))
    }

    @Test
    fun overflowKeepsSortAsFallback() {
        assertEquals("swap_vert", HomeTopBarActions.REORDER_ICON_KEY)
        assertTrue(
            OverflowMenuPolicy.items.any { it.destination == OverflowDestination.SORT_GROUPS },
        )
    }
}
