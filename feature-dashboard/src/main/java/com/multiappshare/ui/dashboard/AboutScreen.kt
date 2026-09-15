package com.multiappshare.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.multiappshare.about.AboutLinks
import com.multiappshare.core.ui.ExternalIntent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    labels: AboutDialogLabels,
    onDismiss: () -> Unit,
    onFeedback: () -> Unit = {},
    onChangelog: () -> Unit = {},
    onPrivacy: () -> Unit = {},
    onLicenses: () -> Unit = {},
) {
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    fun open(url: String) {
        if (!ExternalIntent.tryViewUrl(context, url)) {
            scope.launch { snackbar.showSnackbar(labels.noAppForLink) }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(labels.title) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = labels.ok)
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(labels.versionLine, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(labels.tagline)
            if (labels.shortcuts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    labels.shortcuts,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                labels.privacy,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = labels.privacyOpen,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onPrivacy() },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = labels.licenses,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onLicenses() },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = labels.changelog,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onChangelog() },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(labels.developerContact, fontWeight = FontWeight.Bold)
            Text(
                text = labels.telegramLabel,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { open(AboutLinks.TELEGRAM) },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(labels.supportDeveloper, fontWeight = FontWeight.Bold)
            Text(
                text = labels.venmoLink,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { open(AboutLinks.VENMO) },
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = labels.feedback,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    onFeedback()
                    onDismiss()
                },
            )
        }
    }
}
