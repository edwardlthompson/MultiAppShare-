package com.multiappshare.domain

object AppLanguageTags {
    val supported = setOf("en", "fr", "es")

    fun sanitize(tag: String?): String? {
        val primary = tag?.trim()?.lowercase()?.substringBefore('-') ?: return null
        return primary.takeIf { it in supported }
    }
}
