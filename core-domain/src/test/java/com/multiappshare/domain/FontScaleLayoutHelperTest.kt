package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FontScaleLayoutHelperTest {

    @Test
    fun identifiesLargeAccessibilityFonts() {
        assertFalse(FontScaleLayoutHelper.isLargeFont(1.0f))
        assertFalse(FontScaleLayoutHelper.isLargeFont(1.3f))
        assertTrue(FontScaleLayoutHelper.isLargeFont(1.5f))
        assertTrue(FontScaleLayoutHelper.isLargeFont(2.0f))
    }

    @Test
    fun expandsDialogMaxHeightFractionForLargeFonts() {
        assertEquals(0.80f, FontScaleLayoutHelper.resolveDialogMaxHeightFraction(1.0f), 0.001f)
        assertEquals(0.90f, FontScaleLayoutHelper.resolveDialogMaxHeightFraction(2.0f), 0.001f)
    }
}
