package com.multiappshare

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipData
import android.content.Intent
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
import androidx.compose.runtime.mutableIntStateOf
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
            val p = pendingExportPassphrase.value
            if (p != null) {
                viewModel.exportGroupsToUri(uri, p)
            }
            pendingExportPassphrase.value?.fill('\u0000')
            pendingExportPassphrase.value = null
        } else {
            pendingExportPassphrase.value?.fill('\u0000')
            pendingExportPassphrase.value = null
        }
    }
    
    private val importLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { viewModel.importGroupsFromUri(it) }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    private val currentUris = mutableStateOf<List<Uri>?>(null)
    private val currentText = mutableStateOf<String?>(null)
    private val currentMimeType = mutableStateOf<String?>(null)

    private val showExportPassphraseDialog = mutableStateOf(false)
    private val pendingExportPassphrase = mutableStateOf<CharArray?>(null)
    
    private val appPackages = mutableStateOf<List<String>?>(null)
    private val currentIndex = mutableIntStateOf(0)
    private val isSharingStarted = mutableStateOf(false)

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
                    uris = currentUris.value,
                    text = currentText.value,
                    mimeType = currentMimeType.value,
                    sharingStarted = isSharingStarted.value,
                    currentIndex = currentIndex.intValue,
                    appPackages = appPackages.value,
                    viewModel = viewModel,
                    onExport = { showExportPassphraseDialog.value = true },
                    onImport = { importLauncher.launch(arrayOf("application/json", "application/octet-stream", "*/*")) },
                    onStartSharing = { group, vm ->
                        val mime = currentMimeType.value ?: "*/*"
                        val compatiblePackages = handleIncompatibleApps(currentUris.value, mime, group, viewModel)
                        val contentDesc = getContentDescription(mime, currentText.value, currentUris.value)
                        
                        if (compatiblePackages.isEmpty()) {
                            viewModel.addHistoryItem(HistoryItem(
                                timestamp = System.currentTimeMillis(),
                                groupName = group.name,
                                contentDescription = contentDesc,
                                status = getString(R.string.history_failed_no_compatible),
                                isError = true
                            ))
                            Toast.makeText(this@MainActivity, getString(R.string.toast_no_apps_for_group, group.name), Toast.LENGTH_LONG).show()
                        } else {
                            appPackages.value = compatiblePackages
                            currentIndex.intValue = 0
                            shareStep(currentUris.value, currentText.value, mime, compatiblePackages, 0)
                            isSharingStarted.value = true
                            
                            viewModel.incrementGroupUsage(group) // Frequency sorting increment
                            
                            viewModel.addHistoryItem(HistoryItem(
                                timestamp = System.currentTimeMillis(),
                                groupName = group.name,
                                contentDescription = contentDesc,
                                status = getString(R.string.history_started_sharing_n, compatiblePackages.size)
                            ))
                        }
                    },
                    onReplayShareStep = {
                        val packages = appPackages.value
                        if (packages != null) {
                            shareStep(currentUris.value, currentText.value, currentMimeType.value ?: "*/*", packages, currentIndex.intValue)
                        }
                    },
                    onPreviousShareStep = {
                        val packages = appPackages.value
                        if (packages != null && currentIndex.intValue > 0) {
                            currentIndex.intValue -= 1
                            shareStep(currentUris.value, currentText.value, currentMimeType.value ?: "*/*", packages, currentIndex.intValue)
                        }
                    },
                    onNextStep = {
                        val packages = appPackages.value
                        val next = currentIndex.intValue + 1
                        if (packages != null && next < packages.size) {
                            currentIndex.intValue = next
                            shareStep(currentUris.value, currentText.value, currentMimeType.value ?: "*/*", packages, next)
                        } else {
                            isSharingStarted.value = false
                            stopSharingService()
                            Toast.makeText(this@MainActivity, getString(R.string.toast_sharing_complete), Toast.LENGTH_SHORT).show()
                        }
                    },
                    packageManager = packageManager
                )
                if (showExportPassphraseDialog.value) {
                    BackupExportPassphraseDialog(
                        onDismiss = { showExportPassphraseDialog.value = false },
                        onConfirmed = { chars ->
                            showExportPassphraseDialog.value = false
                            pendingExportPassphrase.value = chars
                            exportLauncher.launch("multiappshare-groups.json")
                        }
                    )
                }
                }
            }
        }
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

                currentUris.value = uris
                currentText.value = text
                currentMimeType.value = mime
                isSharingStarted.value = false
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
        currentUris.value = null
        currentText.value = null
        currentMimeType.value = null
        isSharingStarted.value = false
        appPackages.value = null
        currentIndex.intValue = 0
        stopSharingService()
    }

    private fun shareStep(uris: List<Uri>?, text: String?, mime: String, components: List<String>, index: Int) {
        val serviceIntent = Intent(this, SharingService::class.java).apply {
            action = SharingService.ACTION_START_SHARING
            type = mime
            if (uris != null) putParcelableArrayListExtra(SharingService.EXTRA_IMAGE_URIS, ArrayList(uris))
            putExtra(Intent.EXTRA_TEXT, text)
            putStringArrayListExtra(SharingService.EXTRA_APP_COMPONENTS, ArrayList(components))
            putExtra(SharingService.EXTRA_CURRENT_INDEX, index)
            if (uris != null) {
                // Grant read permission for all URIs
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
        val serviceIntent = Intent(this, SharingService::class.java)
        stopService(serviceIntent)
    }

    private fun handleIncompatibleApps(uris: List<Uri>?, mime: String, group: AppGroup, viewModel: MainViewModel): List<String> {
        val shareAction = if (uris != null && uris.size > 1) Intent.ACTION_SEND_MULTIPLE else Intent.ACTION_SEND
        val compatiblePackages = viewModel.getCompatiblePackages(shareAction, mime)
        val compatible = mutableListOf<String>()
        val incompatible = mutableListOf<String>()

        for (app in group.apps) {
            val componentKey = "${app.packageName}/${app.activityName}"
            val fallbackKey = "${app.packageName}/" // For backward compatibility with older groups
            
            if (componentKey in compatiblePackages || compatiblePackages.any { it.startsWith(fallbackKey) }) {
                compatible.add(if (app.activityName.isNotEmpty()) componentKey else compatiblePackages.first { it.startsWith(fallbackKey) })
            } else {
                incompatible.add(app.appName)
            }
        }

        if (incompatible.isNotEmpty()) {
            showIncompatibleNotification(incompatible)
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
