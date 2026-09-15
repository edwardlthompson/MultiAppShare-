package com.multiappshare.domain

import com.multiappshare.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class MergePreviewTest {

    @Test
    fun combinedCountUnionsDistinctActivities() {
        val signal = AppInfo("Signal", "org.signal", "Main")
        val dupe = AppInfo("Signal 2", "org.signal", "Main")
        val wa = AppInfo("WhatsApp", "com.whatsapp", "Chat")
        assertEquals(2, MergePreview.combinedAppCount(listOf(signal), listOf(dupe, wa)))
    }
}
