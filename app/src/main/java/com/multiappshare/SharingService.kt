package com.multiappshare

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ClipData
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class SharingService : Service() {

    private lateinit var notificationManager: NotificationManager

    companion object {
        const val EXTRA_IMAGE_URI = "com.multiappshare.EXTRA_IMAGE_URI"
        const val EXTRA_APP_PACKAGES = "com.multiappshare.EXTRA_APP_PACKAGES"
        const val EXTRA_CURRENT_INDEX = "com.multiappshare.EXTRA_CURRENT_INDEX"
        const val ACTION_START_SHARING = "com.multiappshare.ACTION_START_SHARING"
        const val ACTION_NEXT = "com.multiappshare.ACTION_NEXT"
        private const val ACTION_STOP = "com.multiappshare.ACTION_STOP"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "sharing_service_channel"
        private const val REQUEST_CODE_STOP = 0
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        if (intent == null || intent.action == ACTION_STOP) {
            stopServiceForeground()
            stopSelf()
            return START_NOT_STICKY
        }

        val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_IMAGE_URI, Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_IMAGE_URI) as? Uri
        }
        val text = intent.getStringExtra(Intent.EXTRA_TEXT)
        val mimeType = intent.type ?: "*/*"

        val appPackages = intent.getStringArrayListExtra(EXTRA_APP_PACKAGES)
        val currentIndex = intent.getIntExtra(EXTRA_CURRENT_INDEX, 0)

        if (appPackages.isNullOrEmpty() || currentIndex >= appPackages.size) {
            stopServiceForeground()
            stopSelf()
            return START_NOT_STICKY
        }

        val notification = createNotification(appPackages, currentIndex).build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        shareWithApp(uri, text, mimeType, appPackages[currentIndex])

        return START_NOT_STICKY
    }

    private fun stopServiceForeground() {
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun createNotification(appPackages: List<String>, currentIndex: Int): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Sharing in progress")
            .setContentText("Sharing to app ${currentIndex + 1} of ${appPackages.size}")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .addAction(0, "Stop", createStopPendingIntent())

        if (currentIndex + 1 < appPackages.size) {
            val nextPendingIntent = createNextPendingIntent(appPackages, currentIndex + 1)
            builder.addAction(0, "Next App", nextPendingIntent)
            builder.setContentIntent(nextPendingIntent)
        }

        return builder
    }

    private fun shareWithApp(uri: Uri?, text: String?, mimeType: String, packageName: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            if (uri != null) {
                putExtra(Intent.EXTRA_STREAM, uri)
                clipData = ClipData.newUri(contentResolver, "Content", uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            if (text != null) {
                putExtra(Intent.EXTRA_TEXT, text)
            }
            setPackage(packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        try {
            startActivity(shareIntent)
        } catch (_: Exception) {
            // Silently handle exceptions for missing packages
        }
    }

    private fun createNextPendingIntent(appPackages: List<String>, nextIndex: Int): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            action = ACTION_NEXT
            putExtra(EXTRA_APP_PACKAGES, ArrayList(appPackages))
            putExtra(EXTRA_CURRENT_INDEX, nextIndex)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        return PendingIntent.getActivity(this, nextIndex, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private fun createStopPendingIntent(): PendingIntent {
        val intent = Intent(this, SharingService::class.java).apply {
            action = ACTION_STOP
        }
        return PendingIntent.getService(this, REQUEST_CODE_STOP, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, "Sharing Service", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
