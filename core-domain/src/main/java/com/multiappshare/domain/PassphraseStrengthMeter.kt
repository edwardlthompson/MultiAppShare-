package com.multiappshare.domain

enum class PassphraseStrengthLevel {
    EMPTY,
    WEAK,
    FAIR,
    STRONG,
}

object PassphraseStrengthMeter {
    fun evaluate(passphrase: CharArray?): PassphraseStrengthLevel {
        if (passphrase == null || passphrase.isEmpty()) return PassphraseStrengthLevel.EMPTY
        val variety = countVariety(passphrase)
        val length = passphrase.size
        return when {
            length >= 12 && variety >= 3 -> PassphraseStrengthLevel.STRONG
            length >= 8 && variety >= 2 -> PassphraseStrengthLevel.FAIR
            else -> PassphraseStrengthLevel.WEAK
        }
    }

    private fun countVariety(passphrase: CharArray): Int {
        var digit = 0
        var upper = 0
        var lower = 0
        var special = 0
        for (c in passphrase) {
            when {
                c.isDigit() -> digit = 1
                c.isUpperCase() -> upper = 1
                c.isLowerCase() -> lower = 1
                else -> special = 1
            }
        }
        return digit + upper + lower + special
    }
}
