package com.multiappshare.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "groups",
    indices = [Index(value = ["id"], unique = true)],
)
@Serializable
data class AppGroup(
    @PrimaryKey val name: String,
    val apps: List<AppInfo>,
    val isExpanded: Boolean = false,
    val usageCount: Int = 0,
    val id: String = "",
)
