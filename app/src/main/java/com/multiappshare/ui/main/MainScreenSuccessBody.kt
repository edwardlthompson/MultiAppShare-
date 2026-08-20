package com.multiappshare.ui.main

import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import com.multiappshare.MainUiState
import com.multiappshare.MainViewModel
import com.multiappshare.ShareSessionState
import com.multiappshare.model.AppGroup

@Composable
internal fun MainScreenSuccessBody(
    state: MainUiState.Success,
    viewModel: MainViewModel,
    shareSession: ShareSessionState,
    packageManager: PackageManager,
    showCreateGroupDialog: Boolean,
    onShowCreateGroupDialog: (Boolean) -> Unit,
    showModifyGroupDialog: AppGroup?,
    onShowModifyGroupDialog: (AppGroup?) -> Unit,
    showReorderDialog: AppGroup?,
    onShowReorderDialog: (AppGroup?) -> Unit,
    showSortGroupsDialog: Boolean,
    onShowSortGroupsDialog: (Boolean) -> Unit,
    groupToDelete: AppGroup?,
    onGroupToDelete: (AppGroup?) -> Unit,
    showHistoryDialog: Boolean,
    onShowHistoryDialog: (Boolean) -> Unit,
    showAboutDialog: Boolean,
    onShowAboutDialog: (Boolean) -> Unit,
    groupFilterQuery: String,
    onGroupFilterChange: (String) -> Unit,
    selectedGroup: AppGroup?,
    onSelectGroup: (AppGroup?) -> Unit,
    groupToRename: AppGroup?,
    onGroupToRename: (AppGroup?) -> Unit,
    groupToMerge: AppGroup?,
    onGroupToMerge: (AppGroup?) -> Unit,
    onStartSharing: (AppGroup, MainViewModel) -> Unit,
) {
    MainScreenGroupEditHost(
        viewModel = viewModel,
        allGroups = state.groups,
        groupToRename = groupToRename,
        onGroupToRename = onGroupToRename,
        groupToMerge = groupToMerge,
        onGroupToMerge = onGroupToMerge,
        onRenamed = { old, newName ->
            if (selectedGroup?.name == old.name) {
                onSelectGroup(selectedGroup?.copy(name = newName))
            }
        },
        onMerged = { sourceName ->
            if (selectedGroup?.name == sourceName) onSelectGroup(null)
        },
    )
    MainScreenDialogsHost(
        state = state,
        viewModel = viewModel,
        packageManager = packageManager,
        showCreateGroupDialog = showCreateGroupDialog,
        onShowCreateGroupDialog = onShowCreateGroupDialog,
        showModifyGroupDialog = showModifyGroupDialog,
        onShowModifyGroupDialog = onShowModifyGroupDialog,
        showReorderDialog = showReorderDialog,
        onShowReorderDialog = onShowReorderDialog,
        showSortGroupsDialog = showSortGroupsDialog,
        onShowSortGroupsDialog = onShowSortGroupsDialog,
        groupToDelete = groupToDelete,
        onGroupToDelete = onGroupToDelete,
        showHistoryDialog = showHistoryDialog,
        onShowHistoryDialog = onShowHistoryDialog,
        showAboutDialog = showAboutDialog,
        onShowAboutDialog = onShowAboutDialog,
    )
    MainScreenGroupsSection(
        state = state,
        viewModel = viewModel,
        shareSession = shareSession,
        packageManager = packageManager,
        groupFilterQuery = groupFilterQuery,
        onGroupFilterChange = onGroupFilterChange,
        onShowCreateGroupDialog = onShowCreateGroupDialog,
        onShowModifyGroupDialog = onShowModifyGroupDialog,
        onShowReorderDialog = onShowReorderDialog,
        onGroupToDelete = onGroupToDelete,
        selectedGroup = selectedGroup,
        onSelectGroup = { onSelectGroup(it) },
        onDuplicateGroup = { viewModel.duplicateGroup(it) },
        onRenameGroup = { onGroupToRename(it) },
        onMergeGroup = { onGroupToMerge(it) },
        onStartSharing = onStartSharing,
    )
}
