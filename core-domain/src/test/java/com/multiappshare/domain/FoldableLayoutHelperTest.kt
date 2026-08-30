package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FoldableLayoutHelperTest {

    @Test
    fun splitsTwoPaneOnWideScreens() {
        assertTrue(FoldableLayoutHelper.shouldSplitTwoPane(600, FoldPosture.FLAT, false))
        assertTrue(FoldableLayoutHelper.shouldSplitTwoPane(840, FoldPosture.FLAT, false))
    }

    @Test
    fun splitsTwoPaneOnSeparatingHingeHalfOpened() {
        assertTrue(FoldableLayoutHelper.shouldSplitTwoPane(400, FoldPosture.HALF_OPENED, true))
    }

    @Test
    fun keepsSinglePaneOnNarrowFlatScreens() {
        assertFalse(FoldableLayoutHelper.shouldSplitTwoPane(360, FoldPosture.FLAT, false))
        assertFalse(FoldableLayoutHelper.shouldSplitTwoPane(400, FoldPosture.HALF_OPENED, false))
    }
}
