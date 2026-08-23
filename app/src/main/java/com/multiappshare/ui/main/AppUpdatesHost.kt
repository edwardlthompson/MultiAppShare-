package com.multiappshare.ui.main

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.multiappshare.BuildConfig
import com.multiappshare.R
import com.multiappshare.ui.dashboard.DashboardDonateNudgeDialog
import com.multiappshare.ui.dashboard.DashboardUpdateDialog
import com.multiappshare.ui.dashboard.DonateNudgeLabels
import com.multiappshare.ui.dashboard.UpdateDialogLabels
import com.multiappshare.updates.AppUpdates
import com.multiappshare.updates.LaunchPrompt
import com.multiappshare.updates.ProductUpdate
import com.multiappshare.updates.UpdatePrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
internal fun AppUpdatesHost(enabled: Boolean) {
    if (!enabled || BuildConfig.DEBUG) return
    val context = LocalContext.current
    var prompt by remember { mutableStateOf<LaunchPrompt?>(null) }
    LaunchedEffect(BuildConfig.VERSION_NAME) {
        prompt = withContext(Dispatchers.IO) {
            AppUpdates.evaluate(context, BuildConfig.VERSION_NAME)
        }
    }
    when (val current = prompt) {
        is LaunchPrompt.Donate -> DashboardDonateNudgeDialog(
            labels = DonateNudgeLabels(
                title = stringResource(R.string.donate_nudge_title),
                body = stringResource(R.string.donate_nudge_body),
                donate = stringResource(R.string.about_donate),
                notNow = stringResource(R.string.donate_not_now),
            ),
            onDonate = {
                UpdatePrefs(context).markVersionSeen(BuildConfig.VERSION_NAME)
                openUrl(context, ProductUpdate.VENMO_URL)
                prompt = null
            },
            onDismiss = {
                UpdatePrefs(context).markVersionSeen(BuildConfig.VERSION_NAME)
                prompt = null
            },
        )
        is LaunchPrompt.Update -> DashboardUpdateDialog(
            labels = UpdateDialogLabels(
                title = stringResource(R.string.update_available_title),
                body = stringResource(R.string.update_available_body, current.version),
                install = stringResource(R.string.update_install),
                later = stringResource(R.string.update_later),
            ),
            onInstall = {
                UpdatePrefs(context).markChecked(System.currentTimeMillis(), current.version)
                openUrl(context, current.url)
                prompt = null
            },
            onDismiss = {
                UpdatePrefs(context).markChecked(System.currentTimeMillis(), current.version)
                prompt = null
            },
        )
        null -> Unit
    }
}

internal fun openUrl(context: android.content.Context, url: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    } catch (_: ActivityNotFoundException) {
        // Stay silent — donate/update must never block the app.
    }
}
