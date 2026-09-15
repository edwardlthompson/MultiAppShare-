package com.multiappshare.core.ui

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

object ExternalIntent {
    fun tryStart(context: Context, intent: Intent): Boolean {
        val launch = Intent(intent)
        if (context !is Activity) {
            launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return try {
            context.startActivity(launch)
            true
        } catch (_: ActivityNotFoundException) {
            false
        }
    }

    fun tryViewUrl(context: Context, url: String): Boolean =
        tryStart(context, Intent(Intent.ACTION_VIEW, url.toUri()))
}
