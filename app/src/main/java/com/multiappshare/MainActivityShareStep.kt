package com.multiappshare

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.multiappshare.domain.GroupDelayOverride
import com.multiappshare.domain.SharingDelay
import com.multiappshare.model.AppGroup
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class MainActivityShareStep(
    private val activity: MainActivity,
    private val viewModel: MainViewModel,
) {
    private var clipData: ClipData? = null
    private var delayJob: Job? = null

    fun shareStep(uris: List<Uri>?, text: String?, mime: String, components: List<String>, index: Int) {
        delayJob?.cancel()
        val start = { startShareService(uris, text, mime, components, index) }
        if (index <= 0) {
            start()
            return
        }
        delayJob = activity.lifecycleScope.launch {
            val waitMs = GroupDelayOverride.resolveDelayMs(viewModel.sharingDelayMs, null)
            if (waitMs > 0) delay(waitMs.toLong())
            start()
        }
    }

    private fun startShareService(
        uris: List<Uri>?,
        text: String?,
        mime: String,
        components: List<String>,
        index: Int,
    ) {
        clipData = attachClipData(uris, mime)
        val serviceIntent = Intent(activity, SharingService::class.java).apply {
            action = SharingService.ACTION_START_SHARING
            type = mime
            if (uris != null) {
                putParcelableArrayListExtra(SharingService.EXTRA_IMAGE_URIS, ArrayList(uris))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            putExtra(Intent.EXTRA_TEXT, text)
            putStringArrayListExtra(SharingService.EXTRA_APP_COMPONENTS, ArrayList(components))
            putExtra(SharingService.EXTRA_CURRENT_INDEX, index)
        }
        ContextCompat.startForegroundService(activity, serviceIntent)
    }

    private fun attachClipData(uris: List<Uri>?, mime: String): ClipData? {
        if (uris == null) return null
        var data: ClipData? = null
        for (uri in uris) {
            val item = ClipData.Item(uri)
            data = if (data == null) ClipData(null, arrayOf(mime), item) else data.apply { addItem(item) }
        }
        return data
    }

    fun stopSharingService() {
        delayJob?.cancel()
        val serviceIntent = Intent(activity, SharingService::class.java).apply {
            action = SharingService.ACTION_STOP
        }
        activity.startService(serviceIntent)
        activity.stopService(serviceIntent)
    }

    fun compatiblePackages(uris: List<Uri>?, mime: String, group: AppGroup): List<String> {
        val shareAction = if (uris != null && uris.size > 1) Intent.ACTION_SEND_MULTIPLE else Intent.ACTION_SEND
        val compatiblePackages = viewModel.getCompatiblePackages(shareAction, mime)
        val compatible = mutableListOf<String>()
        val incompatible = mutableListOf<String>()
        for (app in group.apps) {
            val componentKey = "${app.packageName}/${app.activityName}"
            val fallbackKey = "${app.packageName}/"
            val resolved = when {
                componentKey in compatiblePackages -> componentKey
                app.activityName.isNotEmpty() && compatiblePackages.any { it.startsWith(fallbackKey) } -> componentKey
                compatiblePackages.any { it.startsWith(fallbackKey) } -> {
                    compatiblePackages.filter { it.startsWith(fallbackKey) }.minByOrNull { it.length } ?: componentKey
                }
                else -> null
            }
            if (resolved != null) compatible.add(resolved) else incompatible.add(app.appName)
        }
        if (incompatible.isNotEmpty()) notifyIncompatible(incompatible)
        return compatible
    }

    fun contentDescription(mimeType: String?, text: String?, uris: List<Uri>?): String {
        val countStr = if (uris != null && uris.size > 1) " (${uris.size})" else ""
        return when {
            mimeType?.startsWith("image/") == true -> activity.getString(R.string.history_content_photo, countStr)
            mimeType?.startsWith("video/") == true -> activity.getString(R.string.history_content_video, countStr)
            text != null && uris.isNullOrEmpty() -> {
                if (text.startsWith("http")) {
                    activity.getString(R.string.history_content_link)
                } else {
                    activity.getString(R.string.history_content_text)
                }
            }
            else -> activity.getString(R.string.history_content_media, countStr)
        }
    }

    private fun notifyIncompatible(names: List<String>) {
        val joined = names.joinToString(", ")
        if (!viewModel.notificationsEnabled) {
            Toast.makeText(
                activity,
                activity.getString(R.string.notif_incompatible_big, joined),
                Toast.LENGTH_LONG,
            ).show()
            return
        }
        val manager = activity.getSystemService(NotificationManager::class.java)
        val channelId = "incompatible_apps_channel"
        val channelName = activity.getString(R.string.notif_channel_compat_name)
        manager.createNotificationChannel(
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT),
        )
        val bigText = activity.getString(R.string.notif_incompatible_big, joined)
        manager.notify(
            2,
            NotificationCompat.Builder(activity, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(activity.getString(R.string.notif_incompatible_title))
                .setContentText(activity.getString(R.string.notif_incompatible_summary))
                .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build(),
        )
    }
}
