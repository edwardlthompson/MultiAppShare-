package com.multiappshare.domain

import com.multiappshare.model.AppGroup

object GroupRepair {
    fun pruneUninstalled(groups: List<AppGroup>, installedPackageNames: Set<String>): List<AppGroup> {
        return groups.map { group ->
            val validApps = group.apps.filter { it.packageName in installedPackageNames }
            group.copy(apps = validApps)
        }
    }

    fun countMissingPackages(groups: List<AppGroup>, installedPackageNames: Set<String>): Int {
        var count = 0
        for (group in groups) {
            for (app in group.apps) {
                if (app.packageName !in installedPackageNames) {
                    count++
                }
            }
        }
        return count
    }
}
