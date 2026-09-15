package com.multiappshare.ui.sharing

import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multiappshare.domain.DesktopWindowingHelper
import com.multiappshare.model.AppInfo
import com.multiappshare.resolveShareTargetLabel
import com.multiappshare.shareoverlay.ShareOverlayActions

@Composable
fun SharingInProgress(
    mimeType: String?,
    text: String?,
    uris: List<Uri>?,
    currentIndex: Int,
    totalApps: Int,
    appComponents: List<String>,
    lastShareFailed: Boolean = false,
    paused: Boolean = false,
    hapticsEnabled: Boolean = true,
    packageManager: PackageManager,
    onReplayCurrentStep: () -> Unit = {},
    onPreviousStep: () -> Unit = {},
    onNextStep: () -> Unit,
    onSkipThisApp: () -> Unit = {},
    onFinishEarly: () -> Unit = {},
    onTogglePause: () -> Unit = {},
    onTryLater: () -> Unit = {},
    onCancelShare: () -> Unit = {},
) {
    val currentKey = appComponents.getOrNull(currentIndex).orEmpty()
    val currentLabel = remember(currentIndex, currentKey, appComponents) {
        resolveShareTargetLabel(packageManager, currentKey)
    }
    val heroApp = remember(currentKey, currentLabel) { appInfoFromComponentKey(currentKey, currentLabel) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .then(shareSessionKeyModifier(paused, onNextStep, onCancelShare)),
    ) {
        val maxContent = DesktopWindowingHelper.overlayMaxContentWidthDp(
            DesktopWindowingHelper.classifyWindowSize(maxWidth.value.toInt()),
        )
        Column(
            modifier = Modifier
                .widthIn(max = maxContent.dp)
                .fillMaxSize()
                .align(Alignment.Center)
                .navigationBarsPadding()
                .imePadding(),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        SharingInProgressCopy(
                            heroApp = heroApp,
                            currentLabel = currentLabel,
                            mimeType = mimeType,
                            text = text,
                            uris = uris,
                            currentIndex = currentIndex,
                            totalApps = totalApps,
                            paused = paused,
                            lastShareFailed = lastShareFailed,
                        )
                    }
                }
            }
            if (ShareOverlayActions.PRIMARY_STICKY) {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                    SharingInProgressActions(
                        currentIndex = currentIndex,
                        totalApps = totalApps,
                        lastShareFailed = lastShareFailed,
                        paused = paused,
                        hapticsEnabled = hapticsEnabled,
                        remainingAfterCurrent = totalApps - currentIndex - 1,
                        onReplayCurrentStep = onReplayCurrentStep,
                        onPreviousStep = onPreviousStep,
                        onNextStep = onNextStep,
                        onSkipThisApp = onSkipThisApp,
                        onFinishEarly = onFinishEarly,
                        onTogglePause = onTogglePause,
                        onTryLater = onTryLater,
                    )
                }
            }
        }
    }
}

private fun appInfoFromComponentKey(key: String, label: String): AppInfo? {
    val trimmed = key.trim()
    if (trimmed.isEmpty()) return null
    val parts = trimmed.split("/", limit = 2)
    return AppInfo(
        appName = label.ifEmpty { parts[0] },
        packageName = parts[0],
        activityName = parts.getOrNull(1).orEmpty(),
    )
}
