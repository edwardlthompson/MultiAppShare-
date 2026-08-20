package com.multiappshare.locale

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.multiappshare.domain.AppLanguageTags

object AppLanguage {
    fun apply(tag: String?) {
        val sanitized = AppLanguageTags.sanitize(tag)
        val locales = if (sanitized == null) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(sanitized)
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }
}
