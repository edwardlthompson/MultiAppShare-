package com.multiappshare.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
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

@Composable
fun MainScreenOverflowMenu(
    onSortGroups: () -> Unit,
    onHistory: () -> Unit,
    onAbout: () -> Unit,
    onLanguage: () -> Unit,
    onTheme: () -> Unit,
    onSharingDelay: () -> Unit,
    onExport: () -> Unit,
    onImport: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { menuExpanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.cd_main_menu))
        }
        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_sort_groups)) },
                leadingIcon = { Icon(Icons.AutoMirrored.Filled.List, null) },
                onClick = {
                    onSortGroups()
                    menuExpanded = false
                },
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_history)) },
                leadingIcon = { Icon(Icons.Default.Refresh, null) },
                onClick = {
                    onHistory()
                    menuExpanded = false
                },
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_language)) },
                leadingIcon = { Icon(Icons.Default.Settings, null) },
                onClick = {
                    onLanguage()
                    menuExpanded = false
                },
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_theme)) },
                leadingIcon = { Icon(Icons.Default.Settings, null) },
                onClick = {
                    onTheme()
                    menuExpanded = false
                },
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_sharing_delay)) },
                leadingIcon = { Icon(Icons.Default.Settings, null) },
                onClick = {
                    onSharingDelay()
                    menuExpanded = false
                },
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_about)) },
                leadingIcon = { Icon(Icons.Default.Info, null) },
                onClick = {
                    onAbout()
                    menuExpanded = false
                },
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_export_groups)) },
                leadingIcon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, null) },
                onClick = {
                    onExport()
                    menuExpanded = false
                },
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.menu_import_groups)) },
                leadingIcon = { Icon(Icons.Default.GetApp, null) },
                onClick = {
                    onImport()
                    menuExpanded = false
                },
            )
        }
    }
}
