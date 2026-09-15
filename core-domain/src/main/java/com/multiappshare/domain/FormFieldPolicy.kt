package com.multiappshare.domain

object FormFieldPolicy {
    const val MIN_ROW_TOUCH_DP = 48

    fun newPasswordAutofill(creatingPassphrase: Boolean): Boolean = creatingPassphrase
}
