package com.multiappshare.domain

object ShareSessionChrome {
    data class Identity(val step: Int, val total: Int, val appLabel: String)

    fun showProgressTitle(sharingStarted: Boolean, packageCount: Int): Boolean =
        sharingStarted && packageCount > 0

    fun identity(appLabel: String, currentIndex: Int, total: Int): Identity? {
        val label = appLabel.trim()
        return if (total <= 0 || label.isEmpty()) {
            null
        } else {
            Identity(
                step = (currentIndex + 1).coerceIn(1, total),
                total = total,
                appLabel = label,
            )
        }
    }
}
