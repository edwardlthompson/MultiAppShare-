package com.multiappshare.model

import kotlinx.serialization.Serializable

@Serializable
data class AppGroup(
    val name: String,
    val apps: List<AppInfo>,
    val isExpanded: Boolean = false,
    val usageCount: Int = 0
)
