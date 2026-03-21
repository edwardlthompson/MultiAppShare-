package com.multiappshare.model

import kotlinx.serialization.Serializable

@Serializable
data class HistoryItem(
    val timestamp: Long,
    val groupName: String,
    val contentDescription: String,
    val status: String,
    val isError: Boolean = false
)
