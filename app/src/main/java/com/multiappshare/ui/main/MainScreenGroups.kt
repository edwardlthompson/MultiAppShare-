package com.multiappshare.ui.main

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.multiappshare.EmptyGroupsPlaceholder
import com.multiappshare.MainUiState
import com.multiappshare.MainViewModel
import com.multiappshare.R
import com.multiappshare.ShareSessionState
import com.multiappshare.model.AppGroup
import com.multiappshare.ui.groups.GroupWorkspace

@Composable
internal fun MainScreenGroupsSection(
    state: MainUiState.Success,
    viewModel: MainViewModel,
    shareSession: ShareSessionState,
    packageManager: PackageManager,
    groupFilterQuery: String,
    onGroupFilterChange: (String) -> Unit,
    onShowCreateGroupDialog: (Boolean) -> Unit,
    onShowModifyGroupDialog: (AppGroup?) -> Unit,
    onShowReorderDialog: (AppGroup?) -> Unit,
    onGroupToDelete: (AppGroup?) -> Unit,
    selectedGroup: AppGroup? = null,
    onSelectGroup: (AppGroup) -> Unit = {},
    onDuplicateGroup: (AppGroup) -> Unit = {},
    onRenameGroup: (AppGroup) -> Unit = {},
    onMergeGroup: (AppGroup) -> Unit = {},
    onStartSharing: (AppGroup, MainViewModel) -> Unit,
) {
    val filterGroupsCd = stringResource(R.string.cd_filter_groups)
    val inShareMode = shareSession.inShareMode
    val uris = shareSession.uris
    val mimeType = shareSession.mimeType
    Column {
        if (inShareMode) ShareOverlayHeader()
        if (!inShareMode && state.groups.size > 8) {
            OutlinedTextField(
                value = groupFilterQuery,
                onValueChange = onGroupFilterChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .semantics { contentDescription = filterGroupsCd },
                label = { Text(stringResource(R.string.label_filter_groups)) },
                singleLine = true,
                placeholder = { Text(stringResource(R.string.label_filter_groups)) },
            )
        }
        val filteredGroups = if (inShareMode) {
            filterCompatibleGroups(state.groups, uris, mimeType, viewModel::getCompatiblePackages)
        } else {
            state.groups
        }
        val displayGroups = if (!inShareMode && state.groups.size > 8) {
            filteredGroups.filter { it.name.contains(groupFilterQuery, ignoreCase = true) }
        } else {
            filteredGroups
        }
        when {
            filteredGroups.isEmpty() && inShareMode -> CompatibleGroupsEmptyState(mimeType)
            filteredGroups.isEmpty() -> EmptyGroupsPlaceholder(
                onAddGroup = { onShowCreateGroupDialog(true) },
                onAutoGroup = { viewModel.autoGroupApps(state.allApps, append = false) },
            )
            displayGroups.isEmpty() -> FilterEmptyState()
            else -> GroupWorkspace(
                groups = displayGroups,
                selectedGroup = selectedGroup,
                onSelectGroup = onSelectGroup,
                onModifyClick = { onShowModifyGroupDialog(it) },
                onReorderClick = { onShowReorderDialog(it) },
                onDuplicateClick = onDuplicateGroup,
                onRenameClick = onRenameGroup,
                onMergeClick = onMergeGroup,
                onDeleteClick = { onGroupToDelete(it) },
                onToggleExpanded = { viewModel.toggleGroupExpanded(it) },
                onGroupClick = { onStartSharing(it, viewModel) },
                onAddShortcutClick = { viewModel.createShortcutForGroup(it) },
                inShareMode = inShareMode,
                packageManager = packageManager,
            )
        }
    }
}
