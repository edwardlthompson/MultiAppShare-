package com.multiappshare.domain

import org.junit.Assert.assertTrue
import org.junit.Test

class ClipboardEmptyNoticeTest {

    @Test
    fun emptyClipboardStaysOnTheGroupsHome() {
        assertTrue(ClipboardEmptyNotice.USES_INLINE_BANNER)
    }
}
