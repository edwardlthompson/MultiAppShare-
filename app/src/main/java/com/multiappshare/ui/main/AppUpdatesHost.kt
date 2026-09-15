package com.multiappshare.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.multiappshare.BuildConfig
import com.multiappshare.R
import com.multiappshare.core.ui.ExternalIntent
import com.multiappshare.ui.dashboard.DashboardDonateNudgeDialog
import com.multiappshare.ui.dashboard.DashboardUpdateDialog
import com.multiappshare.ui.dashboard.DonateNudgeLabels
import com.multiappshare.ui.dashboard.UpdateDialogLabels
import com.multiappshare.ui.launch.rememberOpenUrl
import com.multiappshare.updates.AppUpdates
import com.multiappshare.updates.LaunchPrompt
import com.multiappshare.updates.ProductUpdate
import com.multiappshare.updates.UpdatePrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
internal fun AppUpdatesHost(enabled: Boolean) {
    val context = LocalContext.current
    val openUrl = rememberOpenUrl()
    var prompt by remember { mutableStateOf<LaunchPrompt?>(null) }
    var retryNonce by remember { mutableIntStateOf(0) }
    LaunchedEffect(enabled, BuildConfig.VERSION_NAME, retryNonce) {
        if (!enabled || BuildConfig.DEBUG) {
            prompt = null
            return@LaunchedEffect
        }
        prompt = withContext(Dispatchers.IO) {
            AppUpdates.evaluate(context, BuildConfig.VERSION_NAME)
        }
    }
    if (!enabled || BuildConfig.DEBUG) return
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
                openUrl(ProductUpdate.VENMO_URL)
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
                body = stringResource(
                    if (current.listingOnly) {
                        R.string.update_available_body_fdroid
                    } else {
                        R.string.update_available_body
                    },
                    current.version,
                ),
                install = stringResource(
                    if (current.listingOnly) R.string.update_open_fdroid else R.string.update_install,
                ),
                later = stringResource(R.string.update_later),
            ),
            onInstall = {
                UpdatePrefs(context).markChecked(System.currentTimeMillis(), current.version)
                openUrl(current.url)
                prompt = null
            },
            onDismiss = {
                UpdatePrefs(context).markChecked(System.currentTimeMillis(), current.version)
                prompt = null
            },
        )
        is LaunchPrompt.CheckFailed -> DashboardUpdateDialog(
            labels = UpdateDialogLabels(
                title = stringResource(R.string.update_check_failed_title),
                body = stringResource(R.string.update_check_failed_body),
                install = stringResource(R.string.update_retry),
                later = stringResource(R.string.update_later),
            ),
            onInstall = {
                prompt = null
                retryNonce += 1
            },
            onDismiss = {
                UpdatePrefs(context).markChecked(System.currentTimeMillis())
                prompt = null
            },
        )
        null -> Unit
    }
}

internal fun openUrl(context: android.content.Context, url: String): Boolean =
    ExternalIntent.tryViewUrl(context, url)
