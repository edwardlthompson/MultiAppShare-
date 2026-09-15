package com.multiappshare.domain

object NotificationPermissionPolicy {
    const val POST_NOTIFICATIONS_SDK = 33
    const val PROMPT_ON_COLD_START = false

    fun shouldPromptForShare(sdkInt: Int, permissionGranted: Boolean): Boolean =
        sdkInt >= POST_NOTIFICATIONS_SDK && !permissionGranted

    fun shouldShowSettingsBanner(
        sdkInt: Int,
        permissionGranted: Boolean,
        inShareSession: Boolean,
    ): Boolean = sdkInt >= POST_NOTIFICATIONS_SDK && !permissionGranted && inShareSession
}
