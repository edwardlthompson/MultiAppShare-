package com.multiappshare.domain

import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class GroupRepairTest {

    private val appA = AppInfo("App A", "com.example.a")
    private val appB = AppInfo("App B", "com.example.b")
    private val appC = AppInfo("App C", "com.example.c")

    @Test
    fun prunesMissingPackages() {
        val group = AppGroup(name = "Test", apps = listOf(appA, appB, appC))
        val installed = setOf("com.example.a", "com.example.c")
        val repaired = GroupRepair.pruneUninstalled(listOf(group), installed)
        assertEquals(1, repaired.size)
        assertEquals(listOf(appA, appC), repaired[0].apps)
    }

    @Test
    fun countsMissingPackagesAcrossGroups() {
        val g1 = AppGroup(name = "G1", apps = listOf(appA, appB))
        val g2 = AppGroup(name = "G2", apps = listOf(appB, appC))
        val installed = setOf("com.example.a")
        val missing = GroupRepair.countMissingPackages(listOf(g1, g2), installed)
        assertEquals(3, missing)
    }
}
