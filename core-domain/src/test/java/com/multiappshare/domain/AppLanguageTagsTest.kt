package com.multiappshare.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AppLanguageTagsTest {

    @Test
    fun sanitizesSupportedLanguages() {
        assertEquals("en", AppLanguageTags.sanitize("en-US"))
        assertEquals("fr", AppLanguageTags.sanitize("fr-FR"))
        assertEquals("es", AppLanguageTags.sanitize("es-ES"))
        assertEquals("de", AppLanguageTags.sanitize("de-DE"))
        assertEquals("it", AppLanguageTags.sanitize("it-IT"))
    }

    @Test
    fun returnsNullForUnsupportedOrMalformedTags() {
        assertNull(AppLanguageTags.sanitize(null))
        assertNull(AppLanguageTags.sanitize(""))
        assertNull(AppLanguageTags.sanitize("xx-YY"))
    }
}
