package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GroupNotesHelperTest {

    @Test
    fun sanitizesBlankAndNullNotes() {
        assertEquals("", GroupNotesHelper.sanitize(null))
        assertEquals("", GroupNotesHelper.sanitize(""))
        assertEquals("", GroupNotesHelper.sanitize("   "))
        assertFalse(GroupNotesHelper.hasNotes(null))
        assertFalse(GroupNotesHelper.hasNotes("  "))
    }

    @Test
    fun trimsAndPreservesValidNotes() {
        val note = "Share photos and links with family"
        assertEquals(note, GroupNotesHelper.sanitize("  $note  "))
        assertTrue(GroupNotesHelper.hasNotes(note))
    }

    @Test
    fun clampsExcessivelyLongNotes() {
        val longNote = "a".repeat(200)
        val sanitized = GroupNotesHelper.sanitize(longNote)
        assertEquals(GroupNotesHelper.MAX_NOTE_LENGTH, sanitized.length)
    }
}
