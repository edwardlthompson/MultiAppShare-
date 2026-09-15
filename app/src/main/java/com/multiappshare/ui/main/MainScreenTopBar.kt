package com.multiappshare.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.multiappshare.MainViewModel
import com.multiappshare.R
import com.multiappshare.domain.HomeTopBarActions
import com.multiappshare.domain.ShareSessionChrome
import com.multiappshare.resolveShareTargetLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShareModeTopBar(
    onClose: () -> Unit,
    sharingStarted: Boolean = false,
    appComponents: List<String>? = null,
    currentIndex: Int = 0,
) {
    val context = LocalContext.current
    val packages = appComponents.orEmpty()
    val currentKey = packages.getOrNull(currentIndex).orEmpty()
    val resolvedLabel = remember(currentKey) {
        resolveShareTargetLabel(context.packageManager, currentKey)
    }
    val fallbackLabel = stringResource(R.string.app_name)
    val identity = ShareSessionChrome.identity(
        resolvedLabel.ifBlank { fallbackLabel },
        currentIndex,
        packages.size,
    )
    val title = if (ShareSessionChrome.showProgressTitle(sharingStarted, packages.size) && identity != null) {
        stringResource(
            R.string.share_session_chrome_title,
            identity.step,
            identity.total,
            identity.appLabel,
        )
    } else {
        stringResource(R.string.share_overlay_title)
    }
    TopAppBar(
        title = { Text(title, maxLines = 2, modifier = Modifier.semantics { heading() }) },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.cd_close_share))
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreenTopBar(
    viewModel: MainViewModel,
    onSortGroups: () -> Unit,
    onHistory: () -> Unit,
    onSettings: () -> Unit,
    onAutoGroup: () -> Unit,
    groupCount: Int = 0,
) {
    val context = LocalContext.current
    TopAppBar(
        title = {
            Text(
                stringResource(R.string.groups_title),
                maxLines = 2,
                modifier = Modifier.semantics { heading() },
            )
        },
        actions = {
            if (HomeTopBarActions.showReorder(groupCount)) {
                IconButton(onClick = onSortGroups) {
                    Icon(
                        Icons.Default.SwapVert,
                        contentDescription = stringResource(R.string.cd_reorder_groups),
                    )
                }
            }
            MainScreenOverflowMenu(
                onSortGroups = onSortGroups,
                onAutoGroup = onAutoGroup,
                onShareClipboard = { viewModel.shareFromClipboard(context) },
                onHistory = onHistory,
                onSettings = onSettings,
            )
        },
    )
}
