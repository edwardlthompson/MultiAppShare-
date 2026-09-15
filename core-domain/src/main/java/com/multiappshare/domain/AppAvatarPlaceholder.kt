package com.multiappshare.domain

object AppAvatarPlaceholder {
    fun initial(appName: String): String {
        val trimmed = appName.trim()
        if (trimmed.isEmpty()) return "?"
        return trimmed.first().uppercaseChar().toString()
    }
}
