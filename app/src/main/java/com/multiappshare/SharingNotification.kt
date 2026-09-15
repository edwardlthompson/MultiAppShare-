package com.multiappshare

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.multiappshare.domain.ShareSessionChrome
import com.multiappshare.share.ShareNotificationIntents

internal object SharingNotification {
    const val NOTIFICATION_ID = 1
    const val CHANNEL_ID = "sharing_service_channel_v2"
    private const val REQUEST_CONTENT = 1
    private const val REQUEST_NEXT = 2
    private const val REQUEST_SKIP = 3
    private const val REQUEST_CANCEL = 4

    fun channel(context: Context): NotificationChannel =
        NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_sharing_name),
            NotificationManager.IMPORTANCE_LOW,
        ).apply {
            description = context.getString(R.string.notification_channel_sharing_description)
            setShowBadge(false)
        }

    fun builder(
        context: Context,
        appComponents: List<String>,
        currentIndex: Int,
    ): NotificationCompat.Builder {
        val key = appComponents.getOrNull(currentIndex).orEmpty()
        val targetLabel = resolveShareTargetLabel(context.packageManager, key)
            .ifBlank { context.getString(R.string.app_name) }
        val title = sessionTitle(context, targetLabel, currentIndex, appComponents.size)
        val hint = context.getString(R.string.notification_open_to_continue)
        val body = context.getString(
            R.string.notification_sharing_text,
            targetLabel,
            currentIndex + 1,
            appComponents.size,
            hint,
        )
        val hasMore = currentIndex + 1 < appComponents.size
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(hint)
            .setStyle(NotificationCompat.BigTextStyle().bigText("$title. $body"))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSilent(true)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setOngoing(true)
            .setContentIntent(ShareNotificationIntents.pending(context, REQUEST_CONTENT, null))
            .addAction(
                0,
                context.getString(R.string.notification_action_next),
                ShareNotificationIntents.pending(context, REQUEST_NEXT, ShareNotificationIntents.CMD_NEXT),
            )
            .apply {
                if (hasMore) {
                    addAction(
                        0,
                        context.getString(R.string.notification_action_skip),
                        ShareNotificationIntents.pending(
                            context,
                            REQUEST_SKIP,
                            ShareNotificationIntents.CMD_SKIP_ONE,
                        ),
                    )
                }
            }
            .addAction(
                0,
                context.getString(R.string.notification_action_cancel),
                ShareNotificationIntents.pending(context, REQUEST_CANCEL, ShareNotificationIntents.CMD_CANCEL),
            )
    }

    internal fun sessionTitle(
        context: Context,
        appLabel: String,
        currentIndex: Int,
        total: Int,
    ): String {
        val identity = ShareSessionChrome.identity(appLabel, currentIndex, total)
            ?: return context.getString(R.string.notification_sharing_title)
        return context.getString(
            R.string.share_session_chrome_title,
            identity.step,
            identity.total,
            identity.appLabel,
        )
    }
}
