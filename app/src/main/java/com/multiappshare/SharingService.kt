package com.multiappshare

import android.app.NotificationManager
import android.app.Service
import android.content.ClipData
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import timber.log.Timber

/**
 * Service that manages the foreground notification and triggers sharing intents.
 * It is responsible for starting the sharing activity for a specific app component.
 */
class SharingService : Service() {

    private lateinit var notificationManager: NotificationManager

    companion object {
        const val EXTRA_IMAGE_URIS = "com.multiappshare.EXTRA_IMAGE_URIS"
        const val EXTRA_APP_COMPONENTS = "com.multiappshare.EXTRA_APP_COMPONENTS"
        const val EXTRA_CURRENT_INDEX = "com.multiappshare.EXTRA_CURRENT_INDEX"
        const val ACTION_START_SHARING = "com.multiappshare.ACTION_START_SHARING"
        const val ACTION_STOP = "com.multiappshare.ACTION_STOP"
        const val ACTION_SHARE_FAILED = "com.multiappshare.ACTION_SHARE_FAILED"
        const val EXTRA_FAILED_COMPONENT = "com.multiappshare.EXTRA_FAILED_COMPONENT"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        if (intent == null || intent.action == ACTION_STOP) {
            stopServiceForeground()
            stopSelf()
            return START_NOT_STICKY
        }

        val uris: ArrayList<Uri>? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(EXTRA_IMAGE_URIS, Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra(EXTRA_IMAGE_URIS)
        }
        val text = intent.getStringExtra(Intent.EXTRA_TEXT)
        val mimeType = intent.type ?: "*/*"

        val appComponents = intent.getStringArrayListExtra(EXTRA_APP_COMPONENTS)
        val currentIndex = intent.getIntExtra(EXTRA_CURRENT_INDEX, 0)

        if (appComponents.isNullOrEmpty() || currentIndex >= appComponents.size) {
            stopServiceForeground()
            stopSelf()
            return START_NOT_STICKY
        }

        val notification = SharingNotification.builder(this, appComponents, currentIndex).build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                SharingNotification.NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC,
            )
        } else {
            startForeground(SharingNotification.NOTIFICATION_ID, notification)
        }

        shareWithApp(uris, text, mimeType, appComponents[currentIndex])

        return START_NOT_STICKY
    }

    private fun stopServiceForeground() {
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun shareWithApp(uris: List<Uri>?, text: String?, mimeType: String, componentString: String) {
        val shareAction = if (uris != null && uris.size > 1) Intent.ACTION_SEND_MULTIPLE else Intent.ACTION_SEND
        val shareIntent = Intent(shareAction).apply {
            type = mimeType
            if (uris != null && uris.size == 1) {
                putExtra(Intent.EXTRA_STREAM, uris.first())
                clipData = ClipData.newUri(contentResolver, "Content", uris.first())
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else if (uris != null && uris.size > 1) {
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
                for (uri in uris) {
                    val clipDataItem = ClipData.Item(uri)
                    if (clipData == null) {
                        clipData = ClipData(null, arrayOf(mimeType), clipDataItem)
                    } else {
                        clipData?.addItem(clipDataItem)
                    }
                }
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            if (text != null) {
                putExtra(Intent.EXTRA_TEXT, text)
            }
            
            if (componentString.contains("/")) {
                val parts = componentString.split("/")
                if (parts.size == 2 && parts[1].isNotEmpty()) {
                    component = android.content.ComponentName(parts[0], parts[1])
                } else {
                    setPackage(parts[0])
                }
            } else {
                setPackage(componentString)
            }
            
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        try {
            startActivity(shareIntent)
            Timber.d("Successfully started share intent for $componentString")
        } catch (e: Exception) {
            Timber.e(e, "Exception sharing with app: $componentString")
            val label = resolveShareTargetLabel(packageManager, componentString).ifBlank { componentString }
            Toast.makeText(this, getString(R.string.toast_sharing_failed, label), Toast.LENGTH_SHORT).show()
            sendBroadcast(
                Intent(ACTION_SHARE_FAILED).apply {
                    setPackage(packageName)
                    putExtra(EXTRA_FAILED_COMPONENT, componentString)
                },
            )
        }
    }

    private fun createNotificationChannel() {
        notificationManager.createNotificationChannel(SharingNotification.channel(this))
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
