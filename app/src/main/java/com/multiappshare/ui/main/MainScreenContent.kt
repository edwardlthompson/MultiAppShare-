package com.multiappshare.ui.main

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.multiappshare.DeleteGroupDialog
import com.multiappshare.EmptyGroupsPlaceholder
import com.multiappshare.MainUiState
import com.multiappshare.MainViewModel
import com.multiappshare.R
import com.multiappshare.ShareSessionState
import com.multiappshare.model.AppGroup
import com.multiappshare.ui.dashboard.AboutDialogLabels
import com.multiappshare.ui.dashboard.DashboardAboutDialog
import com.multiappshare.ui.dashboard.DashboardHistoryDialog
import com.multiappshare.ui.dashboard.HistoryDialogLabels
import com.multiappshare.ui.groups.CreateGroupDialog
import com.multiappshare.ui.groups.GroupList
import com.multiappshare.ui.groups.ModifyGroupAppsDialog
import com.multiappshare.ui.groups.ReorderAppsDialog
import com.multiappshare.ui.groups.SortGroupsDialog
import com.multiappshare.ui.onboarding.OnboardingDialog

@Composable
internal fun MainScreenDialogsHost(
    state: MainUiState.Success,
    viewModel: MainViewModel,
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
) {
    val context = LocalContext.current

    if (showCreateGroupDialog) {
        CreateGroupDialog(
            onDismiss = { onShowCreateGroupDialog(false) },
            onCreateGroup = { name ->
                viewModel.createGroup(name) { success ->
                    if (success) {
                        onShowCreateGroupDialog(false)
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_duplicate_group_name),
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            },
        )
    }

    if (viewModel.showOnboardingDialog) {
        OnboardingDialog(
            onAutofill = {
                viewModel.autoGroupApps(state.allApps, append = false)
                viewModel.setOnboardingDismissed()
            },
            onManual = { viewModel.setOnboardingDismissed() },
        )
    }

    showModifyGroupDialog?.let { group ->
        ModifyGroupAppsDialog(
            allApps = state.allApps,
            group = group,
            onDismiss = { onShowModifyGroupDialog(null) },
            onSaveApps = { apps ->
                viewModel.updateGroupApps(group, apps)
                onShowModifyGroupDialog(null)
            },
            onRefresh = {
                viewModel.loadData()
                Toast.makeText(context, context.getString(R.string.toast_refreshing_apps), Toast.LENGTH_SHORT).show()
            },
            packageManager = packageManager,
        )
    }

    showReorderDialog?.let { group ->
        ReorderAppsDialog(
            group = group,
            onDismiss = { onShowReorderDialog(null) },
            onSaveOrder = { apps ->
                viewModel.updateGroupApps(group, apps)
                onShowReorderDialog(null)
            },
        )
    }

    if (showSortGroupsDialog) {
        SortGroupsDialog(
            groups = state.groups,
            onDismiss = { onShowSortGroupsDialog(false) },
            onSaveOrder = { groups ->
                viewModel.updateGroupsOrder(groups)
                onShowSortGroupsDialog(false)
            },
        )
    }

    groupToDelete?.let { group ->
        DeleteGroupDialog(
            groupName = group.name,
            onDismiss = { onGroupToDelete(null) },
            onConfirm = {
                viewModel.deleteGroup(group)
                onGroupToDelete(null)
            },
        )
    }

    if (showHistoryDialog) {
        DashboardHistoryDialog(
            history = state.history,
            labels = HistoryDialogLabels(
                title = context.getString(R.string.history_title),
                empty = context.getString(R.string.history_empty),
                sharedPrefix = context.getString(R.string.history_shared_prefix),
                close = context.getString(R.string.button_close),
            ),
            onDismiss = { onShowHistoryDialog(false) },
        )
    }
    if (showAboutDialog) {
        val version = try {
            val pInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(context.packageName, 0)
            }
            pInfo.versionName
        } catch (_: Exception) {
            null
        }
        val versionLabel = version ?: context.getString(R.string.version_unknown)
        DashboardAboutDialog(
            labels = AboutDialogLabels(
                title = context.getString(R.string.about_title),
                versionLine = context.getString(R.string.version_line, versionLabel),
                tagline = context.getString(R.string.about_tagline),
                privacy = context.getString(R.string.about_privacy),
                developerContact = context.getString(R.string.about_developer_contact),
                telegramLabel = context.getString(R.string.about_telegram_label),
                supportDeveloper = context.getString(R.string.about_support_developer),
                venmoLink = context.getString(R.string.about_venmo_link),
                ok = context.getString(R.string.button_ok),
            ),
            onDismiss = { onShowAboutDialog(false) },
        )
    }
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
    onStartSharing: (AppGroup, MainViewModel) -> Unit,
) {
    val context = LocalContext.current

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
                    .semantics { contentDescription = context.getString(R.string.cd_filter_groups) },
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
            else -> GroupList(
                groups = displayGroups,
                onModifyClick = { onShowModifyGroupDialog(it) },
                onReorderClick = { onShowReorderDialog(it) },
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
