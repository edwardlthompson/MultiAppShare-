package com.multiappshare.domain

object ScreenHeadings {
    enum class Role { GROUPS, CHOOSE_GROUP, SHARE_SESSION, SETTINGS }

    fun documentHeading(inShareMode: Boolean, sharingStarted: Boolean): Role = when {
        inShareMode && sharingStarted -> Role.SHARE_SESSION
        inShareMode -> Role.CHOOSE_GROUP
        else -> Role.GROUPS
    }

    const val OVERLAY_COPY_IS_DOCUMENT_HEADING = false
}
