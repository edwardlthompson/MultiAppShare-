package com.multiappshare.domain

import com.multiappshare.model.AppInfo

object GroupEditorSearch {
    fun filter(apps: List<AppInfo>, query: String?): List<AppInfo> {
        val trimmed = query?.trim().orEmpty()
        if (trimmed.isEmpty()) return apps
        return apps.filter { app ->
            app.appName.contains(trimmed, ignoreCase = true) ||
                app.packageName.contains(trimmed, ignoreCase = true)
        }
    }

    fun isSelected(selected: List<AppInfo>, app: AppInfo): Boolean =
        selected.any { sameActivity(it, app) }

    fun withToggled(selected: List<AppInfo>, app: AppInfo): List<AppInfo> =
        if (isSelected(selected, app)) {
            selected.filterNot { sameActivity(it, app) }
        } else {
            selected + app
        }

    fun chipLabel(appName: String): String = appName.trim()

    fun withAllVisible(selected: List<AppInfo>, visible: List<AppInfo>): List<AppInfo> {
        val extra = visible.filterNot { isSelected(selected, it) }
        return selected + extra
    }

    fun showEmptySearch(query: String, filteredCount: Int): Boolean =
        query.trim().isNotEmpty() && filteredCount == 0

    private fun sameActivity(a: AppInfo, b: AppInfo): Boolean =
        a.packageName == b.packageName && a.activityName == b.activityName
}
