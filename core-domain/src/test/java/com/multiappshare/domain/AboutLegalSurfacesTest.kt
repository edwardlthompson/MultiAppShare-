package com.multiappshare.domain

import org.junit.Assert.assertTrue
import org.junit.Test

class AboutLegalSurfacesTest {

    @Test
    fun privacyAndLicensesAreReadableOfflineFromSettings() {
        assertTrue(AboutLegalSurfaces.privacyOffline())
        assertTrue(AboutLegalSurfaces.licensesListed())
        assertTrue(AboutLegalSurfaces.settingsExposesLegal())
    }
}
