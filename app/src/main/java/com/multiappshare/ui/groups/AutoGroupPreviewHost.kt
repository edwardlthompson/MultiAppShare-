package com.multiappshare.ui.groups

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.multiappshare.MainUiState
import com.multiappshare.MainViewModel
import com.multiappshare.domain.AutoGroupDryRun

val LocalRequestAutoGroupPreview = compositionLocalOf<(Boolean, Boolean) -> Unit> { { _, _ -> } }

@Composable
fun AutoGroupPreviewHost(
    uiState: MainUiState,
    viewModel: MainViewModel,
    content: @Composable () -> Unit,
) {
    var request by remember { mutableStateOf<Pair<Boolean, Boolean>?>(null) }
    val success = uiState as? MainUiState.Success
    CompositionLocalProvider(
        LocalRequestAutoGroupPreview provides { append, dismissOnboarding ->
            request = append to dismissOnboarding
        },
    ) {
        content()
        val pending = request
        if (pending != null && success != null) {
            val append = pending.first
            val rows = AutoGroupDryRun.summarize(
                AutoGroupDryRun.preview(success.allApps, success.groups, append),
            )
            AutoGroupPreviewDialog(
                append = append,
                rows = rows,
                onDismiss = { request = null },
                onConfirm = {
                    viewModel.autoGroupApps(success.allApps, append = append)
                    if (pending.second) viewModel.setOnboardingDismissed()
                    request = null
                },
            )
        }
    }
}
