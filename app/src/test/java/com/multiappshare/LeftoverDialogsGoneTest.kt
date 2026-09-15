package com.multiappshare

import org.junit.Assert.assertFalse
import org.junit.Test
import java.io.File

class LeftoverDialogsGoneTest {

    @Test
    fun glassySurfaceAndLegacyAboutAreGone() {
        val root = File("src/main/java")
        val glassy = root.walkTopDown().any { it.name == "GlassySurface.kt" }
        val dialogs = File("src/main/java/com/multiappshare/MainActivityDialogs.kt").readText()
        assertFalse(glassy)
        assertFalse(dialogs.contains("fun AboutDialog"))
        assertFalse(dialogs.contains("fun HistoryDialog"))
    }
}
