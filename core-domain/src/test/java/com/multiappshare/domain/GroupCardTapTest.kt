package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class GroupCardTapTest {

    @Test
    fun shareModeStartsSharing() {
        assertEquals(GroupCardTap.Action.SHARE, GroupCardTap.action(inShareMode = true, hasSelectHandler = false))
        assertEquals(GroupCardTap.Action.SHARE, GroupCardTap.action(inShareMode = true, hasSelectHandler = true))
    }

    @Test
    fun twoPaneSelectsTheGroup() {
        assertEquals(
            GroupCardTap.Action.SELECT,
            GroupCardTap.action(inShareMode = false, hasSelectHandler = true),
        )
    }

    @Test
    fun phoneHomeTogglesExpand() {
        assertEquals(
            GroupCardTap.Action.TOGGLE_EXPAND,
            GroupCardTap.action(inShareMode = false, hasSelectHandler = false),
        )
    }
}
