package com.multiappshare.domain

object PassphraseForm {
    const val MIN_LENGTH = 10

    fun mismatchVisible(passphrase: String, confirm: String): Boolean =
        confirm.isNotEmpty() && passphrase != confirm

    fun exportEnabled(passphrase: String, confirm: String): Boolean =
        passphrase.length >= MIN_LENGTH && passphrase == confirm && passphrase.isNotEmpty()

    fun importEnabled(passphrase: String): Boolean = passphrase.length >= MIN_LENGTH

    fun progress(level: PassphraseStrengthLevel): Float = when (level) {
        PassphraseStrengthLevel.EMPTY -> 0f
        PassphraseStrengthLevel.WEAK -> 0.33f
        PassphraseStrengthLevel.FAIR -> 0.66f
        PassphraseStrengthLevel.STRONG -> 1f
    }
}
