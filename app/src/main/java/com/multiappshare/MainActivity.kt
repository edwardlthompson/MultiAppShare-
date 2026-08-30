package com.multiappshare

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.multiappshare.display.enableFastestSameResolutionMode
import com.multiappshare.ui.theme.MultiAppShareTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var sharing: MainActivitySharing

    private val exportLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/json"),
    ) { uri ->
        if (uri != null) {
            val p = pendingExportPassphrase
            if (p != null) viewModel.exportGroupsToUri(uri, p)
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
            if (intent?.action == SharingService.ACTION_SHARE_FAILED) sharing.onShareFailedAdvance()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        window.decorView.post { window.enableFastestSameResolutionMode() } // default until prefs load
        sharing = MainActivitySharing(this, viewModel, MainActivityShareStep(this, viewModel))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        sharing.handleIntent(intent, restoreIfColdStart = savedInstanceState == null)

        setContent {
            val darkPref by viewModel.darkTheme.collectAsState(initial = null)
            val highRefresh by viewModel.highRefreshEnabled.collectAsState(initial = true)
            val systemDark = isSystemInDarkTheme()
            LaunchedEffect(highRefresh) {
                if (highRefresh) {
                    window.enableFastestSameResolutionMode()
                } else {
                    val lp = window.attributes
                    lp.preferredDisplayModeId = 0
                    window.attributes = lp
                }
            }
            MultiAppShareTheme(darkTheme = darkPref ?: systemDark) {
                Box(modifier = Modifier.fillMaxSize()) {
                    MainScreen(
                        viewModel = viewModel,
                        onExport = { showExportPassphraseDialog.value = true },
                        onImport = {
                            importLauncher.launch(
                                arrayOf("application/json", "application/octet-stream", "*/*"),
                            )
                        },
                        onStartSharing = { group, _ -> sharing.startSharingForGroup(group) },
                        onReplayShareStep = { sharing.replayShareStep() },
                        onPreviousShareStep = { sharing.previousShareStep() },
                        onNextStep = { sharing.nextShareStep() },
                        onSkipThisApp = { sharing.skipThisApp() },
                        onFinishEarly = { sharing.finishEarly() },
                        onCancelShare = { sharing.cancelShareOverlay() },
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
        ContextCompat.registerReceiver(
            this,
            shareFailedReceiver,
            IntentFilter(SharingService.ACTION_SHARE_FAILED),
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
        sharing.handleIntent(intent, restoreIfColdStart = false)
    }
}
