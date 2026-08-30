package com.multiappshare.updates

import android.content.Context
import android.os.Build

object InstallSource {
    fun installerPackage(context: Context): String? {
        val pm = context.packageManager
        val pkg = context.packageName
        return runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                pm.getInstallSourceInfo(pkg).installingPackageName
            } else {
                @Suppress("DEPRECATION")
                pm.getInstallerPackageName(pkg)
            }
        }.getOrNull()
    }
}
