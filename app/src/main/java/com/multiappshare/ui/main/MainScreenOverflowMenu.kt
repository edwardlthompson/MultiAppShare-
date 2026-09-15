package com.multiappshare.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.multiappshare.R
import com.multiappshare.domain.OverflowDestination
import com.multiappshare.domain.OverflowMenuPolicy

@Composable
fun MainScreenOverflowMenu(
    onSortGroups: () -> Unit,
    onAutoGroup: () -> Unit,
    onShareClipboard: () -> Unit,
    onHistory: () -> Unit,
    onSettings: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { menuExpanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.cd_main_menu))
        }
        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
            OverflowMenuPolicy.items.forEach { item ->
                val label: Int
                val icon = when (item.destination) {
                    OverflowDestination.SORT_GROUPS -> {
                        label = R.string.menu_sort_groups
                        Icons.AutoMirrored.Filled.List
                    }
                    OverflowDestination.AUTO_GROUP -> {
                        label = R.string.fab_auto_group
                        Icons.Default.AutoAwesome
                    }
                    OverflowDestination.SHARE_CLIPBOARD -> {
                        label = R.string.menu_share_clipboard
                        Icons.Default.ContentPaste
                    }
                    OverflowDestination.HISTORY -> {
                        label = R.string.menu_history
                        Icons.Default.History
                    }
                    OverflowDestination.SETTINGS -> {
                        label = R.string.menu_settings
                        Icons.Default.Settings
                    }
                }
                val action = when (item.destination) {
                    OverflowDestination.SORT_GROUPS -> onSortGroups
                    OverflowDestination.AUTO_GROUP -> onAutoGroup
                    OverflowDestination.SHARE_CLIPBOARD -> onShareClipboard
                    OverflowDestination.HISTORY -> onHistory
                    OverflowDestination.SETTINGS -> onSettings
                }
                DropdownMenuItem(
                    text = { Text(stringResource(label)) },
                    leadingIcon = { Icon(icon, contentDescription = stringResource(label)) },
                    onClick = {
                        action()
                        menuExpanded = false
                    },
                )
            }
        }
    }
}
