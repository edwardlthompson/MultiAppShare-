package com.multiappshare

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.multiappshare.domain.ShareBackdropScrim
import com.multiappshare.ui.theme.Dimens
import com.multiappshare.ui.theme.PrimaryLight
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ThemeTokensTest {

    @Test
    fun primaryIsNotComposeTemplatePurple() {
        val templatePurple = Color(0xFF6650A4)
        assertNotEquals(templatePurple, PrimaryLight)
    }

    @Test
    fun touchTargetIsAtLeast48dp() {
        assertEquals(48.dp, Dimens.TouchTarget)
    }

    @Test
    fun shareScrimMatchesMaterialRange() {
        assertEquals(ShareBackdropScrim.DEFAULT, Dimens.SHARE_SCRIM)
        assertEquals(ShareBackdropScrim.LOW_RAM, Dimens.SHARE_SCRIM_LOW_RAM)
        assertTrue(Dimens.SHARE_SCRIM_LOW_RAM <= ShareBackdropScrim.MATERIAL_MAX)
    }
}
