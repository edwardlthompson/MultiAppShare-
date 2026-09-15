package com.multiappshare.shareoverlay

import org.junit.Assert.assertTrue
import org.junit.Test

class OverlayStepCardTest {

    @Test
    fun currentStepIsACard() {
        assertTrue(OverlayStepCard.USES_ELEVATED_CARD)
        assertTrue(ShareOverlayActions.PRIMARY_STICKY)
    }
}
