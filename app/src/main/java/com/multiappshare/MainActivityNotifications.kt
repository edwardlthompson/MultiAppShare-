package com.multiappshare

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.multiappshare.domain.NotificationPermissionPolicy

internal class MainActivityNotifications(
    private val activity: ComponentActivity,
    private val viewModel: MainViewModel,
    private val launcher: ActivityResultLauncher<String>,
) {
    private var promptAttempted = false

    fun syncGrantedFromSystem() {
        val granted = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            true
        } else {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        }
        viewModel.onNotificationsPermissionResult(granted)
    }

    fun promptForShareSession() {
        syncGrantedFromSystem()
        if (promptAttempted) return
        if (!NotificationPermissionPolicy.shouldPromptForShare(
                Build.VERSION.SDK_INT,
                viewModel.notificationsEnabled,
            )
        ) {
            return
        }
        promptAttempted = true
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}
