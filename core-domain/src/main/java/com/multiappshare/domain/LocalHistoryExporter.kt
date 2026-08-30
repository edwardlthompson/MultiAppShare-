package com.multiappshare.domain

import com.multiappshare.model.HistoryItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class SanitizedHistoryRecord(
    val timestamp: Long,
    val groupName: String,
    val status: String,
    val isError: Boolean,
)

@Serializable
data class LocalHistoryExportDocument(
    val exportTimestamp: Long,
    val records: List<SanitizedHistoryRecord>,
)

object LocalHistoryExporter {
    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    fun exportToJson(
        items: List<HistoryItem>,
        exportTimestamp: Long = System.currentTimeMillis(),
    ): String {
        val sanitizedRecords = items.map {
            SanitizedHistoryRecord(
                timestamp = it.timestamp,
                groupName = it.groupName,
                status = it.status,
                isError = it.isError,
            )
        }
        val document = LocalHistoryExportDocument(
            exportTimestamp = exportTimestamp,
            records = sanitizedRecords,
        )
        return json.encodeToString(document)
    }
}
