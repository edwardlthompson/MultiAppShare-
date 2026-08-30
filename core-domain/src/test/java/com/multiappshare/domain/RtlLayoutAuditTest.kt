package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RtlLayoutAuditTest {

    @Test
    fun identifiesRtlLanguageCodes() {
        assertTrue(RtlLayoutAudit.isRtlLocale("ar"))
        assertTrue(RtlLayoutAudit.isRtlLocale("ar-EG"))
        assertTrue(RtlLayoutAudit.isRtlLocale("he-IL"))
        assertTrue(RtlLayoutAudit.isRtlLocale("fa"))
        assertTrue(RtlLayoutAudit.isRtlLocale("ur"))
    }

    @Test
    fun identifiesLtrLanguageCodes() {
        assertFalse(RtlLayoutAudit.isRtlLocale("en"))
        assertFalse(RtlLayoutAudit.isRtlLocale("fr"))
        assertFalse(RtlLayoutAudit.isRtlLocale("es"))
        assertFalse(RtlLayoutAudit.isRtlLocale("de"))
        assertFalse(RtlLayoutAudit.isRtlLocale("it"))
        assertFalse(RtlLayoutAudit.isRtlLocale(null))
        assertFalse(RtlLayoutAudit.isRtlLocale(""))
    }
}
