package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DeepLinkGroupLookupTest {

    private val social = AppGroup(name = "Social", apps = emptyList(), id = "id-social")

    @Test
    fun findsById() {
        assertEquals("Social", DeepLinkGroupLookup.existingName(listOf(social), "id-social", null))
    }

    @Test
    fun findsByNameIgnoreCase() {
        assertEquals("Social", DeepLinkGroupLookup.existingName(listOf(social), null, " social "))
    }

    @Test
    fun missingReturnsNull() {
        assertNull(DeepLinkGroupLookup.existingName(listOf(social), null, "__none__"))
        assertNull(DeepLinkGroupLookup.existingName(listOf(social), "missing-id", null))
    }
}
