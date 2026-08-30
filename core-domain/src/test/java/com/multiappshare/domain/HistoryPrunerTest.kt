package com.multiappshare.domain

import com.multiappshare.model.HistoryItem
import org.junit.Assert.assertEquals
import org.junit.Test

class HistoryPrunerTest {

    private val now = 100_000_000_000L
    private val msPerDay = 24 * 60 * 60 * 1000L

    private val item10DaysAgo = HistoryItem(
        id = 1,
        timestamp = now - (10 * msPerDay),
        groupName = "Group1",
        contentDescription = "Test 1",
        status = "Done",
    )
    private val item40DaysAgo = HistoryItem(
        id = 2,
        timestamp = now - (40 * msPerDay),
        groupName = "Group2",
        contentDescription = "Test 2",
        status = "Done",
    )
    private val item100DaysAgo = HistoryItem(
        id = 3,
        timestamp = now - (100 * msPerDay),
        groupName = "Group3",
        contentDescription = "Test 3",
        status = "Done",
    )

    private val allItems = listOf(item10DaysAgo, item40DaysAgo, item100DaysAgo)

    @Test
    fun prunesTo30Days() {
        val result = HistoryPruner.prune(allItems, HistoryRetentionPeriod.DAYS_30, now)
        assertEquals(listOf(item10DaysAgo), result)
    }

    @Test
    fun prunesTo90Days() {
        val result = HistoryPruner.prune(allItems, HistoryRetentionPeriod.DAYS_90, now)
        assertEquals(listOf(item10DaysAgo, item40DaysAgo), result)
    }

    @Test
    fun retainsAllWhenForever() {
        val result = HistoryPruner.prune(allItems, HistoryRetentionPeriod.FOREVER, now)
        assertEquals(allItems, result)
    }
}
