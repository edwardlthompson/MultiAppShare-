package com.multiappshare.domain

import com.multiappshare.model.HistoryItem
import org.junit.Assert.assertEquals
import org.junit.Test

class HistoryGroupFilterTest {

    private val item1 = HistoryItem(
        id = 1,
        timestamp = 1000L,
        groupName = "Friends",
        contentDescription = "Photo",
        status = "Done",
    )
    private val item2 = HistoryItem(
        id = 2,
        timestamp = 2000L,
        groupName = "Work",
        contentDescription = "Document",
        status = "Done",
    )
    private val item3 = HistoryItem(
        id = 3,
        timestamp = 3000L,
        groupName = "friends",
        contentDescription = "Link",
        status = "Done",
    )

    private val allItems = listOf(item1, item2, item3)

    @Test
    fun filtersHistoryByGroupNameCaseInsensitive() {
        val result = HistoryGroupFilter.filter(allItems, "Friends")
        assertEquals(2, result.size)
        assertEquals(listOf(1L, 3L), result.map { it.id })
    }

    @Test
    fun returnsAllWhenFilterIsNullOrEqualToEmpty() {
        assertEquals(allItems, HistoryGroupFilter.filter(allItems, null))
        assertEquals(allItems, HistoryGroupFilter.filter(allItems, ""))
        assertEquals(allItems, HistoryGroupFilter.filter(allItems, "   "))
    }

    @Test
    fun extractsDistinctGroupsSorted() {
        val distinct = HistoryGroupFilter.extractDistinctGroups(listOf(item1, item2))
        assertEquals(listOf("Friends", "Work"), distinct)
    }
}
