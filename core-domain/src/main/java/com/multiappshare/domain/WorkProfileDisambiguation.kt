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

    fun userIdFromUid(uid: Int): Int = uid / PER_USER_RANGE

    private const val PER_USER_RANGE = 100_000
}
