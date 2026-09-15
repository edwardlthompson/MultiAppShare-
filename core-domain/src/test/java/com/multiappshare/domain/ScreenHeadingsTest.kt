package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ScreenHeadingsTest {

    @Test
    fun oneHeadingRolePerMode() {
        assertEquals(ScreenHeadings.Role.GROUPS, ScreenHeadings.documentHeading(false, false))
        assertEquals(ScreenHeadings.Role.CHOOSE_GROUP, ScreenHeadings.documentHeading(true, false))
        assertEquals(ScreenHeadings.Role.SHARE_SESSION, ScreenHeadings.documentHeading(true, true))
        assertFalse(ScreenHeadings.OVERLAY_COPY_IS_DOCUMENT_HEADING)
    }
}
