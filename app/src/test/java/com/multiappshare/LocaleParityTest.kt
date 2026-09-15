package com.multiappshare

import com.multiappshare.domain.AppLanguageTags
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class LocaleParityTest {

    @Test
    fun pickerMatchesLocaleConfigAndResourceFolders() {
        val tags = AppLanguageTags.pickerOrder
        assertEquals(AppLanguageTags.supported, tags.toSet())
        val config = File("src/main/res/xml/locales_config.xml").readText()
        tags.forEach { tag ->
            assertTrue(tag, config.contains("android:name=\"$tag\""))
            val folder = if (tag == "en") "values" else "values-$tag"
            assertTrue(tag, File("src/main/res/$folder/strings.xml").isFile)
        }
    }

    @Test
    fun shippedLocalesHaveEnglishKeys() {
        val enNames = stringNames(File("src/main/res/values/strings.xml"))
        AppLanguageTags.pickerOrder.filter { it != "en" }.forEach { tag ->
            val names = stringNames(File("src/main/res/values-$tag/strings.xml"))
            val missing = enNames - names
            assertTrue("$tag missing $missing", missing.isEmpty())
        }
    }

    private fun stringNames(file: File): Set<String> {
        val pattern = Regex("<string name=\"([^\"]+)\"")
        return pattern.findAll(file.readText()).map { it.groupValues[1] }.toSet()
    }
}
