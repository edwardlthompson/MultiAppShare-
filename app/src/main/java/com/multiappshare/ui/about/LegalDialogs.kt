package com.multiappshare.ui.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.multiappshare.R
import com.multiappshare.domain.OpenSourceLicenseCatalog
import com.multiappshare.domain.PrivacyPolicyText
import com.multiappshare.ui.launch.rememberOpenUrl

@Composable
fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_privacy)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                PrivacyPolicyText.SECTIONS.forEach { (heading, body) ->
                    Text(
                        heading,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                    Text(body, style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_ok)) }
        },
    )
}

@Composable
fun OssLicensesDialog(onDismiss: () -> Unit) {
    val openUrl = rememberOpenUrl()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_licenses)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OpenSourceLicenseCatalog.LICENSES.forEach { license ->
                    Text(
                        license.name,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                    Text("${license.author} · ${license.licenseName}", style = MaterialTheme.typography.bodySmall)
                    Text(
                        license.url,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.clickable { openUrl(license.url) },
                    )
                    HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_ok)) }
        },
    )
}
