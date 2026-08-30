package com.multiappshare.domain

object WorkProfileDisambiguation {
    fun formatAppLabel(
        rawAppName: String,
        isWorkProfile: Boolean,
    ): String {
        val cleanName = rawAppName.trim()
        return if (isWorkProfile) {
            "$cleanName (Work)"
        } else {
            cleanName
        }
    }

    fun isWorkProfileUser(userId: Int): Boolean = userId != 0
}
