package com.multiappshare.data.local

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class ConvertersTest {

    private val converters = Converters()

    @Test
    fun toAppInfoList_invalidJson_returnsEmptyList() {
        val result = converters.toAppInfoList("{not valid json")
        assertTrue(result.isEmpty())
    }

    @Test
    fun toAppInfoList_validJson_roundTrips() {
        val json = converters.fromAppInfoList(emptyList())
        assertEquals("[]", json)
        assertTrue(converters.toAppInfoList(json).isEmpty())
    }
}
