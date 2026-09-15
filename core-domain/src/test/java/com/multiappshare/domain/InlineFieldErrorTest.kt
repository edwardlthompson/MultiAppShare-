package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class InlineFieldErrorTest {

    @Test
    fun of_returnsMessageOnlyWhenMutationFails() {
        assertNull(InlineFieldError.of(success = true, message = "taken"))
        assertEquals("taken", InlineFieldError.of(success = false, message = "taken"))
    }
}
