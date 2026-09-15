package com.multiappshare.domain

object AppLanguageTags {
    val pickerOrder = listOf("en", "de", "es", "fr", "it")
    val supported = pickerOrder.toSet()

    fun sanitize(tag: String?): String? {
        val primary = tag?.trim()?.lowercase()?.substringBefore('-') ?: return null
        return primary.takeIf { it in supported }
    }
}
