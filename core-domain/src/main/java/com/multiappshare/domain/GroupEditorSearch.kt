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
}
