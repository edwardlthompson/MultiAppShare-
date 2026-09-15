package com.multiappshare.ui.main

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.multiappshare.MainUiState
import com.multiappshare.R

@Composable
internal fun MainScreenHomeFab(
    uiState: MainUiState,
    inShareMode: Boolean,
    onAddGroup: () -> Unit,
) {
    if (!inShareMode && uiState is MainUiState.Success && uiState.groups.isNotEmpty()) {
        val addLabel = stringResource(R.string.fab_add_group)
        ExtendedFloatingActionButton(
            onClick = onAddGroup,
            modifier = Modifier.navigationBarsPadding(),
            icon = { Icon(Icons.Default.Add, contentDescription = addLabel) },
            text = { Text(addLabel) },
        )
    }
}
