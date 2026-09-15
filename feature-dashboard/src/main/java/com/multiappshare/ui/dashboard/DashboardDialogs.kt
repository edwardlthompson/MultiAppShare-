package com.multiappshare.ui.dashboard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.multiappshare.model.HistoryItem

data class HistoryDialogLabels(
    val title: String,
    val empty: String,
    val emptyCta: String = "",
    val sharedPrefix: String,
    val close: String,
    val reshare: String = "",
    val reshareRow: String = "",
    val error: String = "",
)

@Composable
fun DashboardHistoryDialog(
    history: List<HistoryItem>,
    labels: HistoryDialogLabels,
    onDismiss: () -> Unit,
    onReshare: (() -> Unit)? = null,
    onReshareItem: ((HistoryItem) -> Unit)? = null,
    errorText: String? = null,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HistoryScreen(
                history = history,
                labels = labels,
                onDismiss = onDismiss,
                onReshare = onReshare,
                onReshareItem = onReshareItem,
                errorText = errorText,
            )
        }
    }
}

data class AboutDialogLabels(
    val title: String,
    val versionLine: String,
    val tagline: String,
    val privacy: String,
    val developerContact: String,
    val telegramLabel: String,
    val supportDeveloper: String,
    val venmoLink: String,
    val changelog: String,
    val feedback: String,
    val ok: String,
    val noAppForLink: String,
    val privacyOpen: String,
    val licenses: String,
    val shortcuts: String = "",
)

@Composable
fun DashboardAboutDialog(
    labels: AboutDialogLabels,
    onDismiss: () -> Unit,
    onFeedback: () -> Unit = {},
    onChangelog: () -> Unit = {},
    onPrivacy: () -> Unit = {},
    onLicenses: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            AboutScreen(
                labels = labels,
                onDismiss = onDismiss,
                onFeedback = onFeedback,
                onChangelog = onChangelog,
                onPrivacy = onPrivacy,
                onLicenses = onLicenses,
            )
        }
    }
}
