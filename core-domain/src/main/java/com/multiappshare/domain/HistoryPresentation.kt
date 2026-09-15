package com.multiappshare.domain

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.IllegalFormatException
import java.util.Locale

object HistoryPresentation {
    fun formatTimestamp(
        timestampMillis: Long,
        locale: Locale,
        zoneId: ZoneId = ZoneId.systemDefault(),
    ): String {
        val zoned = Instant.ofEpochMilli(timestampMillis).atZone(zoneId)
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(locale).format(zoned)
    }

    fun sharedLine(template: String, description: String): String {
        val desc = description.trim()
        if (template.isBlank()) return desc
        return try {
            template.format(desc)
        } catch (_: IllegalFormatException) {
            desc
        }
    }
}
