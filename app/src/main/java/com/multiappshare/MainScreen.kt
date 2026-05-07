package com.multiappshare

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.multiappshare.core.ui.ShareSuccessAnimation
import com.multiappshare.model.AppGroup

/**
 * The main screen of the application.
 * Displays the list of groups, onboarding dialog, and managing groups/apps.
 * Also handles the sharing overlay interface when content is passed via Intent.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uris: List<Uri>?,
    text: String?,
    mimeType: String?,
    sharingStarted: Boolean,
    currentIndex: Int,
    appPackages: List<String>?,
    onStartSharing: (AppGroup, MainViewModel) -> Unit,
    onNextStep: () -> Unit,
    onReplayShareStep: () -> Unit = {},
    onPreviousShareStep: () -> Unit = {},
    packageManager: PackageManager,
    onExport: () -> Unit,
    onImport: () -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var showSuccessAnimation by remember { mutableStateOf(false) }
    var showCreateGroupDialog by remember { mutableStateOf(false) }
    var showModifyGroupDialog by remember { mutableStateOf<AppGroup?>(null) }
    var showReorderDialog by remember { mutableStateOf<AppGroup?>(null) }
    var showSortGroupsDialog by remember { mutableStateOf(false) }
    var groupToDelete by remember { mutableStateOf<AppGroup?>(null) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    var groupFilterQuery by remember { mutableStateOf("") }

    val inShareMode = !uris.isNullOrEmpty() || text != null

    val isLowRamDevice = remember(context) {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        am?.isLowRamDevice == true
    }
    val shareBackdropBlur: Dp = if (isLowRamDevice) 0.dp else 20.dp
    val shareBackdropScrimAlpha = if (isLowRamDevice) 0.78f else 0.62f

    viewModel.importPassphrasePendingUri?.let { pendingUri ->
        BackupImportPassphraseDialog(
            onDismiss = { viewModel.dismissImportPassphraseRequest() },
            onConfirm = { chars -> viewModel.importGroupsWithPassphrase(pendingUri, chars) },
        )
    }

    Scaffold(
        topBar = {
            if (!inShareMode) {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.groups_title),
                            maxLines = 2,
                        )
                    },
                    actions = {
                        Box {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.cd_main_menu))
                            }
                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false },
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.menu_sort_groups)) },
                                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.List, null) },
                                    onClick = {
                                        showSortGroupsDialog = true
                                        menuExpanded = false
                                    },
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.menu_history)) },
                                    leadingIcon = { Icon(Icons.Default.Refresh, null) },
                                    onClick = {
                                        showHistoryDialog = true
                                        menuExpanded = false
                                    },
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.menu_about)) },
                                    leadingIcon = { Icon(Icons.Default.Info, null) },
                                    onClick = {
                                        showAboutDialog = true
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
                    },
                )
            }
        },
        floatingActionButton = {
            val state = uiState
            if (!inShareMode && state is MainUiState.Success) {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
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
            color = if (inShareMode) Color.Transparent else Color.Transparent,
            shape = if (inShareMode) MaterialTheme.shapes.large else RectangleShape,
            tonalElevation = if (inShareMode) 8.dp else 0.dp,
        ) {
            if (inShareMode) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(shareBackdropBlur)
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = shareBackdropScrimAlpha)),
                )
            }
            Box(modifier = Modifier.fillMaxSize()) {
                if (inShareMode && sharingStarted && appPackages != null) {
                    SharingInProgress(
                        mimeType = mimeType,
                        text = text,
                        uris = uris,
                        currentIndex = currentIndex,
                        totalApps = appPackages.size,
                        appComponents = appPackages,
                        packageManager = packageManager,
                        onReplayCurrentStep = onReplayShareStep,
                        onPreviousStep = onPreviousShareStep,
                        onNextStep = {
                            if (currentIndex + 1 == appPackages.size) {
                                showSuccessAnimation = true
                            }
                            onNextStep()
                        },
                    )
                } else {
                    when (val state = uiState) {
                        is MainUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        is MainUiState.Success -> {
                            Column {
                                if (inShareMode) {
                                    ElevatedCard(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                            .fillMaxWidth(),
                                        colors = CardDefaults.elevatedCardColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        ),
                                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                stringResource(R.string.share_overlay_title),
                                                style = MaterialTheme.typography.titleLarge,
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                stringResource(R.string.share_overlay_subtitle),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.92f),
                                            )
                                        }
                                    }
                                }

                                if (!inShareMode && state.groups.size > 8) {
                                    OutlinedTextField(
                                        value = groupFilterQuery,
                                        onValueChange = { groupFilterQuery = it },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                            .semantics {
                                                contentDescription = context.getString(R.string.cd_filter_groups)
                                            },
                                        label = { Text(stringResource(R.string.label_filter_groups)) },
                                        singleLine = true,
                                        placeholder = { Text(stringResource(R.string.label_filter_groups)) },
                                    )
                                }

                                if (showCreateGroupDialog) {
                                    CreateGroupDialog(
                                        onDismiss = { showCreateGroupDialog = false },
                                        onCreateGroup = { name ->
                                            viewModel.createGroup(name)
                                            showCreateGroupDialog = false
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
                                        onDismiss = { showModifyGroupDialog = null },
                                        onSaveApps = { apps ->
                                            viewModel.updateGroupApps(group, apps)
                                            showModifyGroupDialog = null
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
                                        onDismiss = { showReorderDialog = null },
                                        onSaveOrder = { apps ->
                                            viewModel.updateGroupApps(group, apps)
                                            showReorderDialog = null
                                        },
                                    )
                                }

                                if (showSortGroupsDialog) {
                                    SortGroupsDialog(
                                        groups = state.groups,
                                        onDismiss = { showSortGroupsDialog = false },
                                        onSaveOrder = { groups ->
                                            viewModel.updateGroupsOrder(groups)
                                            showSortGroupsDialog = false
                                        },
                                    )
                                }

                                groupToDelete?.let { group ->
                                    DeleteGroupDialog(
                                        groupName = group.name,
                                        onDismiss = { groupToDelete = null },
                                        onConfirm = {
                                            viewModel.deleteGroup(group)
                                            groupToDelete = null
                                        },
                                    )
                                }

                                if (showHistoryDialog) HistoryDialog(history = state.history, onDismiss = { showHistoryDialog = false })
                                if (showAboutDialog) AboutDialog(onDismiss = { showAboutDialog = false })

                                val filteredGroups = if (inShareMode) {
                                    val shareAction = if (uris != null && uris.size > 1) {
                                        android.content.Intent.ACTION_SEND_MULTIPLE
                                    } else {
                                        android.content.Intent.ACTION_SEND
                                    }
                                    val compatibleCat = viewModel.getCompatiblePackages(shareAction, mimeType ?: "*/*")
                                    state.groups.filter { group ->
                                        group.apps.any { app ->
                                            val key = "${app.packageName}/${app.activityName}"
                                            val fallbackKey = "${app.packageName}/"
                                            key in compatibleCat || compatibleCat.any { it.startsWith(fallbackKey) }
                                        }
                                    }
                                } else {
                                    state.groups
                                }

                                val displayGroups = if (!inShareMode && state.groups.size > 8) {
                                    filteredGroups.filter { it.name.contains(groupFilterQuery, ignoreCase = true) }
                                } else {
                                    filteredGroups
                                }

                                if (filteredGroups.isEmpty()) {
                                    if (inShareMode) {
                                        Box(
                                            modifier = Modifier.fillMaxSize().padding(16.dp),
                                            contentAlignment = androidx.compose.ui.Alignment.Center,
                                        ) {
                                            Text(
                                                stringResource(R.string.no_compatible_groups_detail, mimeType ?: "*/*"),
                                                style = MaterialTheme.typography.bodyLarge,
                                                textAlign = TextAlign.Center,
                                            )
                                        }
                                    } else {
                                        EmptyGroupsPlaceholder(
                                            onAddGroup = { showCreateGroupDialog = true },
                                            onAutoGroup = { viewModel.autoGroupApps(state.allApps, append = false) },
                                        )
                                    }
                                } else if (displayGroups.isEmpty()) {
                                    Box(
                                        modifier = Modifier.fillMaxSize().padding(16.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            stringResource(R.string.no_groups_match_filter),
                                            style = MaterialTheme.typography.bodyLarge,
                                            textAlign = TextAlign.Center,
                                        )
                                    }
                                } else {
                                    GroupList(
                                        groups = displayGroups,
                                        onModifyClick = { showModifyGroupDialog = it },
                                        onReorderClick = { showReorderDialog = it },
                                        onDeleteClick = { groupToDelete = it },
                                        onToggleExpanded = { viewModel.toggleGroupExpanded(it) },
                                        onGroupClick = { onStartSharing(it, viewModel) },
                                        onAddShortcutClick = { viewModel.createShortcutForGroup(it) },
                                        inShareMode = inShareMode,
                                        packageManager = packageManager,
                                    )
                                }
                            }
                        }
                    }
                }

                if (showSuccessAnimation) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center,
                    ) {
                        ShareSuccessAnimation(onAnimationEnd = { showSuccessAnimation = false })
                    }
                }
            }
        }
    }
}
