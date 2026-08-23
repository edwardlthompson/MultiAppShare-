package com.multiappshare.ui.main

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.multiappshare.MainViewModel
import com.multiappshare.R
import com.multiappshare.updates.ProductUpdate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreenTopBar(
    viewModel: MainViewModel,
    onSortGroups: () -> Unit,
    onHistory: () -> Unit,
    onAbout: () -> Unit,
    onLanguage: () -> Unit,
    onTheme: () -> Unit,
    onSharingDelay: () -> Unit,
    onExport: () -> Unit,
    onImport: () -> Unit,
) {
    val context = LocalContext.current
    TopAppBar(
        title = { Text(stringResource(R.string.groups_title), maxLines = 2) },
        actions = {
            MainScreenOverflowMenu(
                onSortGroups = onSortGroups,
                onHistory = onHistory,
                onAbout = onAbout,
                onDonate = { openUrl(context, ProductUpdate.VENMO_URL) },
                onLanguage = onLanguage,
                onTheme = onTheme,
                onSharingDelay = onSharingDelay,
                onShareClipboard = { viewModel.shareFromClipboard(context) },
                onExport = onExport,
                onImport = onImport,
            )
        },
    )
}
