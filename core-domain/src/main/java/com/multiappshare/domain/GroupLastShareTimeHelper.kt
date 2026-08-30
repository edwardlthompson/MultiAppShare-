package com.multiappshare.domain

object GroupLastShareTimeHelper {
    private const val MS_PER_MINUTE = 60 * 1000L
    private const val MS_PER_HOUR = 60 * MS_PER_MINUTE
    private const val MS_PER_DAY = 24 * MS_PER_HOUR

    fun formatRelativeTime(lastShareTimestamp: Long?, nowTimestamp: Long = System.currentTimeMillis()): String {
        if (lastShareTimestamp == null || lastShareTimestamp <= 0L) return "Never shared"
        val diff = (nowTimestamp - lastShareTimestamp).coerceAtLeast(0L)
        return when {
            diff < MS_PER_MINUTE -> "Just now"
            diff < MS_PER_HOUR -> "${diff / MS_PER_MINUTE}m ago"
            diff < MS_PER_DAY -> "${diff / MS_PER_HOUR}h ago"
            else -> "${diff / MS_PER_DAY}d ago"
        }
    }
}
