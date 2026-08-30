package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OverlayGroupCollapserTest {

    private val group1 = AppGroup(name = "Images", apps = emptyList(), isExpanded = true, id = "id-img")
    private val group2 = AppGroup(name = "Audio", apps = emptyList(), isExpanded = true, id = "id-audio")

    @Test
    fun collapsesIncompatibleGroups() {
        val groups = listOf(group1, group2)
        val compatible = setOf("id-img")

        val result = OverlayGroupCollapser.resolveExpansionStates(groups, compatible)
        assertTrue(result[0].isExpanded)
        assertFalse(result[1].isExpanded)
    }

    @Test
    fun preservesOriginalStatesWhenCollapseIsDisabled() {
        val groups = listOf(group1, group2)
        val compatible = setOf("id-img")

        val result = OverlayGroupCollapser.resolveExpansionStates(
            groups = groups,
            compatibleGroupIds = compatible,
            defaultCollapseIncompatible = false,
        )
        assertTrue(result[0].isExpanded)
        assertTrue(result[1].isExpanded)
    }
}
