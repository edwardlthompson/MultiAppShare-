package com.multiappshare.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.multiappshare.ShareSessionState

@Composable
fun ShareSessionBackHandler(
    shareSession: ShareSessionState,
    onFinishEarly: () -> Unit,
    onCancelShare: () -> Unit,
) {
    BackHandler(enabled = shareSession.inShareMode) {
        if (shareSession.sharingStarted) onFinishEarly() else onCancelShare()
    }
}
