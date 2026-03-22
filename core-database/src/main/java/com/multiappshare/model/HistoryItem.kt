package com.multiappshare.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "history")
@Serializable
data class HistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val groupName: String,
    val contentDescription: String,
    val status: String,
    val isError: Boolean = false
)
