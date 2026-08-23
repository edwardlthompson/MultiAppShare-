package com.multiappshare.updates

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LaunchPromptTest {

    @Test
    fun firstRunRecordsVersionWithoutDonate() = runTest {
        val seen = mutableListOf<String>()
        var fetches = 0
        val prompt = LaunchPromptDecider.decide(
            currentVersion = "1.9.4",
            lastSeenVersion = null,
            lastCheckAt = null,
            dismissedVersion = null,
            now = 0L,
            fetchLatest = {
                fetches += 1
                null
            },
            markSeen = { seen += it },
            markChecked = {},
        )
        assertNull(prompt)
        assertEquals(listOf("1.9.4"), seen)
        assertEquals(1, fetches)
    }

    @Test
    fun donateNudgeOnlyOnVersionChangeAndSkipsFetch() = runTest {
        var fetches = 0
        val prompt = LaunchPromptDecider.decide(
            currentVersion = "1.9.5",
            lastSeenVersion = "1.9.4",
            lastCheckAt = 0L,
            dismissedVersion = null,
            now = ProductUpdate.MS_DAY,
            fetchLatest = {
                fetches += 1
                release("1.9.6")
            },
            markSeen = {},
            markChecked = {},
        )
        assertEquals(LaunchPrompt.Donate, prompt)
        assertEquals(0, fetches)
    }

    @Test
    fun dailyIntervalSkipsFetchWhenDayHasNotElapsed() = runTest {
        var fetches = 0
        val prompt = LaunchPromptDecider.decide(
            currentVersion = "1.9.4",
            lastSeenVersion = "1.9.4",
            lastCheckAt = 0L,
            dismissedVersion = null,
            now = ProductUpdate.MS_DAY - 1,
            fetchLatest = {
                fetches += 1
                release("1.9.5")
            },
            markSeen = {},
            markChecked = {},
        )
        assertNull(prompt)
        assertEquals(0, fetches)
    }

    @Test
    fun newerMatchingAssetPromptsInstall() = runTest {
        val checked = mutableListOf<Long>()
        val prompt = LaunchPromptDecider.decide(
            currentVersion = "1.9.4",
            lastSeenVersion = "1.9.4",
            lastCheckAt = 0L,
            dismissedVersion = null,
            now = ProductUpdate.MS_DAY,
            fetchLatest = { release("1.9.5") },
            markSeen = {},
            markChecked = { checked += it },
        )
        assertEquals(LaunchPrompt.Update("1.9.5", "https://example.com/a.apk"), prompt)
        assertEquals(emptyList<Long>(), checked)
    }

    @Test
    fun dismissedVersionStaysSilent() = runTest {
        val prompt = LaunchPromptDecider.decide(
            currentVersion = "1.9.4",
            lastSeenVersion = "1.9.4",
            lastCheckAt = 0L,
            dismissedVersion = "1.9.5",
            now = ProductUpdate.MS_DAY,
            fetchLatest = { release("1.9.5") },
            markSeen = {},
            markChecked = {},
        )
        assertNull(prompt)
    }

    @Test
    fun failedFetchOrEmptyAssetsStaySilent() = runTest {
        val failed = LaunchPromptDecider.decide(
            currentVersion = "1.9.4",
            lastSeenVersion = "1.9.4",
            lastCheckAt = 0L,
            dismissedVersion = null,
            now = ProductUpdate.MS_DAY,
            fetchLatest = { error("timeout") },
            markSeen = {},
            markChecked = {},
        )
        val empty = LaunchPromptDecider.decide(
            currentVersion = "1.9.4",
            lastSeenVersion = "1.9.4",
            lastCheckAt = 0L,
            dismissedVersion = null,
            now = ProductUpdate.MS_DAY,
            fetchLatest = { GithubRelease(ProductUpdate.RELEASES_PAGE, emptyList()) },
            markSeen = {},
            markChecked = {},
        )
        assertNull(failed)
        assertNull(empty)
        assertTrue(GithubReleaseJson.parse("{") == null)
        val json = """
            {"html_url":"https://example.com/r","assets":[
              {"name":"MultiAppShare-v1.9.5-release.apk","browser_download_url":"https://example.com/a.apk"}
            ]}
        """.trimIndent()
        assertEquals(
            "1.9.5",
            GithubReleaseJson.parse(json)?.let { ProductUpdate.selectApkAsset(it.assets)?.version },
        )
    }

    private fun release(version: String) = GithubRelease(
        htmlUrl = ProductUpdate.RELEASES_PAGE,
        assets = listOf(
            ProductUpdate.NamedAsset(
                "MultiAppShare-v$version-release.apk",
                "https://example.com/a.apk",
            ),
        ),
    )
}
