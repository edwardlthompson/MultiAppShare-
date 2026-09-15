package com.multiappshare

import org.junit.Assert.assertFalse
import org.junit.Test
import java.io.File

class ShareReturnCopyTest {

    @Test
    fun overlayReturnCopyHasNoUriScheme() {
        val dirs = listOf("values", "values-es", "values-fr")
        val pattern = Regex("<string name=\"sharing_return_instruction\">([^<]+)</string>")
        dirs.forEach { dir ->
            val xml = File("src/main/res/$dir/strings.xml").readText()
            val body = pattern.find(xml)?.groupValues?.get(1).orEmpty()
            assertFalse(dir, body.isEmpty())
            assertFalse(dir, body.contains("://"))
            assertFalse(dir, body.contains("multiappshare://", ignoreCase = true))
        }
    }
}
