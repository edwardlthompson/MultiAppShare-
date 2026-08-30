package com.multiappshare.ui.sharing

import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.multiappshare.R
import com.multiappshare.resolveShareTargetLabel

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
    packageManager: PackageManager,
    onReplayCurrentStep: () -> Unit = {},
    onPreviousStep: () -> Unit = {},
    onNextStep: () -> Unit,
    onSkipThisApp: () -> Unit = {},
    onFinishEarly: () -> Unit = {},
    onTogglePause: () -> Unit = {},
) {
    val haptic = LocalHapticFeedback.current
    val currentKey = appComponents.getOrNull(currentIndex).orEmpty()
    val currentLabel = remember(currentIndex, currentKey, appComponents) {
        resolveShareTargetLabel(packageManager, currentKey)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val contentNoun = when {
            mimeType?.startsWith("image/") == true ->
                if (uris != null && uris.size > 1) stringResource(R.string.sharing_content_photos_n, uris.size)
                else stringResource(R.string.sharing_content_photo)
            mimeType?.startsWith("video/") == true ->
                if (uris != null && uris.size > 1) stringResource(R.string.sharing_content_videos_n, uris.size)
                else stringResource(R.string.sharing_content_video)
            text != null && uris.isNullOrEmpty() -> stringResource(R.string.sharing_content_text)
            else ->
                if (uris != null && uris.size > 1) stringResource(R.string.sharing_content_media_n, uris.size)
                else stringResource(R.string.sharing_content_media)
        }
        Text(
            stringResource(R.string.sharing_headline_format, contentNoun),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(R.string.sharing_step_format, currentIndex + 1, totalApps),
            style = MaterialTheme.typography.titleMedium,
        )
        if (currentLabel.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                stringResource(R.string.sharing_next_app_format, currentLabel),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            stringResource(R.string.sharing_preview_hint),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            stringResource(R.string.sharing_return_instruction),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
        if (lastShareFailed && currentLabel.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.sharing_open_failed, currentLabel),
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(
                onClick = onReplayCurrentStep,
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 48.dp),
            ) {
                Text(
                    stringResource(if (lastShareFailed) R.string.sharing_retry else R.string.sharing_replay_current),
                    textAlign = TextAlign.Center,
                )
            }
            OutlinedButton(
                onClick = onPreviousStep,
                enabled = currentIndex > 0,
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 48.dp),
            ) {
                Text(stringResource(R.string.sharing_previous), textAlign = TextAlign.Center)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = onTogglePause,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp),
        ) {
            Text(
                stringResource(if (paused) R.string.sharing_resume else R.string.sharing_pause),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (currentIndex + 1 >= totalApps) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
                onNextStep()
            },
            enabled = !paused,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp),
        ) {
            Text(
                if (currentIndex + 1 < totalApps) {
                    stringResource(R.string.sharing_button_next)
                } else {
                    stringResource(R.string.sharing_button_finish)
                },
            )
        }
        if (currentIndex + 1 < totalApps) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onSkipThisApp,
                enabled = !paused,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp),
            ) {
                Text(stringResource(R.string.sharing_button_skip_app))
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onFinishEarly,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp),
            ) {
                Text(stringResource(R.string.sharing_button_finish_early))
            }
        }
    }
}
