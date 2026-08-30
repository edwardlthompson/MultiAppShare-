package com.multiappshare.domain

import com.multiappshare.model.AppInfo

object AppSuccessSorter {
    fun sortByLastSuccess(
        apps: List<AppInfo>,
        lastSuccessTimestamps: Map<String, Long>,
    ): List<AppInfo> {
        if (lastSuccessTimestamps.isEmpty()) return apps
        return apps.sortedWith(
            compareByDescending<AppInfo> { lastSuccessTimestamps[it.packageName] ?: 0L }
                .thenBy { it.appName.lowercase() },
        )
    }
}
