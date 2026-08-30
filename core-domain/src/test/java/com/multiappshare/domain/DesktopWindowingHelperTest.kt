package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class DesktopWindowingHelperTest {

    @Test
    fun classifiesWindowSizesCorrectly() {
        assertEquals(WindowSizeClass.COMPACT, DesktopWindowingHelper.classifyWindowSize(360))
        assertEquals(WindowSizeClass.MEDIUM, DesktopWindowingHelper.classifyWindowSize(700))
        assertEquals(WindowSizeClass.EXPANDED, DesktopWindowingHelper.classifyWindowSize(1200))
    }

    @Test
    fun determinesOverlayMaxContentWidth() {
        assertEquals(400, DesktopWindowingHelper.overlayMaxContentWidthDp(WindowSizeClass.COMPACT))
        assertEquals(560, DesktopWindowingHelper.overlayMaxContentWidthDp(WindowSizeClass.MEDIUM))
        assertEquals(720, DesktopWindowingHelper.overlayMaxContentWidthDp(WindowSizeClass.EXPANDED))
    }
}
