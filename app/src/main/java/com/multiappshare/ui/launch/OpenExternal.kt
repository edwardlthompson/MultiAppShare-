package com.multiappshare.ui.launch

import android.content.Context
import android.content.Intent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.multiappshare.R
import com.multiappshare.core.ui.ExternalIntent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState?> { null }

@Composable
fun rememberOpenUrl(): (String) -> Boolean {
    val context = LocalContext.current
    val notify = rememberLaunchFailedNotify()
    return remember(context, notify) { OpenUrlAction(context, notify) }
}

@Composable
fun rememberStartIntent(): (Intent) -> Boolean {
    val context = LocalContext.current
    val notify = rememberLaunchFailedNotify()
    return remember(context, notify) { StartIntentAction(context, notify) }
}

private class OpenUrlAction(
    private val context: Context,
    private val notify: () -> Unit,
) : (String) -> Boolean {
    override fun invoke(url: String): Boolean {
        val ok = ExternalIntent.tryViewUrl(context, url)
        if (!ok) notify()
        return ok
    }
}

private class StartIntentAction(
    private val context: Context,
    private val notify: () -> Unit,
) : (Intent) -> Boolean {
    override fun invoke(intent: Intent): Boolean {
        val ok = ExternalIntent.tryStart(context, intent)
        if (!ok) notify()
        return ok
    }
}

@Composable
private fun rememberLaunchFailedNotify(): () -> Unit {
    val host = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()
    val message = stringResource(R.string.error_no_app_for_link)
    return remember(host, message) { NotifyLaunchFailed(host, scope, message) }
}

private class NotifyLaunchFailed(
    private val host: SnackbarHostState?,
    private val scope: CoroutineScope,
    private val message: String,
) : () -> Unit {
    override fun invoke() {
        if (host != null) {
            scope.launch { host.showSnackbar(message) }
        }
    }
}
