package com.multiappshare.ui.main

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.multiappshare.MainViewModel
import com.multiappshare.R
import com.multiappshare.ui.groups.GroupDeleteUndoEffect

@Composable
fun MainScreenSnackbarEffects(
    viewModel: MainViewModel,
    hostState: SnackbarHostState,
    onCreateGroup: () -> Unit,
) {
    GroupDeleteUndoEffect(
        lastDeleted = viewModel.lastDeletedGroup,
        hostState = hostState,
        onUndo = { viewModel.undoDeleteGroup() },
        onConsumed = { viewModel.clearLastDeletedGroup() },
    )
    val missing = viewModel.missingDeepLinkLabel
    val message = stringResource(R.string.error_group_not_found)
    val create = stringResource(R.string.empty_groups_button_create)
    LaunchedEffect(missing) {
        if (missing == null) return@LaunchedEffect
        val result = hostState.showSnackbar(
            message = message,
            actionLabel = create,
            duration = SnackbarDuration.Long,
        )
        if (result == SnackbarResult.ActionPerformed) onCreateGroup()
        viewModel.missingDeepLinkLabel = null
    }
    val copied = viewModel.lastDuplicatedName
    val copiedMessage = copied?.let { stringResource(R.string.snackbar_group_copied, it) }
    LaunchedEffect(copied) {
        if (copied == null || copiedMessage == null) return@LaunchedEffect
        hostState.showSnackbar(message = copiedMessage, duration = SnackbarDuration.Long)
        viewModel.lastDuplicatedName = null
    }
}
