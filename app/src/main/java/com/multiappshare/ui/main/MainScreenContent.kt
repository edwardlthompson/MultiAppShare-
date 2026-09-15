package com.multiappshare.ui.main

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.multiappshare.DeleteGroupDialog
import com.multiappshare.MainUiState
import com.multiappshare.MainViewModel
import com.multiappshare.R
import com.multiappshare.domain.InlineFieldError
import com.multiappshare.model.AppGroup
import com.multiappshare.ui.dashboard.AboutDialogLabels
import com.multiappshare.ui.dashboard.DashboardAboutDialog
import com.multiappshare.ui.dashboard.DashboardHistoryDialog
import com.multiappshare.ui.dashboard.HistoryDialogLabels
import com.multiappshare.ui.groups.CreateGroupDialog
import com.multiappshare.ui.groups.LocalRequestAutoGroupPreview
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
    onCrashReviewVisible: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    var showFeedback by remember { mutableStateOf(false) }
    var showWhatsNew by remember { mutableStateOf(false) }
    var showPrivacy by remember { mutableStateOf(false) }
    var showLicenses by remember { mutableStateOf(false) }
    MainScreenGoldenPathHost(
        showFeedback = showFeedback,
        onShowFeedback = { showFeedback = it },
        onboardingVisible = viewModel.showOnboardingDialog,
        onCrashReviewVisible = onCrashReviewVisible,
    )
    val requestAutoGroup = LocalRequestAutoGroupPreview.current
    val duplicateGroupMessage = stringResource(R.string.toast_duplicate_group_name)
    val refreshingAppsToast = stringResource(R.string.toast_refreshing_apps)
    var createNameError by remember { mutableStateOf<String?>(null) }
    var historyError by remember { mutableStateOf<String?>(null) }
    val historyLabels = HistoryDialogLabels(
        title = stringResource(R.string.history_title),
        empty = stringResource(R.string.history_empty),
        emptyCta = stringResource(R.string.history_empty_cta),
        sharedPrefix = stringResource(R.string.history_shared_prefix),
        close = stringResource(R.string.button_close),
        reshare = stringResource(R.string.history_reshare_last),
        reshareRow = stringResource(R.string.history_reshare_row),
        error = stringResource(R.string.history_error_cd),
    )
    val reshareFailedMessage = stringResource(R.string.toast_reshare_unavailable)
    if (showCreateGroupDialog) {
        CreateGroupDialog(
            onDismiss = {
                createNameError = null
                onShowCreateGroupDialog(false)
            },
            nameError = createNameError,
            onNameEdited = { createNameError = null },
            onCreateGroup = { name ->
                viewModel.createGroup(name) { success ->
                    createNameError = InlineFieldError.of(success, duplicateGroupMessage)
                    if (success) {
                        onShowCreateGroupDialog(false)
                    }
                }
            },
        )
    }
    if (viewModel.showOnboardingDialog) {
        OnboardingDialog(
            onAutofill = { requestAutoGroup(false, true) },
            onManual = { viewModel.setOnboardingDismissed() },
            onSkip = { viewModel.setOnboardingDismissed() },
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
            errorText = historyError,
            onDismiss = {
                historyError = null
                onShowHistoryDialog(false)
            },
            onReshare = if (viewModel.hasLastSharePayload) {
                {
                    viewModel.restoreLastPayload { ok ->
                        historyError = InlineFieldError.of(ok, reshareFailedMessage)
                        if (ok) {
                            onShowHistoryDialog(false)
                        }
                    }
                }
            } else {
                null
            },
            onReshareItem = { item ->
                viewModel.restoreHistoryPayload(item) { ok ->
                    historyError = InlineFieldError.of(ok, reshareFailedMessage)
                    if (ok) {
                        onShowHistoryDialog(false)
                    }
                }
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
                venmoLink = stringResource(R.string.about_donate),
                changelog = stringResource(R.string.about_changelog),
                feedback = stringResource(R.string.feedback_title),
                ok = stringResource(R.string.button_ok),
                noAppForLink = stringResource(R.string.error_no_app_for_link),
                privacyOpen = stringResource(R.string.settings_privacy),
                licenses = stringResource(R.string.settings_licenses),
                shortcuts = stringResource(R.string.about_shortcuts),
            ),
            onDismiss = { onShowAboutDialog(false) },
            onFeedback = { showFeedback = true },
            onChangelog = { showWhatsNew = true },
            onPrivacy = { showPrivacy = true },
            onLicenses = { showLicenses = true },
        )
    }
    if (showWhatsNew) {
        com.multiappshare.ui.about.WhatsNewDialog(onDismiss = { showWhatsNew = false })
    }
    if (showPrivacy) {
        com.multiappshare.ui.about.PrivacyPolicyDialog(onDismiss = { showPrivacy = false })
    }
    if (showLicenses) {
        com.multiappshare.ui.about.OssLicensesDialog(onDismiss = { showLicenses = false })
    }
    if (viewModel.pinUnsupported) {
        com.multiappshare.ui.groups.PinUnsupportedDialog(onDismiss = { viewModel.pinUnsupported = false })
    }
}
