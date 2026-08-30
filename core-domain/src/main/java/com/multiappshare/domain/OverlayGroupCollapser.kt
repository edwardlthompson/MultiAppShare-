package com.multiappshare.domain

import com.multiappshare.model.AppGroup

object OverlayGroupCollapser {
    fun resolveExpansionStates(
        groups: List<AppGroup>,
        compatibleGroupIds: Set<String>,
        defaultCollapseIncompatible: Boolean = true,
    ): List<AppGroup> {
        if (!defaultCollapseIncompatible) return groups
        return groups.map { group ->
            val hasCompatibleApps = group.id in compatibleGroupIds || group.name in compatibleGroupIds
            if (!hasCompatibleApps) {
                group.copy(isExpanded = false)
            } else {
                group
            }
        }
    }
}
