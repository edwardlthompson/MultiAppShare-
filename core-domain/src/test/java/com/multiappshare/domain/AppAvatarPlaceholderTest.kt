package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class AppAvatarPlaceholderTest {

    @Test
    fun initialUsesFirstLetter() {
        assertEquals("S", AppAvatarPlaceholder.initial("social"))
        assertEquals("W", AppAvatarPlaceholder.initial("  Work Chat"))
        assertEquals("?", AppAvatarPlaceholder.initial("   "))
    }
}
