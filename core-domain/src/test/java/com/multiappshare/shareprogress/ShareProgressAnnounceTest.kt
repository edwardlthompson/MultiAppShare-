package com.multiappshare.shareprogress

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ShareProgressAnnounceTest {

    @Test
    fun oneBasedStepWhenInRange() {
        val snap = ShareProgressAnnounce.snapshot(0, 3, "Signal")
        assertNotNull(snap)
        assertEquals(1, snap!!.step)
        assertEquals(3, snap.total)
        assertEquals("Signal", snap.target)
    }

    @Test
    fun rejectsEmptyOrOutOfRange() {
        assertNull(ShareProgressAnnounce.snapshot(0, 0, "X"))
        assertNull(ShareProgressAnnounce.snapshot(-1, 2, "X"))
        assertNull(ShareProgressAnnounce.snapshot(2, 2, "X"))
    }
}
