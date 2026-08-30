package com.multiappshare.domain

object GroupColorLabel {
    val PRESET_EMOJIS = listOf("📁", "💬", "📷", "🎵", "💼", "🎮", "⭐", "🚀")

    fun formatWithEmoji(emoji: String?, rawName: String): String {
        val cleanName = stripEmoji(rawName).trim()
        val cleanEmoji = emoji?.trim().orEmpty()
        if (cleanEmoji.isEmpty()) return cleanName
        return if (cleanName.isEmpty()) cleanEmoji else "$cleanEmoji $cleanName"
    }

    fun extractEmoji(name: String): String? {
        val trimmed = name.trim()
        return PRESET_EMOJIS.firstOrNull { trimmed.startsWith(it) }
    }

    fun stripEmoji(name: String): String {
        var result = name.trim()
        for (emoji in PRESET_EMOJIS) {
            if (result.startsWith(emoji)) {
                result = result.removePrefix(emoji).trim()
            }
        }
        return result
    }
}
