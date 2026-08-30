package com.multiappshare.about

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AboutLinksTest {

    @Test
    fun changelogAndDonateAreHttps() {
        assertTrue(AboutLinks.isHttpsUrl(AboutLinks.CHANGELOG))
        assertTrue(AboutLinks.isHttpsUrl(AboutLinks.VENMO))
        assertTrue(AboutLinks.isHttpsUrl(AboutLinks.GITHUB_SPONSORS))
        assertTrue(AboutLinks.isHttpsUrl(AboutLinks.LIBERAPAY))
        assertTrue(AboutLinks.isHttpsUrl(AboutLinks.TELEGRAM))
    }

    @Test
    fun rejectsEmptyAndNonHttps() {
        assertFalse(AboutLinks.isHttpsUrl(""))
        assertFalse(AboutLinks.isHttpsUrl("http://example.com"))
        assertFalse(AboutLinks.isHttpsUrl("https://"))
    }
}
