package com.multiappshare.domain

object HomeWidgetModel {
    data class WidgetState(
        val primaryGroupLabel: String,
        val hasClipboardTarget: Boolean,
    )

    fun createWidgetState(
        mostRecentGroupName: String?,
        clipboardEnabled: Boolean = true,
    ): WidgetState {
        val label = mostRecentGroupName?.trim()?.ifBlank { "Recent Group" } ?: "Recent Group"
        return WidgetState(
            primaryGroupLabel = label,
            hasClipboardTarget = clipboardEnabled,
        )
    }
}
