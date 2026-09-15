package com.multiappshare.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.multiappshare.BuildConfig
import com.multiappshare.R
import com.multiappshare.domain.WhatsNewHelper
import com.multiappshare.ui.launch.rememberOpenUrl
import com.multiappshare.updates.ProductUpdate
import com.multiappshare.updates.UpdatePrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun WhatsNewDialog(
    onDismiss: () -> Unit,
    onDonate: () -> Unit = {},
) {
    val highlights = remember {
        WhatsNewHelper.getHighlightsForVersion(BuildConfig.VERSION_NAME)
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_whats_new)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                highlights.forEach { line ->
                    Text(
                        "• $line",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_ok)) }
        },
        dismissButton = {
            TextButton(onClick = onDonate) { Text(stringResource(R.string.about_donate)) }
        },
    )
}

@Composable
fun WhatsNewHost(enabled: Boolean, onVisible: (Boolean) -> Unit = {}) {
    val context = LocalContext.current
    val openUrl = rememberOpenUrl()
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(enabled, BuildConfig.VERSION_CODE) {
        if (!enabled) {
            visible = false
            onVisible(false)
            return@LaunchedEffect
        }
        visible = withContext(Dispatchers.IO) {
            val prefs = UpdatePrefs(context)
            val lastSeen = prefs.lastSeenVersionCode()
            val show = WhatsNewHelper.shouldShowWhatsNew(lastSeen, BuildConfig.VERSION_CODE)
            if (!show) prefs.markVersionCodeSeen(BuildConfig.VERSION_CODE)
            show
        }
        onVisible(visible)
    }
    if (!enabled) return
    if (visible) {
        WhatsNewDialog(
            onDismiss = {
                UpdatePrefs(context).markVersionCodeSeen(BuildConfig.VERSION_CODE)
                visible = false
                onVisible(false)
            },
            onDonate = {
                openUrl(ProductUpdate.VENMO_URL)
            },
        )
    }
}
