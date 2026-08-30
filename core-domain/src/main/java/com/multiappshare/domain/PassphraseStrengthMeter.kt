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
        val length = passphrase.size
        var hasDigit = false
        var hasUpper = false
        var hasLower = false
        var hasSpecial = false

        for (c in passphrase) {
            when {
                c.isDigit() -> hasDigit = true
                c.isUpperCase() -> hasUpper = true
                c.isLowerCase() -> hasLower = true
                else -> hasSpecial = true
            }
        }

        var varietyCount = 0
        if (hasDigit) varietyCount++
        if (hasUpper) varietyCount++
        if (hasLower) varietyCount++
        if (hasSpecial) varietyCount++

        return when {
            length >= 12 && varietyCount >= 3 -> PassphraseStrengthLevel.STRONG
            length >= 8 && varietyCount >= 2 -> PassphraseStrengthLevel.FAIR
            else -> PassphraseStrengthLevel.WEAK
        }
    }
}
