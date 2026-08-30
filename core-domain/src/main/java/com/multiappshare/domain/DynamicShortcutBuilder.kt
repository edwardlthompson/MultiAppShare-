package com.multiappshare.domain

import com.multiappshare.model.AppGroup

data class DynamicShortcutItem(
    val id: String,
    val shortLabel: String,
    val longLabel: String,
    val rank: Int,
)

object DynamicShortcutBuilder {
    const val MAX_SHORTCUTS = 4

    fun buildTopShortcuts(groups: List<AppGroup>): List<DynamicShortcutItem> {
        return groups
            .filter { it.name.isNotBlank() }
            .sortedByDescending { it.usageCount }
            .take(MAX_SHORTCUTS)
            .mapIndexed { index, group ->
                DynamicShortcutItem(
                    id = "group_${group.id.ifBlank { group.name }}",
                    shortLabel = group.name.take(10),
                    longLabel = "Share to ${group.name}",
                    rank = index,
                )
            }
    }
}
