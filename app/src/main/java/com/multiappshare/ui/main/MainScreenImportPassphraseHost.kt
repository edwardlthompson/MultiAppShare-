package com.multiappshare.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.multiappshare.BackupImportPassphraseDialog
import com.multiappshare.MainViewModel

@Composable
internal fun MainScreenImportPassphraseHost(viewModel: MainViewModel) {
    var importErrorRes by remember { mutableStateOf<Int?>(null) }
    val pendingUri = viewModel.importPassphrasePendingUri ?: return
    BackupImportPassphraseDialog(
        errorText = importErrorRes?.let { stringResource(it) },
        onPassEdited = { importErrorRes = null },
        onDismiss = {
            importErrorRes = null
            viewModel.dismissImportPassphraseRequest()
        },
        onConfirm = { chars ->
            viewModel.importGroupsWithPassphrase(pendingUri, chars) { importErrorRes = it }
        },
    )
}
