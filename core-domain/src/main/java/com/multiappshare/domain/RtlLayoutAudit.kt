package com.multiappshare.domain

object RtlLayoutAudit {
    val RTL_LANGUAGE_CODES = setOf("ar", "fa", "he", "ur", "yi", "ps", "sd")

    fun isRtlLocale(languageCode: String?): Boolean {
        val clean = languageCode?.trim()?.lowercase()?.substringBefore('-') ?: return false
        return clean in RTL_LANGUAGE_CODES
    }
}
