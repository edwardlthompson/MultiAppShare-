package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ShareOverlayCopyTest {

    @Test
    fun sessionCopyDoesNotSendPeopleHome() {
        assertTrue(ShareOverlayCopy.isSessionScoped("Tap a group to start."))
        assertFalse(ShareOverlayCopy.isSessionScoped("Open ⋮ on the main screen."))
        assertFalse(ShareOverlayCopy.isSessionScoped("Change order from the main screen."))
    }
}
