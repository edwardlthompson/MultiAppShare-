package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FormFieldPolicyTest {

    @Test
    fun rowTouchTargetMeetsAccessibilityMinimum() {
        assertEquals(48, FormFieldPolicy.MIN_ROW_TOUCH_DP)
    }

    @Test
    fun exportAndConfirmUseNewPasswordAutofill() {
        assertTrue(FormFieldPolicy.newPasswordAutofill(creatingPassphrase = true))
        assertFalse(FormFieldPolicy.newPasswordAutofill(creatingPassphrase = false))
    }
}
