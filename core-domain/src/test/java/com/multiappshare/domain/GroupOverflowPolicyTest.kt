package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GroupOverflowPolicyTest {

    @Test
    fun deleteIsItsOwnDangerSectionAfterEditAndOrganize() {
        val sections = GroupOverflowPolicy.sections()
        assertEquals(3, sections.size)
        assertEquals(GroupOverflowPolicy.edit, sections[0])
        assertEquals(GroupOverflowPolicy.organize, sections[1])
        assertEquals(listOf(GroupOverflowAction.DELETE), sections[2])
        assertTrue(GroupOverflowAction.DELETE !in GroupOverflowPolicy.edit)
        assertTrue(GroupOverflowAction.DELETE !in GroupOverflowPolicy.organize)
    }
}
