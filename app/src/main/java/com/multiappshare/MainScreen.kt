package com.multiappshare

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.multiappshare.core.ui.ShareSuccessAnimation
import com.multiappshare.model.AppGroup
import com.multiappshare.ui.groups.GroupDeleteSnackbarHost
import com.multiappshare.ui.groups.GroupDeleteUndoEffect
import com.multiappshare.ui.main.MainScreenOverflowMenu
import com.multiappshare.ui.main.MainScreenSettingsHost
import com.multiappshare.ui.main.MainScreenSuccessBody
import com.multiappshare.ui.main.ShareSessionBackHandler
import com.multiappshare.ui.sharing.SharingInProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onStartSharing: (AppGroup, MainViewModel) -> Unit,
    onNextStep: () -> Unit,
    onReplayShareStep: () -> Unit = {},
    onPreviousShareStep: () -> Unit = {},
    onFinishEarly: () -> Unit = {},
    onSkipThisApp: () -> Unit = {},
    onCancelShare: () -> Unit = {},
    packageManager: PackageManager,
    onExport: () -> Unit,
    onImport: () -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val shareSession = viewModel.shareSession
    var showSuccessAnimation by remember { mutableStateOf(false) }
    var showCreateGroupDialog by remember { mutableStateOf(false) }
    var showModifyGroupDialog by remember { mutableStateOf<AppGroup?>(null) }
    var showReorderDialog by remember { mutableStateOf<AppGroup?>(null) }
    var showSortGroupsDialog by remember { mutableStateOf(false) }
    var groupToDelete by remember { mutableStateOf<AppGroup?>(null) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showDelayDialog by remember { mutableStateOf(false) }
    var groupToRename by remember { mutableStateOf<AppGroup?>(null) }
    var groupToMerge by remember { mutableStateOf<AppGroup?>(null) }
    var selectedGroup by remember { mutableStateOf<AppGroup?>(null) }
    var groupFilterQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    val inShareMode = shareSession.inShareMode
    val isLowRamDevice = remember(context) {
        (context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)?.isLowRamDevice == true
    }
    val shareBackdropBlur: Dp = if (isLowRamDevice) 0.dp else 20.dp
    val shareBackdropScrimAlpha = if (isLowRamDevice) 0.78f else 0.62f

    ShareSessionBackHandler(shareSession, onFinishEarly, onCancelShare)

    viewModel.importPassphrasePendingUri?.let { pendingUri ->
        BackupImportPassphraseDialog(
            onDismiss = { viewModel.dismissImportPassphraseRequest() },
            onConfirm = { chars -> viewModel.importGroupsWithPassphrase(pendingUri, chars) },
        )
    }
    MainScreenSettingsHost(
        viewModel = viewModel,
        showLanguage = showLanguageDialog,
        onShowLanguage = { showLanguageDialog = it },
        showTheme = showThemeDialog,
        onShowTheme = { showThemeDialog = it },
        showDelay = showDelayDialog,
        onShowDelay = { showDelayDialog = it },
    )
    GroupDeleteUndoEffect(
        lastDeleted = viewModel.lastDeletedGroup,
        hostState = snackbarHostState,
        onUndo = { viewModel.undoDeleteGroup() },
        onConsumed = { viewModel.clearLastDeletedGroup() },
    )

    Scaffold(
        topBar = {
            if (!inShareMode) {
                TopAppBar(
                    title = { Text(stringResource(R.string.groups_title), maxLines = 2) },
                    actions = {
                        MainScreenOverflowMenu(
                            onSortGroups = { showSortGroupsDialog = true },
                            onHistory = { showHistoryDialog = true },
                            onAbout = { showAboutDialog = true },
                            onLanguage = { showLanguageDialog = true },
                            onTheme = { showThemeDialog = true },
                            onSharingDelay = { showDelayDialog = true },
                            onExport = onExport,
                            onImport = onImport,
                        )
                    },
                )
            }
        },
        snackbarHost = { GroupDeleteSnackbarHost(snackbarHostState) },
        floatingActionButton = {
            val state = uiState
            if (!inShareMode && state is MainUiState.Success) {
                Column(horizontalAlignment = Alignment.End) {
                    ExtendedFloatingActionButton(
                        onClick = { viewModel.autoGroupApps(state.allApps, append = true) },
                        icon = { Icon(Icons.Default.Build, null) },
                        text = { Text(stringResource(R.string.fab_auto_group)) },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExtendedFloatingActionButton(
                        onClick = { showCreateGroupDialog = true },
                        icon = { Icon(Icons.Default.Add, null) },
                        text = { Text(stringResource(R.string.fab_add_group)) },
                    )
                }
            }
        },
    ) { padding ->
        val backgroundModifier = if (inShareMode) {
            Modifier.fillMaxSize().padding(padding).padding(16.dp)
        } else {
            Modifier.fillMaxSize().padding(padding)
        }

        Surface(
            modifier = backgroundModifier,
            color = Color.Transparent,
            shape = if (inShareMode) MaterialTheme.shapes.large else RectangleShape,
            tonalElevation = if (inShareMode) 8.dp else 0.dp,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (inShareMode) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .blur(shareBackdropBlur)
                            .background(MaterialTheme.colorScheme.scrim.copy(alpha = shareBackdropScrimAlpha)),
                    )
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    if (inShareMode && shareSession.sharingStarted && shareSession.appPackages != null) {
                        SharingInProgress(
                            mimeType = shareSession.mimeType,
                            text = shareSession.text,
                            uris = shareSession.uris,
                            currentIndex = shareSession.currentIndex,
                            totalApps = shareSession.appPackages.size,
                            appComponents = shareSession.appPackages,
                            packageManager = packageManager,
                            onReplayCurrentStep = onReplayShareStep,
                            onPreviousStep = onPreviousShareStep,
                            onSkipThisApp = onSkipThisApp,
                            onFinishEarly = onFinishEarly,
                            onNextStep = {
                                if (shareSession.currentIndex + 1 == shareSession.appPackages.size) {
                                    showSuccessAnimation = true
                                }
                                onNextStep()
                            },
                        )
                    } else {
                        when (val state = uiState) {
                            is MainUiState.Loading -> {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
                            is MainUiState.Success -> {
                                MainScreenSuccessBody(
                                    state = state,
                                    viewModel = viewModel,
                                    shareSession = shareSession,
                                    packageManager = packageManager,
                                    showCreateGroupDialog = showCreateGroupDialog,
                                    onShowCreateGroupDialog = { showCreateGroupDialog = it },
                                    showModifyGroupDialog = showModifyGroupDialog,
                                    onShowModifyGroupDialog = { showModifyGroupDialog = it },
                                    showReorderDialog = showReorderDialog,
                                    onShowReorderDialog = { showReorderDialog = it },
                                    showSortGroupsDialog = showSortGroupsDialog,
                                    onShowSortGroupsDialog = { showSortGroupsDialog = it },
                                    groupToDelete = groupToDelete,
                                    onGroupToDelete = { groupToDelete = it },
                                    showHistoryDialog = showHistoryDialog,
                                    onShowHistoryDialog = { showHistoryDialog = it },
                                    showAboutDialog = showAboutDialog,
                                    onShowAboutDialog = { showAboutDialog = it },
                                    groupFilterQuery = groupFilterQuery,
                                    onGroupFilterChange = { groupFilterQuery = it },
                                    selectedGroup = selectedGroup,
                                    onSelectGroup = { selectedGroup = it },
                                    groupToRename = groupToRename,
                                    onGroupToRename = { groupToRename = it },
                                    groupToMerge = groupToMerge,
                                    onGroupToMerge = { groupToMerge = it },
                                    onStartSharing = onStartSharing,
                                )
                            }
                        }
                    }

                    if (showSuccessAnimation) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            ShareSuccessAnimation(onAnimationEnd = { showSuccessAnimation = false })
                        }
                    }
                }
            }
        }
    }
}
