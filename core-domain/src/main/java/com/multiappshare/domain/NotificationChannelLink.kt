package com.multiappshare.domain

object NotificationChannelLink {
    const val ACTION_APP_NOTIFICATION_SETTINGS = "android.settings.APP_NOTIFICATION_SETTINGS"
    const val ACTION_CHANNEL_NOTIFICATION_SETTINGS = "android.settings.CHANNEL_NOTIFICATION_SETTINGS"
    const val EXTRA_APP_PACKAGE = "android.provider.extra.APP_PACKAGE"
    const val EXTRA_CHANNEL_ID = "android.provider.extra.CHANNEL_ID"

    fun buildChannelSettingsIntentData(
        packageName: String,
        channelId: String?,
    ): Map<String, String> {
        val params = mutableMapOf(EXTRA_APP_PACKAGE to packageName)
        if (!channelId.isNullOrBlank()) {
            params[EXTRA_CHANNEL_ID] = channelId
        }
        return params
    }
}
