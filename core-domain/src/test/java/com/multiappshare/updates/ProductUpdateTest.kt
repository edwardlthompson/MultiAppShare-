package com.multiappshare.updates

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductUpdateTest {

    @Test
    fun dailyCheckWaitsAFullDay() {
        assertTrue(ProductUpdate.shouldCheckDaily(null, 0L))
        assertFalse(ProductUpdate.shouldCheckDaily(0L, ProductUpdate.MS_DAY - 1))
        assertTrue(ProductUpdate.shouldCheckDaily(0L, ProductUpdate.MS_DAY))
    }

    @Test
    fun apkVersionIgnoresTemplateTags() {
        assertEquals(
            "1.9.4",
            ProductUpdate.parseApkVersion("MultiAppShare-v1.9.4-release.apk"),
        )
        assertEquals(
            "1.9.5",
            ProductUpdate.parseApkVersion("multiappshare-1.9.5-foss.apk"),
        )
        assertNull(ProductUpdate.parseApkVersion("v0.21.0"))
        assertNull(ProductUpdate.parseApkVersion("MultiAppShare-v1.9.4-debug.apk"))
    }

    @Test
    fun newerThanCurrentComparesInstallerVersions() {
        assertTrue(ProductUpdate.isNewerVersion("1.9.4", "1.9.5"))
        assertFalse(ProductUpdate.isNewerVersion("1.9.5", "1.9.5"))
        assertFalse(ProductUpdate.isNewerVersion("1.10.0", "1.9.9"))
    }

    @Test
    fun donateNudgeOnlyAfterVersionChange() {
        assertFalse(ProductUpdate.shouldNudgeDonate(null, "1.9.4"))
        assertFalse(ProductUpdate.shouldNudgeDonate("1.9.4", "1.9.4"))
        assertTrue(ProductUpdate.shouldNudgeDonate("1.9.4", "1.9.5"))
    }

    @Test
    fun selectApkAssetReadsProductFilename() {
        val picked = ProductUpdate.selectApkAsset(
            listOf(
                ProductUpdate.NamedAsset("sbom.cyclonedx.json", "https://example.com/sbom"),
                ProductUpdate.NamedAsset(
                    "MultiAppShare-v1.9.5-release.apk",
                    "https://example.com/a.apk",
                ),
            ),
        )
        assertEquals("1.9.5", picked?.version)
        assertEquals("https://example.com/a.apk", picked?.url)
    }

    @Test
    fun updatePromptSkipsDismissedVersion() {
        assertTrue(ProductUpdate.shouldPromptUpdate("1.9.4", "1.9.5", null))
        assertFalse(ProductUpdate.shouldPromptUpdate("1.9.4", "1.9.5", "1.9.5"))
        assertFalse(ProductUpdate.shouldPromptUpdate("1.9.5", "1.9.5", null))
        assertFalse(ProductUpdate.shouldPromptUpdate("1.9.4", null, null))
    }
}
