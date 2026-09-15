package com.multiappshare

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.multiappshare.core.ui.ShareSuccessAnimation
import com.multiappshare.domain.ShareBackdropScrim
import com.multiappshare.model.AppGroup
import com.multiappshare.ui.groups.AutoGroupPreviewHost
import com.multiappshare.ui.groups.GroupDeleteSnackbarHost
import com.multiappshare.ui.groups.LocalRequestAutoGroupPreview
import com.multiappshare.ui.launch.LocalSnackbarHostState
import com.multiappshare.ui.main.GroupListSkeleton
import com.multiappshare.ui.main.MainScreenHomeFab
import com.multiappshare.ui.main.MainScreenImportPassphraseHost
import com.multiappshare.ui.main.MainScreenSettingsHost
import com.multiappshare.ui.main.MainScreenSharingHost
import com.multiappshare.ui.main.MainScreenSnackbarEffects
import com.multiappshare.ui.main.MainScreenSuccessBody
import com.multiappshare.ui.main.MainScreenTopBar
import com.multiappshare.ui.main.ShareModeTopBar
import com.multiappshare.ui.main.ShareSessionBackHandler
import com.multiappshare.ui.main.rememberSkipSuccessBurst
import com.multiappshare.ui.theme.Dimens

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
    var showCreateGroupDialog by rememberSaveable { mutableStateOf(false) }
    var showModifyGroupDialog by remember { mutableStateOf<AppGroup?>(null) }
    var showReorderDialog by remember { mutableStateOf<AppGroup?>(null) }
    var showSortGroupsDialog by rememberSaveable { mutableStateOf(false) }
    var groupToDelete by remember { mutableStateOf<AppGroup?>(null) }
    var showHistoryDialog by rememberSaveable { mutableStateOf(false) }
    var showAboutDialog by rememberSaveable { mutableStateOf(false) }
    var showLanguageDialog by rememberSaveable { mutableStateOf(false) }
    var showThemeDialog by rememberSaveable { mutableStateOf(false) }
    var showDelayDialog by rememberSaveable { mutableStateOf(false) }
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    var groupToRename by remember { mutableStateOf<AppGroup?>(null) }
    var groupToMerge by remember { mutableStateOf<AppGroup?>(null) }
    var selectedGroup by remember { mutableStateOf<AppGroup?>(null) }
    var groupFilterQuery by rememberSaveable { mutableStateOf("") }
    var showImportStrategy by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val inShareMode = shareSession.inShareMode
    val isLowRamDevice = remember(context) {
        (context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager)?.isLowRamDevice == true
    }
    val shareBackdropBlur = if (isLowRamDevice) 0.dp else Dimens.ShareBackdropBlur
    val shareBackdropScrimAlpha = ShareBackdropScrim.alpha(isLowRamDevice)

    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
        ShareSessionBackHandler(shareSession, onFinishEarly, onCancelShare)
        MainScreenImportPassphraseHost(viewModel)
        if (showImportStrategy) {
            val groupCount = (uiState as? MainUiState.Success)?.groups?.size ?: 0
            com.multiappshare.ui.backup.ImportStrategyDialog(
                existingGroupCount = groupCount,
                onDismiss = { showImportStrategy = false },
                onChosen = { strategy ->
                    showImportStrategy = false
                    viewModel.prepareImport(strategy)
                    onImport()
                },
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
            showSettings = showSettingsDialog,
            onShowSettings = { showSettingsDialog = it },
            onExport = onExport,
            onImport = { showImportStrategy = true },
            onHistory = { showHistoryDialog = true },
            onAbout = { showAboutDialog = true },
        )
        MainScreenSnackbarEffects(
            viewModel = viewModel,
            hostState = snackbarHostState,
            onCreateGroup = { showCreateGroupDialog = true },
        )

        AutoGroupPreviewHost(uiState = uiState, viewModel = viewModel) {
            val requestAutoGroup = LocalRequestAutoGroupPreview.current
            Scaffold(
                topBar = {
                    if (inShareMode) {
                        ShareModeTopBar(
                            onClose = onCancelShare,
                            sharingStarted = shareSession.sharingStarted,
                            appComponents = shareSession.appPackages,
                            currentIndex = shareSession.currentIndex,
                        )
                    } else {
                        MainScreenTopBar(
                            viewModel = viewModel,
                            onSortGroups = { showSortGroupsDialog = true },
                            onHistory = { showHistoryDialog = true },
                            onSettings = { showSettingsDialog = true },
                            onAutoGroup = { requestAutoGroup(true, false) },
                            groupCount = (uiState as? MainUiState.Success)?.groups?.size ?: 0,
                        )
                    }
                },
                snackbarHost = { GroupDeleteSnackbarHost(snackbarHostState) },
                floatingActionButton = {
                    MainScreenHomeFab(
                        uiState = uiState,
                        inShareMode = inShareMode,
                        onAddGroup = { showCreateGroupDialog = true },
                    )
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
                                MainScreenSharingHost(
                                    shareSession = shareSession,
                                    packageManager = packageManager,
                                    viewModel = viewModel,
                                    onNextStep = onNextStep,
                                    onReplayShareStep = onReplayShareStep,
                                    onPreviousShareStep = onPreviousShareStep,
                                    onFinishEarly = onFinishEarly,
                                    onSkipThisApp = onSkipThisApp,
                                    onMarkSuccess = { showSuccessAnimation = true },
                                    onCancelShare = onCancelShare,
                                )
                            } else {
                                when (val state = uiState) {
                                    is MainUiState.Loading -> {
                                        GroupListSkeleton(modifier = Modifier.align(Alignment.TopCenter))
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
                                ShareSuccessAnimation(
                                    modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
                                    skipBurst = rememberSkipSuccessBurst(),
                                    onAnimationEnd = { showSuccessAnimation = false },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
