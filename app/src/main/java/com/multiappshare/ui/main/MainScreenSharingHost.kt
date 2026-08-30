package com.multiappshare.ui.main

import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.multiappshare.MainViewModel
import com.multiappshare.ShareSessionState
import com.multiappshare.sharedefer.ShareDefer
import com.multiappshare.sharehaptics.ShareHapticsViewModel
import com.multiappshare.ui.sharing.SharingInProgress

@Composable
internal fun MainScreenSharingHost(
    shareSession: ShareSessionState,
    packageManager: PackageManager,
    viewModel: MainViewModel,
    onNextStep: () -> Unit,
    onReplayShareStep: () -> Unit,
    onPreviousShareStep: () -> Unit,
    onFinishEarly: () -> Unit,
    onSkipThisApp: () -> Unit,
    onMarkSuccess: () -> Unit,
) {
    val hapticsVm: ShareHapticsViewModel = hiltViewModel()
    val hapticsOn by hapticsVm.enabled.collectAsState(initial = true)
    val packages = shareSession.appPackages
    if (packages != null) {
        SharingInProgress(
            mimeType = shareSession.mimeType,
            text = shareSession.text,
            uris = shareSession.uris,
            currentIndex = shareSession.currentIndex,
            totalApps = packages.size,
            appComponents = packages,
            lastShareFailed = shareSession.lastShareFailed,
            paused = shareSession.paused,
            hapticsEnabled = hapticsOn,
            packageManager = packageManager,
            onReplayCurrentStep = onReplayShareStep,
            onPreviousStep = onPreviousShareStep,
            onSkipThisApp = onSkipThisApp,
            onFinishEarly = onFinishEarly,
            onTogglePause = {
                viewModel.updateShareSession { copy(paused = !paused) }
            },
            onTryLater = {
                val moved = ShareDefer.moveCurrentToEnd(packages, shareSession.currentIndex)
                viewModel.updateShareSession {
                    copy(
                        appPackages = moved.packages,
                        currentIndex = moved.currentIndex,
                        lastShareFailed = false,
                    )
                }
                onReplayShareStep()
            },
            onNextStep = {
                if (shareSession.currentIndex + 1 == packages.size) {
                    onMarkSuccess()
                }
                onNextStep()
            },
        )
    }
}
