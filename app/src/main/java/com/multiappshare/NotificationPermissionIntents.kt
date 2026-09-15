package com.multiappshare

import android.content.Context
import android.content.Intent
import com.multiappshare.domain.NotificationChannelLink

internal object NotificationPermissionIntents {
    fun appSettings(context: Context): Intent {
        val extras = NotificationChannelLink.buildChannelSettingsIntentData(
            context.packageName,
            SharingNotification.CHANNEL_ID,
        )
        return Intent(NotificationChannelLink.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            extras.forEach { (key, value) -> putExtra(key, value) }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
