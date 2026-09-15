package com.multiappshare.domain

object SharingHeadline {
    const val SHOW_NEXT_APP_LINE = false

    fun title(appLabel: String, contentFallback: String): String =
        appLabel.ifBlank { contentFallback }
}
