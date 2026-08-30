package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import org.junit.Assert.assertEquals
import org.junit.Test

class DynamicShortcutBuilderTest {

    @Test
    fun buildsTopRankedShortcutsByUsage() {
        val groups = listOf(
            AppGroup(name = "Low", apps = emptyList(), usageCount = 1, id = "1"),
            AppGroup(name = "High", apps = emptyList(), usageCount = 100, id = "2"),
            AppGroup(name = "Medium", apps = emptyList(), usageCount = 50, id = "3"),
        )

        val shortcuts = DynamicShortcutBuilder.buildTopShortcuts(groups)
        assertEquals(3, shortcuts.size)
        assertEquals("High", shortcuts[0].shortLabel)
        assertEquals(0, shortcuts[0].rank)
        assertEquals("Medium", shortcuts[1].shortLabel)
        assertEquals(1, shortcuts[1].rank)
        assertEquals("Low", shortcuts[2].shortLabel)
        assertEquals(2, shortcuts[2].rank)
    }

    @Test
    fun limitsShortcutsToMaxAllowed() {
        val groups = (1..10).map {
            AppGroup(name = "Group$it", apps = emptyList(), usageCount = it, id = "$it")
        }
        val shortcuts = DynamicShortcutBuilder.buildTopShortcuts(groups)
        assertEquals(DynamicShortcutBuilder.MAX_SHORTCUTS, shortcuts.size)
    }
}
