package com.multiappshare.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OpenSourceLicensesTest {

    @Test
    fun containsStandardCoreDependenciesWithHttpsUrls() {
        val licenses = OpenSourceLicenseCatalog.LICENSES
        assertTrue(licenses.isNotEmpty())
        for (license in licenses) {
            assertTrue(license.name.isNotBlank())
            assertTrue(license.author.isNotBlank())
            assertTrue(license.licenseName.isNotBlank())
            assertTrue(license.url.startsWith("https://"))
            assertFalse(license.licenseName.contains("Proprietary"))
        }
    }
}
