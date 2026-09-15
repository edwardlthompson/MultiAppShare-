package com.multiappshare.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.multiappshare.crashcapture.CrashStore
import com.multiappshare.domain.FirstRunDialogQueue
import com.multiappshare.ui.crashcapture.CrashReviewDialog
import com.multiappshare.ui.feedback.FeedbackDialog

@Composable
internal fun MainScreenGoldenPathHost(
    showFeedback: Boolean,
    onShowFeedback: (Boolean) -> Unit,
    onboardingVisible: Boolean,
    onCrashReviewVisible: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    var crashText by remember { mutableStateOf<String?>(null) }
    var showCrash by remember { mutableStateOf(false) }
    var attachCrash by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        val pending = CrashStore.readPending(context)
        crashText = pending
        showCrash = pending != null
    }
    val crashVisible = showCrash && !showFeedback && FirstRunDialogQueue.allowCrashReview(onboardingVisible)
    LaunchedEffect(crashVisible) { onCrashReviewVisible(crashVisible) }
    if (crashVisible) {
        CrashReviewDialog(
            onDismiss = {
                CrashStore.clear(context)
                crashText = null
                attachCrash = false
                showCrash = false
            },
            onSendFeedback = {
                attachCrash = true
                showCrash = false
                onShowFeedback(true)
            },
        )
    }
    if (showFeedback) {
        FeedbackDialog(
            crashText = crashText.takeIf { attachCrash },
            onDismiss = {
                CrashStore.clear(context)
                crashText = null
                attachCrash = false
                onShowFeedback(false)
            },
        )
    }
}
