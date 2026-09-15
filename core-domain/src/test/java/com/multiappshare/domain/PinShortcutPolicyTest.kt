package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PinShortcutPolicyTest {

    @Test
    fun explainsOnlyWhenLauncherCannotPin() {
        assertTrue(PinShortcutPolicy.shouldExplainUnsupported(pinSupported = false))
        assertFalse(PinShortcutPolicy.shouldExplainUnsupported(pinSupported = true))
    }
}
