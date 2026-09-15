package com.multiappshare

import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * TU-015–TU-021: Golden Path slices are adopted in product packages, not examples/android.
 */
class TemplateGoldenPathParityTest {

    @Test
    fun androidGoldenPathSurfacesExistOutsideExamplesStub() {
        val checks = listOf(
            "src/main/java/com/multiappshare/ui/about" to "about",
            "src/main/java/com/multiappshare/ui/settings" to "settings",
            "src/main/java/com/multiappshare/ui/feedback" to "feedback",
            "src/main/java/com/multiappshare/crashcapture" to "crash-capture",
            "src/main/java/com/multiappshare/display" to "display-refresh",
        )
        checks.forEach { (rel, id) ->
            assertTrue(id, File(rel).exists())
        }
        assertTrue(
            "privacy-report",
            File("../core-domain/src/main/java/com/multiappshare/privacyreport").isDirectory,
        )
        assertTrue("github-feedback", File("src/main/java/com/multiappshare/ui/feedback").isDirectory)
        assertTrue("no examples overwrite", !File("examples/android").exists())
    }
}
