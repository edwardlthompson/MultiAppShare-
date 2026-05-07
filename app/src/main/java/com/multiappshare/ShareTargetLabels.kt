package com.multiappshare

import android.content.ComponentName
import android.content.pm.PackageManager

/**
 * Resolves a share target key (`package/activity` or `package` only) to a user-visible label.
 */
fun resolveShareTargetLabel(packageManager: PackageManager, componentKey: String): String {
    val trimmed = componentKey.trim()
    if (trimmed.isEmpty()) return ""

    if (!trimmed.contains("/")) {
        return runCatching {
            @Suppress("DEPRECATION")
            val appInfo = packageManager.getApplicationInfo(trimmed, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        }.getOrElse { trimmed }
    }

    val parts = trimmed.split("/", limit = 2)
    val pkg = parts[0]
    val activity = parts.getOrNull(1).orEmpty()

    return runCatching {
        if (activity.isNotEmpty()) {
            val cn = ComponentName(pkg, activity)
            @Suppress("DEPRECATION")
            val info = packageManager.getActivityInfo(cn, 0)
            info.loadLabel(packageManager).toString()
        } else {
            @Suppress("DEPRECATION")
            val appInfo = packageManager.getApplicationInfo(pkg, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        }
    }.getOrElse { pkg }
}
