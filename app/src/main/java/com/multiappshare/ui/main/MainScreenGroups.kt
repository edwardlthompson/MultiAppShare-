package com.multiappshare.ui.main

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.multiappshare.EmptyGroupsPlaceholder
import com.multiappshare.MainUiState
import com.multiappshare.MainViewModel
import com.multiappshare.R
import com.multiappshare.ShareSessionState
import com.multiappshare.domain.GroupListFilter
import com.multiappshare.domain.GroupRowMeta
import com.multiappshare.model.AppGroup
import com.multiappshare.payloadpreview.PayloadPreview
import com.multiappshare.ui.groups.GroupWorkspace
import com.multiappshare.ui.groups.LocalRequestAutoGroupPreview
import com.multiappshare.ui.share.SharePayloadSheet
import com.multiappshare.ui.sharing.InlineNoticeBanner
import com.multiappshare.ui.sharing.NotificationPermissionBanner

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
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        trailingIcon = {
            if (GroupListFilter.showClear(query)) {
                IconButton(onClick = { onChange("") }) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = stringResource(R.string.cd_clear_search),
                    )
                }
            }
        },
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
    onShowSortGroups: () -> Unit = {},
    onGroupToDelete: (AppGroup?) -> Unit,
    selectedGroup: AppGroup? = null,
    onSelectGroup: (AppGroup) -> Unit = {},
    onDuplicateGroup: (AppGroup) -> Unit = {},
    onRenameGroup: (AppGroup) -> Unit = {},
    onMergeGroup: (AppGroup) -> Unit = {},
    onStartSharing: (AppGroup, MainViewModel) -> Unit,
) {
    val filterGroupsCd = stringResource(R.string.cd_filter_groups)
    val requestAutoGroup = LocalRequestAutoGroupPreview.current
    val inShareMode = shareSession.inShareMode
    val pinnedNames by viewModel.appearance.pinnedGroupNames.collectAsState(initial = emptySet())
    var previewGroup by remember { mutableStateOf<AppGroup?>(null) }
    val uris = shareSession.uris
    val mimeType = shareSession.mimeType
    Column {
        shareSession.noticeRes?.let { res ->
            InlineNoticeBanner(text = stringResource(res))
        }
        if (inShareMode) {
            ShareOverlayHeader()
            NotificationPermissionBanner(
                permissionGranted = viewModel.notificationsEnabled,
                inShareSession = true,
            )
        }
        if (state.groups.isNotEmpty()) {
            GroupFilterField(
                query = groupFilterQuery,
                onChange = onGroupFilterChange,
                contentDescription = filterGroupsCd,
            )
            if (!inShareMode && state.groups.size > 1) {
                TextButton(
                    onClick = onShowSortGroups,
                    modifier = Modifier.padding(horizontal = 8.dp),
                ) {
                    Text(stringResource(R.string.menu_sort_groups))
                }
            }
        }
        val filteredGroups = if (inShareMode) {
            filterCompatibleGroups(state.groups, uris, mimeType, viewModel::getCompatiblePackages)
        } else {
            state.groups
        }
        val displayGroups = filteredGroups.filter { GroupListFilter.matches(it.name, groupFilterQuery) }
        when {
            filteredGroups.isEmpty() && inShareMode -> CompatibleGroupsEmptyState(
                mimeType = mimeType,
                onAddGroup = { onShowCreateGroupDialog(true) },
            )
            filteredGroups.isEmpty() -> EmptyGroupsPlaceholder(
                onAddGroup = { onShowCreateGroupDialog(true) },
                onAutoGroup = { requestAutoGroup(false, false) },
            )
            displayGroups.isEmpty() -> FilterEmptyState(onClear = { onGroupFilterChange("") })
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
                onPinClick = { viewModel.toggleGroupPin(it.name) },
                pinnedGroupNames = pinnedNames,
                lastSharedByGroup = GroupRowMeta.latestTimestampByName(
                    state.history.map { it.groupName to it.timestamp },
                ),
                inShareMode = inShareMode,
                packageManager = packageManager,
            )
        }
    }
    previewGroup?.let { group ->
        SharePayloadSheet(
            mime = mimeType,
            text = shareSession.text,
            uris = uris.orEmpty(),
            onConfirm = { ordered ->
                previewGroup = null
                viewModel.updateShareSession { copy(uris = ordered) }
                onStartSharing(group, viewModel)
            },
            onDismiss = { previewGroup = null },
        )
    }
}
