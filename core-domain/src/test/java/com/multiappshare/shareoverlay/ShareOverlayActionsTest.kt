package com.multiappshare.shareoverlay

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ShareOverlayActionsTest {

    @Test
    fun primaryIsFinishOnlyOnLastApp() {
        assertFalse(ShareOverlayActions.primaryIsFinish(0, 3))
        assertTrue(ShareOverlayActions.primaryIsFinish(2, 3))
    }

    @Test
    fun skipActionsHiddenOnLastApp() {
        assertTrue(ShareOverlayActions.showSkipActions(0, 3))
        assertFalse(ShareOverlayActions.showSkipActions(2, 3))
    }

    @Test
    fun tryLaterOnlyWhenFailedWithRemaining() {
        assertTrue(ShareOverlayActions.showTryLater(lastShareFailed = true, remainingAfterCurrent = 1))
        assertFalse(ShareOverlayActions.showTryLater(lastShareFailed = false, remainingAfterCurrent = 1))
        assertFalse(ShareOverlayActions.showTryLater(lastShareFailed = true, remainingAfterCurrent = 0))
    }

    @Test
    fun progressFractionMatchesStep() {
        assertEquals(0.5f, ShareOverlayActions.progressFraction(0, 2), 0.001f)
        assertEquals(1f, ShareOverlayActions.progressFraction(2, 3), 0.001f)
    }

    @Test
    fun primaryStaysPinnedOutsideScroll() {
        assertTrue(ShareOverlayActions.PRIMARY_STICKY)
    }
}
