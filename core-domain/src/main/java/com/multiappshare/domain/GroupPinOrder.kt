package com.multiappshare.domain

import com.multiappshare.model.AppGroup

object GroupPinOrder {
    fun sortGroupsWithPinned(
        groups: List<AppGroup>,
        pinnedGroupNames: Set<String>,
    ): List<AppGroup> {
        val (pinned, unpinned) = groups.partition { it.name in pinnedGroupNames }
        val sortedPinned = pinned.sortedByDescending { it.usageCount }
        val sortedUnpinned = unpinned.sortedByDescending { it.usageCount }
        return sortedPinned + sortedUnpinned
    }

    fun togglePin(pinnedGroupNames: Set<String>, groupName: String): Set<String> {
        return if (groupName in pinnedGroupNames) {
            pinnedGroupNames - groupName
        } else {
            pinnedGroupNames + groupName
        }
    }
}
