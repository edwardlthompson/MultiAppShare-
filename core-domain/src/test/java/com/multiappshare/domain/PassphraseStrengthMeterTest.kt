package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class PassphraseStrengthMeterTest {

    @Test
    fun evaluatesEmptyAndNull() {
        assertEquals(PassphraseStrengthLevel.EMPTY, PassphraseStrengthMeter.evaluate(null))
        assertEquals(PassphraseStrengthLevel.EMPTY, PassphraseStrengthMeter.evaluate(charArrayOf()))
    }

    @Test
    fun evaluatesWeakPassphrases() {
        assertEquals(PassphraseStrengthLevel.WEAK, PassphraseStrengthMeter.evaluate("short".toCharArray()))
        assertEquals(PassphraseStrengthLevel.WEAK, PassphraseStrengthMeter.evaluate("12345".toCharArray()))
    }

    @Test
    fun evaluatesFairPassphrases() {
        assertEquals(PassphraseStrengthLevel.FAIR, PassphraseStrengthMeter.evaluate("Password123".toCharArray()))
    }

    @Test
    fun evaluatesStrongPassphrases() {
        assertEquals(
            PassphraseStrengthLevel.STRONG,
            PassphraseStrengthMeter.evaluate("Super-Secure-Passphrase!2026".toCharArray()),
        )
    }
}
