package com.multiappshare

import com.multiappshare.domain.FeatureSpecHonesty
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class FeatureSpecHonestyFilesTest {

    @Test
    fun reviewUiSpecsPointAtRealComposables() {
        val src = File("src/main/java")
        val docs = File("../docs/features")
        FeatureSpecHonesty.REVIEW_LOCKS.forEach { lock ->
            val spec = File(docs, "${lock.slug}.md")
            assertTrue(lock.slug, spec.isFile)
            val text = spec.readText()
            assertTrue("${lock.slug} layer", text.contains("Layer: user-visible"))
            val fileName = lock.composableFile
            assertTrue("${lock.slug} composable", !fileName.isNullOrBlank())
            assertTrue(fileName, src.walkTopDown().any { it.name == fileName })
        }
    }
}
