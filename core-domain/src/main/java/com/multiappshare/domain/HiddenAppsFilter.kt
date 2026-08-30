package com.multiappshare.domain

import com.multiappshare.model.AppInfo

object HiddenAppsFilter {
    fun filterVisibleApps(
        allApps: List<AppInfo>,
        hiddenPackages: Set<String>,
    ): List<AppInfo> {
        if (hiddenPackages.isEmpty()) return allApps
        return allApps.filter { it.packageName !in hiddenPackages }
    }

    fun toggleHidden(
        hiddenPackages: Set<String>,
        packageName: String,
    ): Set<String> {
        if (packageName.isBlank()) return hiddenPackages
        return if (packageName in hiddenPackages) {
            hiddenPackages - packageName
        } else {
            hiddenPackages + packageName
        }
    }
}
