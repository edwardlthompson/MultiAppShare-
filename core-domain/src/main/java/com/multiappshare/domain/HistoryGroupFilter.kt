package com.multiappshare.domain

import com.multiappshare.model.HistoryItem

object HistoryGroupFilter {
    fun filter(
        items: List<HistoryItem>,
        groupName: String?,
    ): List<HistoryItem> {
        val trimmed = groupName?.trim().orEmpty()
        if (trimmed.isEmpty()) return items
        return items.filter { it.groupName.equals(trimmed, ignoreCase = true) }
    }

    fun extractDistinctGroups(items: List<HistoryItem>): List<String> {
        return items.map { it.groupName.trim() }
            .filter { it.isNotEmpty() }
            .distinct()
            .sortedBy { it.lowercase() }
    }
}
