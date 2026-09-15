package com.multiappshare.domain

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object BackupExportName {
    private val day = DateTimeFormatter.ISO_LOCAL_DATE

    fun json(epochMillis: Long, zone: ZoneId = ZoneId.systemDefault()): String {
        val date = Instant.ofEpochMilli(epochMillis).atZone(zone).toLocalDate()
        return "multiappshare-groups-${date.format(day)}.json"
    }
}
