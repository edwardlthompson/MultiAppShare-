package com.multiappshare.domain

import com.multiappshare.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class HiddenAppsFilterTest {

    private val signal = AppInfo(packageName = "org.signal", activityName = "Main", appName = "Signal")
    private val spamApp = AppInfo(packageName = "com.junk.app", activityName = "Main", appName = "Junk")

    @Test
    fun filtersOutHiddenPackages() {
        val apps = listOf(signal, spamApp)
        val visible = HiddenAppsFilter.filterVisibleApps(apps, setOf("com.junk.app"))
        assertEquals(1, visible.size)
        assertEquals("Signal", visible[0].appName)
    }

    @Test
    fun togglesPackageInHiddenSet() {
        val empty = emptySet<String>()
        val hidden = HiddenAppsFilter.toggleHidden(empty, "com.junk.app")
        assertEquals(setOf("com.junk.app"), hidden)

        val unhidden = HiddenAppsFilter.toggleHidden(hidden, "com.junk.app")
        assertEquals(emptySet<String>(), unhidden)
    }
}
