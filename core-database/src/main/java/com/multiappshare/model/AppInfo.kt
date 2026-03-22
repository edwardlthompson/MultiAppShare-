package com.multiappshare.model

import kotlinx.serialization.Serializable

@Serializable
data class AppInfo(
    val appName: String,
    val packageName: String,
    val activityName: String = "",
    val category: Int = -1
)
