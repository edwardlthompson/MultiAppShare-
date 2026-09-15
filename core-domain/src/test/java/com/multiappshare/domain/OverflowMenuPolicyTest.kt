package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OverflowMenuPolicyTest {

    @Test
    fun overflowStaysAtMostFivePeerActions() {
        assertTrue(OverflowMenuPolicy.withinLimit())
        assertEquals(OverflowMenuPolicy.MAX_ITEMS, OverflowMenuPolicy.items.size)
        assertEquals(
            listOf(
                OverflowDestination.SORT_GROUPS,
                OverflowDestination.AUTO_GROUP,
                OverflowDestination.SHARE_CLIPBOARD,
                OverflowDestination.HISTORY,
                OverflowDestination.SETTINGS,
            ),
            OverflowMenuPolicy.items.map { it.destination },
        )
    }

    @Test
    fun overflowAndHubIconsAreUnique() {
        assertTrue(OverflowMenuPolicy.iconKeysAreUnique())
        assertTrue(SettingsHubPolicy.iconKeysAreUnique())
        assertEquals(
            "content_paste",
            OverflowMenuPolicy.items.first { it.destination == OverflowDestination.SHARE_CLIPBOARD }.iconKey,
        )
        assertEquals(
            "history",
            OverflowMenuPolicy.items.first { it.destination == OverflowDestination.HISTORY }.iconKey,
        )
        assertEquals(6, SettingsHubPolicy.items.size)
        assertFalse(OverflowMenuPolicy.includesDonate())
        assertFalse(SettingsHubPolicy.includesDonate())
    }
}
