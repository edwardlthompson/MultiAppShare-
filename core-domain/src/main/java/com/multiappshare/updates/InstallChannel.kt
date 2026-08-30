package com.multiappshare.updates

object InstallChannel {
    const val FDROID_LISTING =
        "https://f-droid.org/packages/com.edwardlthompson.multiappshare/"

    private val fdroidInstallers = setOf(
        "org.fdroid.fdroid",
        "org.fdroid.basic",
        "org.fdroid.fdroid.privileged",
        "org.fdroid.fdroid.privileged.ota",
        "org.fdroid.lite",
        "nya.kitsunyan.foxydroid",
        "com.looker.droidify",
        "eu.bubu1.fdroidclassic",
        "in.sunilpaulmathew.izzyondroid",
    )

    fun allowsDirectApk(installerPackage: String?): Boolean {
        val pkg = installerPackage?.trim().orEmpty()
        if (pkg.isEmpty()) return true
        return pkg !in fdroidInstallers && !pkg.contains("fdroid", ignoreCase = true)
    }

    fun updateUrl(allowDirectApk: Boolean, apkUrl: String): String =
        if (allowDirectApk) apkUrl else FDROID_LISTING

    fun getSourceLabel(installerPackage: String?): String {
        val pkg = installerPackage?.trim().orEmpty()
        return when {
            pkg.isEmpty() -> "Sideload / Direct APK"
            pkg in fdroidInstallers || pkg.contains("fdroid", ignoreCase = true) -> "F-Droid"
            pkg.contains("google", ignoreCase = true) -> "Google Play"
            else -> "Installer ($pkg)"
        }
    }
}
