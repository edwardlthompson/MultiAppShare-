package com.multiappshare.domain

object GroupRowMeta {
    const val MS_PER_DAY = 86_400_000L

    fun latestTimestampByName(events: List<Pair<String, Long>>): Map<String, Long> {
        val latest = mutableMapOf<String, Long>()
        for ((name, timestamp) in events) {
            val current = latest[name]
            if (current == null || timestamp > current) {
                latest[name] = timestamp
            }
        }
        return latest
    }

    fun daysSince(lastSharedMillis: Long?, nowMillis: Long): Int? {
        if (lastSharedMillis == null) return null
        val delta = (nowMillis - lastSharedMillis).coerceAtLeast(0L)
        return (delta / MS_PER_DAY).toInt()
    }
}
