package com.multiappshare.share

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.multiappshare.MainActivity

object ShareNotificationIntents {
    const val EXTRA_SHARE_COMMAND = "com.multiappshare.EXTRA_SHARE_COMMAND"
    const val CMD_NEXT = "next"
    const val CMD_SKIP_ONE = "skip_one"
    const val CMD_CANCEL = "cancel"

    fun commandOf(intent: Intent?): String? = intent?.getStringExtra(EXTRA_SHARE_COMMAND)

    fun activityIntent(context: Context, command: String?): Intent =
        Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_MAIN
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (command != null) putExtra(EXTRA_SHARE_COMMAND, command)
        }

    fun pending(context: Context, requestCode: Int, command: String?): PendingIntent =
        PendingIntent.getActivity(
            context,
            requestCode,
            activityIntent(context, command),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
}
