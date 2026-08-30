package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ImportConfirmationHelperTest {

    @Test
    fun shouldPromptConfirmation_returnsTrue_onlyWhenReplacingWithExistingGroups() {
        assertTrue(ImportConfirmationHelper.shouldPromptConfirmation(1, isReplaceStrategy = true))
        assertTrue(ImportConfirmationHelper.shouldPromptConfirmation(5, isReplaceStrategy = true))
        assertFalse(ImportConfirmationHelper.shouldPromptConfirmation(0, isReplaceStrategy = true))
        assertFalse(ImportConfirmationHelper.shouldPromptConfirmation(5, isReplaceStrategy = false))
    }

    @Test
    fun formatWarningMessage_handlesSingularAndPlural() {
        val singleMsg = "Importing in Replace mode will overwrite 1 existing group."
        val pluralMsg = "Importing in Replace mode will overwrite 3 existing groups."
        assertEquals(singleMsg, ImportConfirmationHelper.formatWarningMessage(1))
        assertEquals(pluralMsg, ImportConfirmationHelper.formatWarningMessage(3))
    }
}
