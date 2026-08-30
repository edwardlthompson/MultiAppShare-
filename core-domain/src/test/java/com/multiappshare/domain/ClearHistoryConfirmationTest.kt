package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ClearHistoryConfirmationTest {

    @Test
    fun determinesDialogVisibilityBasedOnCount() {
        assertFalse(ClearHistoryConfirmation.shouldShowDialog(0))
        assertTrue(ClearHistoryConfirmation.shouldShowDialog(1))
        assertTrue(ClearHistoryConfirmation.shouldShowDialog(50))
    }

    @Test
    fun formatsConfirmationPromptCorrectly() {
        assertEquals("Are you sure you want to delete 1 history record?", ClearHistoryConfirmation.formatConfirmationPrompt(1))
        assertEquals("Are you sure you want to delete all 5 history records?", ClearHistoryConfirmation.formatConfirmationPrompt(5))
    }
}
