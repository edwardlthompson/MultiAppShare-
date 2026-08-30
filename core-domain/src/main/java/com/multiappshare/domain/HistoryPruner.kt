package com.multiappshare.domain

import com.multiappshare.model.HistoryItem

enum class HistoryRetentionPeriod(val days: Int) {
    DAYS_30(30),
    DAYS_90(90),
    FOREVER(-1),
}

object HistoryPruner {
    private const val MS_PER_DAY = 24 * 60 * 60 * 1000L

    fun prune(
        items: List<HistoryItem>,
        period: HistoryRetentionPeriod,
        nowTimestamp: Long = System.currentTimeMillis(),
    ): List<HistoryItem> {
        if (period == HistoryRetentionPeriod.FOREVER) return items
        val cutoff = nowTimestamp - (period.days * MS_PER_DAY)
        return items.filter { it.timestamp >= cutoff }
    }
}
