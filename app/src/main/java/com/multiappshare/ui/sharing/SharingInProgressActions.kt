package com.multiappshare.ui.sharing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.multiappshare.R
import com.multiappshare.sharehaptics.ShareHaptics
import com.multiappshare.shareoverlay.ShareOverlayActions

@Composable
internal fun SharingInProgressActions(
    currentIndex: Int,
    totalApps: Int,
    lastShareFailed: Boolean,
    paused: Boolean,
    hapticsEnabled: Boolean,
    remainingAfterCurrent: Int,
    onReplayCurrentStep: () -> Unit,
    onPreviousStep: () -> Unit,
    onNextStep: () -> Unit,
    onSkipThisApp: () -> Unit,
    onFinishEarly: () -> Unit,
    onTogglePause: () -> Unit,
    onTryLater: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val finish = ShareOverlayActions.primaryIsFinish(currentIndex, totalApps)
    val showSkip = ShareOverlayActions.showSkipActions(currentIndex, totalApps)
    val showTryLater = ShareOverlayActions.showTryLater(lastShareFailed, remainingAfterCurrent)
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedButton(
            onClick = onReplayCurrentStep,
            enabled = !paused,
            modifier = Modifier.weight(1f).defaultMinSize(minHeight = 48.dp),
        ) {
            Text(
                stringResource(if (lastShareFailed) R.string.sharing_retry else R.string.sharing_replay_current),
                textAlign = TextAlign.Center,
            )
        }
        OutlinedButton(
            onClick = onPreviousStep,
            enabled = currentIndex > 0,
            modifier = Modifier.weight(1f).defaultMinSize(minHeight = 48.dp),
        ) {
            Text(stringResource(R.string.sharing_previous), textAlign = TextAlign.Center)
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Button(
        onClick = {
            if (finish && ShareHaptics.shouldPerform(hapticsEnabled)) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            onNextStep()
        },
        enabled = !paused,
        modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 48.dp),
    ) {
        Text(
            stringResource(if (finish) R.string.sharing_button_finish else R.string.sharing_button_next),
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
    SharingMoreMenu(
        paused = paused,
        showSkip = showSkip,
        showTryLater = showTryLater,
        onTogglePause = onTogglePause,
        onSkipThisApp = onSkipThisApp,
        onFinishEarly = onFinishEarly,
        onTryLater = onTryLater,
    )
}

@Composable
private fun SharingMoreMenu(
    paused: Boolean,
    showSkip: Boolean,
    showTryLater: Boolean,
    onTogglePause: () -> Unit,
    onSkipThisApp: () -> Unit,
    onFinishEarly: () -> Unit,
    onTryLater: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.cd_more_options))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = {
                    Text(stringResource(if (paused) R.string.sharing_resume else R.string.sharing_pause))
                },
                onClick = {
                    onTogglePause()
                    expanded = false
                },
            )
            if (showTryLater) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.sharing_try_later)) },
                    enabled = !paused,
                    onClick = {
                        onTryLater()
                        expanded = false
                    },
                )
            }
            if (showSkip) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.sharing_button_skip_app)) },
                    enabled = !paused,
                    onClick = {
                        onSkipThisApp()
                        expanded = false
                    },
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.sharing_button_finish_early)) },
                    onClick = {
                        onFinishEarly()
                        expanded = false
                    },
                )
            }
        }
    }
}
