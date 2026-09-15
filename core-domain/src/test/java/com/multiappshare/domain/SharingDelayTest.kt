package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class SharingDelayTest {

    @Test
    fun presetsUseCameraStyleCopyKeys() {
        assertEquals(SharingDelay.PresetCopy.NONE, SharingDelay.presetCopy(0))
        assertEquals(SharingDelay.PresetCopy.SHORT, SharingDelay.presetCopy(500))
        assertEquals(SharingDelay.PresetCopy.ONE, SharingDelay.presetCopy(1000))
        assertEquals(SharingDelay.PresetCopy.MANY, SharingDelay.presetCopy(2000))
    }

    @Test
    fun manyPresetFormatsWholeSecondsNotMilliseconds() {
        assertEquals(2, SharingDelay.wholeSeconds(2000))
    }
}
