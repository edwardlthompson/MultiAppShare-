package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GroupColorLabelTest {

    @Test
    fun formatsEmojiWithRawName() {
        assertEquals("💬 Chat", GroupColorLabel.formatWithEmoji("💬", "Chat"))
        assertEquals("💬 Chat", GroupColorLabel.formatWithEmoji("💬", "💬 Chat"))
        assertEquals("Chat", GroupColorLabel.formatWithEmoji(null, "Chat"))
        assertEquals("Chat", GroupColorLabel.formatWithEmoji("", "Chat"))
    }

    @Test
    fun extractsEmojiFromNamedGroup() {
        assertEquals("💬", GroupColorLabel.extractEmoji("💬 Chat"))
        assertEquals("⭐", GroupColorLabel.extractEmoji("⭐ Favorites"))
        assertNull(GroupColorLabel.extractEmoji("Plain Group"))
    }

    @Test
    fun stripsEmojiFromNamedGroup() {
        assertEquals("Chat", GroupColorLabel.stripEmoji("💬 Chat"))
        assertEquals("Plain", GroupColorLabel.stripEmoji("Plain"))
    }
}
