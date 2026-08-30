package com.multiappshare.domain

import com.multiappshare.model.AppGroup

object GroupNamesClipboardFormatter {
    fun formatGroupApps(group: AppGroup): String {
        if (group.apps.isEmpty()) return "${group.name}: (no apps)"
        val appList = group.apps.joinToString(", ") { it.appName }
        return "${group.name}: $appList"
    }

    fun formatAllGroups(groups: List<AppGroup>): String {
        if (groups.isEmpty()) return ""
        return groups.joinToString("\n") { formatGroupApps(it) }
    }
}
