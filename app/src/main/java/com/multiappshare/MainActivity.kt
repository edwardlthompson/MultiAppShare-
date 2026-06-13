package com.multiappshare

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.multiappshare.model.AppGroup
import com.multiappshare.model.HistoryItem
import com.multiappshare.ui.theme.MultiAppShareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val exportLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        if (uri != null) {
            val p = pendingExportPassphrase
            if (p != null) {
                viewModel.exportGroupsToUri(uri, p)
            }
        }
        pendingExportPassphrase?.fill('\u0000')
        pendingExportPassphrase = null
    }

    private val importLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { viewModel.importGroupsFromUri(it) }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted -> viewModel.onNotificationsPermissionResult(granted) }

    private var pendingExportPassphrase: CharArray? = null
    private val showExportPassphraseDialog = mutableStateOf(false)

    private val shareFailedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action != SharingService.ACTION_SHARE_FAILED) return
            val session = viewModel.shareSession
            val packages = session.appPackages ?: return
            val next = session.currentIndex + 1
            if (next < packages.size) {
                viewModel.updateShareSession { copy(currentIndex = next) }
                shareStep(session.uris, session.text, session.mimeType ?: "*/*", packages, next)
            } else {
                viewModel.updateShareSession { copy(sharingStarted = false) }
                stopSharingService()
                Toast.makeText(this@MainActivity, getString(R.string.toast_sharing_complete), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        handleIntent(intent)

        setContent {
            MultiAppShareTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    MainScreen(
                        viewModel = viewModel,
                        onExport = { showExportPassphraseDialog.value = true },
                        onImport = { importLauncher.launch(arrayOf("application/json", "application/octet-stream", "*/*")) },
                        onStartSharing = { group, vm -> startSharingForGroup(group, vm) },
                        onReplayShareStep = { replayShareStep() },
                        onPreviousShareStep = { previousShareStep() },
                        onNextStep = { nextShareStep() },
                        packageManager = packageManager,
                    )
                    if (showExportPassphraseDialog.value) {
                        BackupExportPassphraseDialog(
                            onDismiss = { showExportPassphraseDialog.value = false },
                            onConfirmed = { chars ->
                                showExportPassphraseDialog.value = false
                                pendingExportPassphrase = chars
                                exportLauncher.launch("multiappshare-groups.json")
                            },
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(SharingService.ACTION_SHARE_FAILED)
        ContextCompat.registerReceiver(
            this,
            shareFailedReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(shareFailedReceiver)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return

        when {
            intent.action == Intent.ACTION_SEND || intent.action == Intent.ACTION_SEND_MULTIPLE -> {
                clearSessionShareState()
                val isMultiple = intent.action == Intent.ACTION_SEND_MULTIPLE
                val uris: List<Uri>? = if (isMultiple) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                    }
                } else {
                    val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri
                    }
                    if (uri != null) listOf(uri) else null
                }
                val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                val mime = intent.type ?: "*/*"
                viewModel.updateShareSession {
                    copy(uris = uris, text = text, mimeType = mime, sharingStarted = false)
                }
            }
            intent.action == Intent.ACTION_VIEW && intent.data?.scheme == DeeplinkContract.SCHEME -> {
                applyDeepLink(intent.data!!)
            }
            !intent.getStringExtra("GROUP_NAME").isNullOrBlank() -> {
                viewModel.expandGroupByNameIfPresent(intent.getStringExtra("GROUP_NAME")!!)
                clearSessionShareState()
            }
        }
    }

    private fun applyDeepLink(uri: Uri) {
        clearSessionShareState()
        when (uri.host) {
            DeeplinkContract.HOST_OPEN -> { /* normal entry */ }
            DeeplinkContract.HOST_GROUP -> {
                val raw = uri.getQueryParameter(DeeplinkContract.QUERY_GROUP_NAME)?.trim().orEmpty()
                if (raw.isNotEmpty()) {
                    viewModel.expandGroupByNameIfPresent(raw)
                }
            }
        }
    }

    private fun clearSessionShareState() {
        viewModel.clearShareSession()
        stopSharingService()
    }

    private fun startSharingForGroup(group: AppGroup, viewModel: MainViewModel) {
        val session = viewModel.shareSession
        val mime = session.mimeType ?: "*/*"
        val compatiblePackages = handleIncompatibleApps(session.uris, mime, group, viewModel)
        val contentDesc = getContentDescription(mime, session.text, session.uris)

        if (compatiblePackages.isEmpty()) {
            viewModel.addHistoryItem(
                HistoryItem(
                    timestamp = System.currentTimeMillis(),
                    groupName = group.name,
                    contentDescription = contentDesc,
                    status = getString(R.string.history_failed_no_compatible),
                    isError = true,
                ),
            )
            Toast.makeText(this, getString(R.string.toast_no_apps_for_group, group.name), Toast.LENGTH_LONG).show()
        } else {
            viewModel.updateShareSession {
                copy(appPackages = compatiblePackages, currentIndex = 0, sharingStarted = true)
            }
            shareStep(session.uris, session.text, mime, compatiblePackages, 0)
            viewModel.incrementGroupUsage(group)
            viewModel.addHistoryItem(
                HistoryItem(
                    timestamp = System.currentTimeMillis(),
                    groupName = group.name,
                    contentDescription = contentDesc,
                    status = getString(R.string.history_started_sharing_n, compatiblePackages.size),
                ),
            )
        }
    }

    private fun replayShareStep() {
        val session = viewModel.shareSession
        val packages = session.appPackages ?: return
        shareStep(session.uris, session.text, session.mimeType ?: "*/*", packages, session.currentIndex)
    }

    private fun previousShareStep() {
        val session = viewModel.shareSession
        val packages = session.appPackages ?: return
        if (session.currentIndex > 0) {
            val prev = session.currentIndex - 1
            viewModel.updateShareSession { copy(currentIndex = prev) }
            shareStep(session.uris, session.text, session.mimeType ?: "*/*", packages, prev)
        }
    }

    private fun nextShareStep() {
        val session = viewModel.shareSession
        val packages = session.appPackages ?: return
        val next = session.currentIndex + 1
        if (next < packages.size) {
            viewModel.updateShareSession { copy(currentIndex = next) }
            shareStep(session.uris, session.text, session.mimeType ?: "*/*", packages, next)
        } else {
            viewModel.updateShareSession { copy(sharingStarted = false) }
            stopSharingService()
            Toast.makeText(this, getString(R.string.toast_sharing_complete), Toast.LENGTH_SHORT).show()
        }
    }

    private var clipData: ClipData? = null

    private fun shareStep(uris: List<Uri>?, text: String?, mime: String, components: List<String>, index: Int) {
        clipData = null
        val serviceIntent = Intent(this, SharingService::class.java).apply {
            action = SharingService.ACTION_START_SHARING
            type = mime
            if (uris != null) putParcelableArrayListExtra(SharingService.EXTRA_IMAGE_URIS, ArrayList(uris))
            putExtra(Intent.EXTRA_TEXT, text)
            putStringArrayListExtra(SharingService.EXTRA_APP_COMPONENTS, ArrayList(components))
            putExtra(SharingService.EXTRA_CURRENT_INDEX, index)
            if (uris != null) {
                for (uri in uris) {
                    val clipDataItem = ClipData.Item(uri)
                    if (clipData == null) {
                        clipData = ClipData(null, arrayOf(mime), clipDataItem)
                    } else {
                        clipData?.addItem(clipDataItem)
                    }
                }
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun stopSharingService() {
        val serviceIntent = Intent(this, SharingService::class.java).apply {
            action = SharingService.ACTION_STOP
        }
        startService(serviceIntent)
        stopService(serviceIntent)
    }

    private fun handleIncompatibleApps(uris: List<Uri>?, mime: String, group: AppGroup, viewModel: MainViewModel): List<String> {
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

            if (resolved != null) {
                compatible.add(resolved)
            } else {
                incompatible.add(app.appName)
            }
        }

        if (incompatible.isNotEmpty()) {
            if (viewModel.notificationsEnabled) {
                showIncompatibleNotification(incompatible)
            } else {
                val appList = incompatible.joinToString(", ")
                Toast.makeText(
                    this,
                    getString(R.string.notif_incompatible_big, appList),
                    Toast.LENGTH_LONG,
                ).show()
            }
        }

        return compatible
    }

    private fun showIncompatibleNotification(incompatibleAppNames: List<String>) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channelId = "incompatible_apps_channel"
        val channel = NotificationChannel(channelId, getString(R.string.notif_channel_compat_name), NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val appList = incompatibleAppNames.joinToString(", ")
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.notif_incompatible_title))
            .setContentText(getString(R.string.notif_incompatible_summary))
            .setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.notif_incompatible_big, appList)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(2, notification)
    }

    private fun getContentDescription(mimeType: String?, text: String?, uris: List<Uri>?): String {
        val countStr = if (uris != null && uris.size > 1) " (${uris.size})" else ""
        return when {
            mimeType?.startsWith("image/") == true -> getString(R.string.history_content_photo, countStr)
            mimeType?.startsWith("video/") == true -> getString(R.string.history_content_video, countStr)
            text != null && uris.isNullOrEmpty() ->
                if (text.startsWith("http")) getString(R.string.history_content_link) else getString(R.string.history_content_text)
            else -> getString(R.string.history_content_media, countStr)
        }
    }
}
