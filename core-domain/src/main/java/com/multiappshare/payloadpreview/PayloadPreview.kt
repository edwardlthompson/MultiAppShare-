package com.multiappshare.payloadpreview

object PayloadPreview {
    const val TEXT_LIMIT = 140

    fun shouldShow(hasPayload: Boolean, sharingStarted: Boolean): Boolean =
        hasPayload && !sharingStarted

    fun mimeLabel(mime: String?): String = mime?.trim()?.ifBlank { null } ?: "*/*"

    fun uriCount(uris: List<*>?): Int = uris?.size ?: 0

    fun textSnippet(text: String?): String {
        val trimmed = text?.trim().orEmpty()
        if (trimmed.length <= TEXT_LIMIT) return trimmed
        return trimmed.take(TEXT_LIMIT) + "…"
    }
}
