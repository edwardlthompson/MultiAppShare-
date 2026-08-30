package com.multiappshare.ui.main

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.multiappshare.payloadpreview.PayloadPreview
import com.multiappshare.ui.groups.GroupWorkspace
import com.multiappshare.payloadpreview.PayloadReorder
import com.multiappshare.ui.share.PayloadPreviewDialog
import com.multiappshare.ui.share.ReorderAttachmentsDialog

@Composable
internal fun GroupFilterField(
    query: String,
    onChange: (String) -> Unit,
    contentDescription: String,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .semantics { this.contentDescription = contentDescription },
        label = { Text(stringResource(R.string.label_filter_groups)) },
        singleLine = true,
        placeholder = { Text(stringResource(R.string.label_filter_groups)) },
    )
}

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
    var previewGroup by remember { mutableStateOf<AppGroup?>(null) }
    var reorderGroup by remember { mutableStateOf<AppGroup?>(null) }
    val uris = shareSession.uris
    val mimeType = shareSession.mimeType
    Column {
        if (inShareMode) ShareOverlayHeader()
        if (!inShareMode && state.groups.isNotEmpty()) {
            GroupFilterField(
                query = groupFilterQuery,
                onChange = onGroupFilterChange,
                contentDescription = filterGroupsCd,
            )
        }
        val filteredGroups = if (inShareMode) {
            filterCompatibleGroups(state.groups, uris, mimeType, viewModel::getCompatiblePackages)
        } else {
            state.groups
        }
        val displayGroups = if (!inShareMode && state.groups.isNotEmpty()) {
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
                onGroupClick = { group ->
                    if (PayloadPreview.shouldShow(inShareMode, shareSession.sharingStarted)) {
                        previewGroup = group
                    } else {
                        onStartSharing(group, viewModel)
                    }
                },
                onAddShortcutClick = { viewModel.createShortcutForGroup(it) },
                inShareMode = inShareMode,
                packageManager = packageManager,
            )
        }
    }
    previewGroup?.let { group ->
        PayloadPreviewDialog(
            mime = mimeType,
            text = shareSession.text,
            uriCount = PayloadPreview.uriCount(uris),
            onConfirm = {
                previewGroup = null
                if (PayloadReorder.shouldOffer(PayloadPreview.uriCount(uris))) {
                    reorderGroup = group
                } else {
                    onStartSharing(group, viewModel)
                }
            },
            onDismiss = { previewGroup = null },
        )
    }
    reorderGroup?.let { group ->
        val current = uris.orEmpty()
        ReorderAttachmentsDialog(
            uris = current,
            onConfirm = { ordered ->
                reorderGroup = null
                viewModel.updateShareSession { copy(uris = ordered) }
                onStartSharing(group, viewModel)
            },
            onDismiss = { reorderGroup = null },
        )
    }
}
