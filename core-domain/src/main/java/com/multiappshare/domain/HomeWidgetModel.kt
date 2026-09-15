package com.multiappshare.domain

object HomeWidgetModel {
    data class WidgetState(
        val primaryGroupLabel: String,
        val hasClipboardTarget: Boolean,
    )

    fun createWidgetState(
        mostRecentGroupName: String?,
        clipboardEnabled: Boolean = true,
        fallbackLabel: String,
    ): WidgetState {
        val trimmed = mostRecentGroupName?.trim().orEmpty()
        val label = trimmed.ifBlank { fallbackLabel }
        return WidgetState(
            primaryGroupLabel = label,
            hasClipboardTarget = clipboardEnabled,
        )
    }
}
