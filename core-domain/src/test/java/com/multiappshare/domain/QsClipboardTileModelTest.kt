package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QsClipboardTileModelTest {

    @Test
    fun activeOnlyWhenClipboardHasContent() {
        assertTrue(QsClipboardTileModel.isActive(true))
        assertFalse(QsClipboardTileModel.isActive(false))
    }
}
