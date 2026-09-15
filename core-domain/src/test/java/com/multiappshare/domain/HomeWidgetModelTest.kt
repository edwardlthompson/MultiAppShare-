package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeWidgetModelTest {

    @Test
    fun createsStateWithValidGroupName() {
        val state = HomeWidgetModel.createWidgetState("Family Chat", true, "Recent Group")
        assertEquals("Family Chat", state.primaryGroupLabel)
        assertTrue(state.hasClipboardTarget)
    }

    @Test
    fun fallsBackToDefaultLabelWhenGroupNameIsBlankOrNull() {
        val state1 = HomeWidgetModel.createWidgetState(null, true, "Recent Group")
        assertEquals("Recent Group", state1.primaryGroupLabel)

        val state2 = HomeWidgetModel.createWidgetState("   ", false, "Recent Group")
        assertEquals("Recent Group", state2.primaryGroupLabel)
    }
}
