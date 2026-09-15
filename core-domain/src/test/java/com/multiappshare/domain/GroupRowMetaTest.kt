package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GroupRowMetaTest {

    @Test
    fun latestTimestampByName_keepsNewestPerGroup() {
        val latest = GroupRowMeta.latestTimestampByName(
            listOf(
                "Social" to 10L,
                "Work" to 5L,
                "Social" to 40L,
                "Social" to 20L,
            ),
        )
        assertEquals(40L, latest["Social"])
        assertEquals(5L, latest["Work"])
    }

    @Test
    fun daysSince_nullWhenNeverShared() {
        assertNull(GroupRowMeta.daysSince(null, nowMillis = 1000L))
    }

    @Test
    fun daysSince_countsWholeDays() {
        val now = 10 * GroupRowMeta.MS_PER_DAY
        assertEquals(0, GroupRowMeta.daysSince(now, now))
        assertEquals(1, GroupRowMeta.daysSince(now - GroupRowMeta.MS_PER_DAY, now))
        assertEquals(3, GroupRowMeta.daysSince(now - 3 * GroupRowMeta.MS_PER_DAY, now))
    }
}
