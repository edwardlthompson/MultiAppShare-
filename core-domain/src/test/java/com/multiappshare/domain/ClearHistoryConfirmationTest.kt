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
        val singleExpected = "Are you sure you want to delete 1 history record?"
        val pluralExpected = "Are you sure you want to delete all 5 history records?"
        assertEquals(singleExpected, ClearHistoryConfirmation.formatConfirmationPrompt(1))
        assertEquals(pluralExpected, ClearHistoryConfirmation.formatConfirmationPrompt(5))
    }
}
