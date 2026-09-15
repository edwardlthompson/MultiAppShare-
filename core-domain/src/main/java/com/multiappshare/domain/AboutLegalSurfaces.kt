package com.multiappshare.domain

object AboutLegalSurfaces {
    fun privacyOffline(): Boolean = PrivacyPolicyText.SECTIONS.isNotEmpty()

    fun licensesListed(): Boolean = OpenSourceLicenseCatalog.LICENSES.isNotEmpty()

    fun settingsExposesLegal(): Boolean =
        SettingsCatalog.ENTRIES.any { it.id == "privacy" } &&
            SettingsCatalog.ENTRIES.any { it.id == "licenses" }
}
