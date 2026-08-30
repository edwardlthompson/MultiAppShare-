package com.multiappshare.domain

object AppInfoShortcutHelper {
    const val ACTION_APPLICATION_DETAILS_SETTINGS = "android.settings.APPLICATION_DETAILS_SETTINGS"

    fun buildUri(packageName: String): String {
        val clean = packageName.trim()
        return "package:$clean"
    }

    fun isValidPackageName(packageName: String?): Boolean {
        val clean = packageName?.trim().orEmpty()
        return clean.isNotEmpty() && clean.contains('.')
    }
}
