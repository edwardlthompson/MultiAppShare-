package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShareBackdropScrimTest {

    @Test
    fun alphasStayInMaterialScrimRange() {
        val normal = ShareBackdropScrim.alpha(false)
        val lowRam = ShareBackdropScrim.alpha(true)
        assertEquals(ShareBackdropScrim.DEFAULT, normal)
        assertEquals(ShareBackdropScrim.LOW_RAM, lowRam)
        assertTrue(normal >= ShareBackdropScrim.MATERIAL_MIN)
        assertTrue(lowRam <= ShareBackdropScrim.MATERIAL_MAX)
        assertTrue(lowRam >= normal)
    }
}
