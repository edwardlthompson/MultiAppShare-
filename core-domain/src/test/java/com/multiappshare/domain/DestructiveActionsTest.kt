package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DestructiveActionsTest {

    @Test
    fun paneDeleteIsNotAPrimarySave() {
        assertTrue(DestructiveActions.PANE_DELETE_USES_ERROR_COLOR)
        assertFalse(
            DestructiveActions.looksLikePrimarySave(
                isDelete = true,
                usesErrorColor = DestructiveActions.PANE_DELETE_USES_ERROR_COLOR,
            ),
        )
        assertTrue(DestructiveActions.looksLikePrimarySave(isDelete = true, usesErrorColor = false))
    }
}
