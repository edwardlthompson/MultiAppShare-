package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import org.junit.Assert.assertEquals
import org.junit.Test

class GroupPinOrderTest {

    private val groupA = AppGroup(name = "Alpha", apps = emptyList(), usageCount = 5)
    private val groupB = AppGroup(name = "Beta", apps = emptyList(), usageCount = 10)
    private val groupC = AppGroup(name = "Gamma", apps = emptyList(), usageCount = 20)

    @Test
    fun placesPinnedGroupsFirstSortedByUsage() {
        val groups = listOf(groupA, groupB, groupC)
        val pinned = setOf("Alpha")
        val sorted = GroupPinOrder.sortGroupsWithPinned(groups, pinned)

        assertEquals(listOf("Alpha", "Gamma", "Beta"), sorted.map { it.name })
    }

    @Test
    fun togglesPinMembership() {
        val initial = setOf("Alpha")
        val toggledOn = GroupPinOrder.togglePin(initial, "Beta")
        assertEquals(setOf("Alpha", "Beta"), toggledOn)

        val toggledOff = GroupPinOrder.togglePin(toggledOn, "Alpha")
        assertEquals(setOf("Beta"), toggledOff)
    }
}
