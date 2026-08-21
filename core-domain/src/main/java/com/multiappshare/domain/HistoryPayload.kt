package com.multiappshare.domain

import com.multiappshare.model.HistoryItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object HistoryPayload {
    private val json = Json { ignoreUnknownKeys = true }

    fun encode(snapshot: ShareSessionSnapshot): String =
        json.encodeToString(
            snapshot.copy(appPackages = emptyList(), currentIndex = 0, sharingStarted = false),
        )

    fun decode(raw: String?): ShareSessionSnapshot? {
        if (raw.isNullOrBlank()) return null
        return runCatching { json.decodeFromString<ShareSessionSnapshot>(raw) }.getOrNull()
    }

    fun HistoryItem.reshareSnapshot(): ShareSessionSnapshot? =
        decode(payloadJson)?.takeIf { it.hasPayload() }
}
