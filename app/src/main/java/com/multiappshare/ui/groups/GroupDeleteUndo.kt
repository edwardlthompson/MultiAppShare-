package com.multiappshare.ui.groups

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.multiappshare.R
import com.multiappshare.model.AppGroup

@Composable
fun GroupDeleteSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState)
}

@Composable
fun GroupDeleteUndoEffect(
    lastDeleted: AppGroup?,
    hostState: SnackbarHostState,
    onUndo: () -> Unit,
    onConsumed: () -> Unit,
) {
    val message = stringResource(R.string.snackbar_group_deleted)
    val undo = stringResource(R.string.snackbar_undo)
    LaunchedEffect(lastDeleted?.name) {
        if (lastDeleted == null) return@LaunchedEffect
        val result = hostState.showSnackbar(
            message = message,
            actionLabel = undo,
            duration = SnackbarDuration.Short,
        )
        if (result == SnackbarResult.ActionPerformed) onUndo() else onConsumed()
    }
}
