package com.multiappshare.ui.main

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.multiappshare.DeleteGroupDialog
import com.multiappshare.MainUiState
import com.multiappshare.MainViewModel
import com.multiappshare.R
import com.multiappshare.model.AppGroup
import com.multiappshare.ui.dashboard.AboutDialogLabels
import com.multiappshare.ui.dashboard.DashboardAboutDialog
import com.multiappshare.ui.dashboard.DashboardHistoryDialog
import com.multiappshare.ui.dashboard.HistoryDialogLabels
import com.multiappshare.ui.groups.CreateGroupDialog
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
    val duplicateGroupToast = stringResource(R.string.toast_duplicate_group_name)
    val refreshingAppsToast = stringResource(R.string.toast_refreshing_apps)
    val historyLabels = HistoryDialogLabels(
        title = stringResource(R.string.history_title),
        empty = stringResource(R.string.history_empty),
        sharedPrefix = stringResource(R.string.history_shared_prefix),
        close = stringResource(R.string.button_close),
        reshare = stringResource(R.string.history_reshare_last),
    )
    val reshareFailedToast = stringResource(R.string.toast_reshare_unavailable)
    if (showCreateGroupDialog) {
        CreateGroupDialog(
            onDismiss = { onShowCreateGroupDialog(false) },
            onCreateGroup = { name ->
                viewModel.createGroup(name) { success ->
                    if (success) {
                        onShowCreateGroupDialog(false)
                    } else {
                        Toast.makeText(context, duplicateGroupToast, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, refreshingAppsToast, Toast.LENGTH_SHORT).show()
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
            labels = historyLabels,
            onDismiss = { onShowHistoryDialog(false) },
            onReshare = if (viewModel.hasLastSharePayload) {
                {
                    viewModel.restoreLastPayload { ok ->
                        if (ok) {
                            onShowHistoryDialog(false)
                        } else {
                            Toast.makeText(context, reshareFailedToast, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                null
            },
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
        val versionLabel = version ?: stringResource(R.string.version_unknown)
        DashboardAboutDialog(
            labels = AboutDialogLabels(
                title = stringResource(R.string.about_title),
                versionLine = stringResource(R.string.version_line, versionLabel),
                tagline = stringResource(R.string.about_tagline),
                privacy = stringResource(R.string.about_privacy),
                developerContact = stringResource(R.string.about_developer_contact),
                telegramLabel = stringResource(R.string.about_telegram_label),
                supportDeveloper = stringResource(R.string.about_support_developer),
                venmoLink = stringResource(R.string.about_venmo_link),
                ok = stringResource(R.string.button_ok),
            ),
            onDismiss = { onShowAboutDialog(false) },
        )
    }
}
