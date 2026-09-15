package com.multiappshare.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.multiappshare.DeeplinkContract
import com.multiappshare.MainActivity
import com.multiappshare.R
import com.multiappshare.domain.HomeWidgetModel
import com.multiappshare.qs.ShareClipboardTileService

class HomeShareWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val state = HomeWidgetModel.createWidgetState(
            WidgetPrefs.load(context),
            fallbackLabel = context.getString(R.string.widget_recent_group),
        )
        appWidgetIds.forEach { id ->
            appWidgetManager.updateAppWidget(id, buildViews(context, state))
        }
    }

    companion object {
        fun refresh(context: Context, mostRecentGroupName: String?) {
            WidgetPrefs.save(context, mostRecentGroupName)
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, HomeShareWidget::class.java))
            if (ids.isEmpty()) return
            HomeShareWidget().onUpdate(context, manager, ids)
        }
    }
}

internal object WidgetPrefs {
    private const val PREFS = "mas_widget"
    private const val KEY_GROUP = "recent_group"

    fun save(context: Context, groupName: String?) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_GROUP, groupName)
            .apply()
    }

    fun load(context: Context): String? =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY_GROUP, null)
}

private fun buildViews(context: Context, state: HomeWidgetModel.WidgetState): RemoteViews {
    val views = RemoteViews(context.packageName, R.layout.widget_home_share)
    views.setTextViewText(R.id.widget_group_label, state.primaryGroupLabel)
    val groupIntent = Intent(Intent.ACTION_VIEW).apply {
        data = "${DeeplinkContract.SCHEME}://${DeeplinkContract.HOST_GROUP}?${DeeplinkContract.QUERY_GROUP_NAME}=${
            android.net.Uri.encode(state.primaryGroupLabel)
        }".toUri()
        setPackage(context.packageName)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
    }
    views.setOnClickPendingIntent(R.id.widget_group_label, activityPending(context, groupIntent, 1))
    if (state.hasClipboardTarget) {
        val clip = Intent(context, MainActivity::class.java).apply {
            action = ShareClipboardTileService.ACTION_QS_SHARE_CLIPBOARD
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        views.setOnClickPendingIntent(R.id.widget_clipboard, activityPending(context, clip, 2))
        views.setViewVisibility(R.id.widget_clipboard, android.view.View.VISIBLE)
    } else {
        views.setViewVisibility(R.id.widget_clipboard, android.view.View.GONE)
    }
    return views
}

private fun activityPending(context: Context, intent: Intent, requestCode: Int): PendingIntent {
    val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    return PendingIntent.getActivity(context, requestCode, intent, flags)
}
