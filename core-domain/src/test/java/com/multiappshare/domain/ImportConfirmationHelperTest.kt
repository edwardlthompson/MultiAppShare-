package com.multiappshare.domain

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
}
