package com.multiappshare.ui.sharing

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.multiappshare.NotificationPermissionIntents
import com.multiappshare.R
import com.multiappshare.domain.NotificationPermissionPolicy
import com.multiappshare.ui.launch.rememberStartIntent

@Composable
fun NotificationPermissionBanner(permissionGranted: Boolean, inShareSession: Boolean) {
    val context = LocalContext.current
    val startIntent = rememberStartIntent()
    if (!NotificationPermissionPolicy.shouldShowSettingsBanner(
            sdkInt = Build.VERSION.SDK_INT,
            permissionGranted = permissionGranted,
            inShareSession = inShareSession,
        )
    ) {
        return
    }
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                stringResource(R.string.notification_permission_banner),
                style = MaterialTheme.typography.bodyMedium,
            )
            OutlinedButton(
                onClick = { startIntent(NotificationPermissionIntents.appSettings(context)) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.notification_permission_open_settings))
            }
        }
    }
}
