package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PassphraseFormTest {

    @Test
    fun mismatchHiddenUntilConfirmStarted() {
        assertFalse(PassphraseForm.mismatchVisible("secret-pass", ""))
        assertFalse(PassphraseForm.mismatchVisible("secret-pass", "secret-pass"))
        assertTrue(PassphraseForm.mismatchVisible("secret-pass", "other-pass"))
    }

    @Test
    fun exportRequiresMinLengthAndMatch() {
        assertFalse(PassphraseForm.exportEnabled("short", "short"))
        assertFalse(PassphraseForm.exportEnabled("long-enough", "mismatch!!"))
        assertTrue(PassphraseForm.exportEnabled("long-enough", "long-enough"))
    }

    @Test
    fun importRequiresMinLength() {
        assertFalse(PassphraseForm.importEnabled("short"))
        assertTrue(PassphraseForm.importEnabled("long-enough"))
    }

    @Test
    fun progressMatchesStrengthTiers() {
        assertEquals(0f, PassphraseForm.progress(PassphraseStrengthLevel.EMPTY), 0.001f)
        assertEquals(0.33f, PassphraseForm.progress(PassphraseStrengthLevel.WEAK), 0.001f)
        assertEquals(1f, PassphraseForm.progress(PassphraseStrengthLevel.STRONG), 0.001f)
    }
}
