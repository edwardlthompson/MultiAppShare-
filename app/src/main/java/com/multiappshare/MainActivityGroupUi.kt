package com.multiappshare

import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.activity.compose.BackHandler
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import kotlinx.coroutines.launch
/**
 * Displays the progress of the sequential sharing operation.
 * Guides the user through sharing items iteratively to apps in a group.
 */
@Composable
fun SharingInProgress(
    mimeType: String?,
    text: String?,
    uris: List<Uri>?,
    currentIndex: Int,
    totalApps: Int,
    appComponents: List<String>,
    packageManager: PackageManager,
    onReplayCurrentStep: () -> Unit = {},
    onPreviousStep: () -> Unit = {},
    onNextStep: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val currentKey = appComponents.getOrNull(currentIndex).orEmpty()
    val currentLabel = remember(currentIndex, currentKey, appComponents) {
        resolveShareTargetLabel(packageManager, currentKey)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val contentNoun = when {
            mimeType?.startsWith("image/") == true ->
                if (uris != null && uris.size > 1) stringResource(R.string.sharing_content_photos_n, uris.size)
                else stringResource(R.string.sharing_content_photo)
            mimeType?.startsWith("video/") == true ->
                if (uris != null && uris.size > 1) stringResource(R.string.sharing_content_videos_n, uris.size)
                else stringResource(R.string.sharing_content_video)
            text != null && uris.isNullOrEmpty() -> stringResource(R.string.sharing_content_text)
            else ->
                if (uris != null && uris.size > 1) stringResource(R.string.sharing_content_media_n, uris.size)
                else stringResource(R.string.sharing_content_media)
        }
        Text(
            stringResource(R.string.sharing_headline_format, contentNoun),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(R.string.sharing_step_format, currentIndex + 1, totalApps),
            style = MaterialTheme.typography.titleMedium,
        )
        if (currentLabel.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                stringResource(R.string.sharing_next_app_format, currentLabel),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            stringResource(R.string.sharing_preview_hint),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            stringResource(R.string.sharing_return_instruction),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(
                onClick = onReplayCurrentStep,
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 48.dp),
            ) {
                Text(stringResource(R.string.sharing_replay_current), textAlign = TextAlign.Center)
            }
            OutlinedButton(
                onClick = onPreviousStep,
                enabled = currentIndex > 0,
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 48.dp),
            ) {
                Text(stringResource(R.string.sharing_previous), textAlign = TextAlign.Center)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                if (currentIndex + 1 >= totalApps) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
                onNextStep()
            },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp),
        ) {
            Text(
                if (currentIndex + 1 < totalApps) {
                    stringResource(R.string.sharing_button_next)
                } else {
                    stringResource(R.string.sharing_button_finish)
                },
            )
        }
    }
}

@Composable
fun GroupList(
    groups: List<AppGroup>,
    onModifyClick: (AppGroup) -> Unit,
    onReorderClick: (AppGroup) -> Unit,
    onDeleteClick: (AppGroup) -> Unit,
    onToggleExpanded: (AppGroup) -> Unit,
    onGroupClick: (AppGroup) -> Unit,
    onAddShortcutClick: (AppGroup) -> Unit,
    inShareMode: Boolean,
    packageManager: PackageManager
) {
    LazyColumn {
        items(groups) { group ->
            GroupItem(
                group = group,
                onModifyClick = { onModifyClick(group) },
                onReorderClick = { onReorderClick(group) },
                onDeleteClick = { onDeleteClick(group) },
                onToggleExpanded = { onToggleExpanded(group) },
                onGroupClick = { onGroupClick(group) },
                onAddShortcutClick = { onAddShortcutClick(group) },
                inShareMode = inShareMode,
                packageManager = packageManager
            )
        }
    }
}

@Composable
fun GroupItem(
    group: AppGroup,
    onModifyClick: () -> Unit,
    onReorderClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onToggleExpanded: () -> Unit,
    onGroupClick: () -> Unit,
    onAddShortcutClick: () -> Unit,
    inShareMode: Boolean,
    packageManager: PackageManager
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth().clickable(enabled = inShareMode, onClick = onGroupClick),
        colors = if (inShareMode) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer) else CardDefaults.cardColors()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!inShareMode) {
                    Icon(
                        imageVector = Icons.Default.DragHandle,
                        contentDescription = stringResource(R.string.cd_reorder_share_order_hint),
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(22.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = onToggleExpanded) {
                    Icon(
                        imageVector = if (group.isExpanded && !inShareMode) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.cd_toggle_group),
                    )
                }
                Text(text = group.name, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                if (!inShareMode) {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.cd_more_options))
                        }
                        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                            DropdownMenuItem(text = { Text(stringResource(R.string.menu_modify_apps)) }, onClick = { menuExpanded = false; onModifyClick() })
                            DropdownMenuItem(text = { Text(stringResource(R.string.menu_reorder_apps)) }, onClick = { menuExpanded = false; onReorderClick() })
                            DropdownMenuItem(text = { Text(stringResource(R.string.menu_add_home_shortcut)) }, onClick = { menuExpanded = false; onAddShortcutClick() })
                            DropdownMenuItem(text = { Text(stringResource(R.string.menu_delete_group), color = MaterialTheme.colorScheme.error) }, onClick = { menuExpanded = false; onDeleteClick() })
                        }
                    }
                }
            }
            
            AnimatedVisibility(visible = group.isExpanded && !inShareMode) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    if (group.apps.isEmpty()) {
                        Text(text = stringResource(R.string.group_empty_hint), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 8.dp))
                    } else {
                        group.apps.forEachIndexed { index, app ->
                            AppListItem(app = app, packageManager = packageManager)
                            if (index < group.apps.size - 1) HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppListItem(app: AppInfo, packageManager: PackageManager) {
    val placeholderColor = remember(app.packageName) {
        val hash = app.packageName.hashCode()
        Color(
            red = ((hash shr 16) and 0xFF) / 255f,
            green = ((hash shr 8) and 0xFF) / 255f,
            blue = (hash and 0xFF) / 255f,
            alpha = 1f
        )
    }

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        SubcomposeAsyncImage(
            model = app,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        ) {
            if (painter.state is AsyncImagePainter.State.Loading) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(placeholderColor, shape = CircleShape)
                )
            } else {
                SubcomposeAsyncImageContent()
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = app.appName)
    }
}

@Composable
fun CreateGroupDialog(onDismiss: () -> Unit, onCreateGroup: (String) -> Unit) {
    var groupName by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_create_group_title)) },
        text = {
            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text(stringResource(R.string.label_group_name)) },
                singleLine = true,
            )
        },
        confirmButton = {
            Button(onClick = { if (groupName.isNotBlank()) onCreateGroup(groupName) }) { Text(stringResource(R.string.button_create)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) } },
    )
}

@Composable
fun ModifyGroupAppsDialog(
    allApps: List<AppInfo>,
    group: AppGroup,
    onDismiss: () -> Unit,
    onSaveApps: (List<AppInfo>) -> Unit,
    onRefresh: () -> Unit,
    packageManager: PackageManager
) {
    val selectedApps = remember { mutableStateListOf<AppInfo>().apply { addAll(group.apps) } }
    var searchQuery by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.modify_group_title, group.name), modifier = Modifier.weight(1f))
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.cd_refresh_app_list))
                }
            }
        },
        text = {
            Column {
                if (selectedApps.isNotEmpty()) {
                    androidx.compose.foundation.lazy.LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)
                    ) {
                        items(selectedApps) { app ->
                            AssistChip(
                                onClick = { selectedApps.remove(app) },
                                label = { Text(app.appName.split(" - ").first()) },
                                leadingIcon = { Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp)) }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(stringResource(R.string.label_search_apps)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.height(300.dp)) {
                    val filteredApps = allApps.filter { it.appName.contains(searchQuery, ignoreCase = true) }
                    items(filteredApps) { app ->
                        val isSelected = selectedApps.any { it.packageName == app.packageName && it.activityName == app.activityName }
                        Row(modifier = Modifier.fillMaxWidth().clickable {
                            if (isSelected) {
                                selectedApps.removeAll { it.packageName == app.packageName && it.activityName == app.activityName }
                            } else {
                                selectedApps.add(app)
                            }
                        }.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(model = app, contentDescription = null, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = app.appName, modifier = Modifier.weight(1f))
                            Checkbox(checked = isSelected, onCheckedChange = null)
                        }
                    }
                }
            }
        },
        confirmButton = { Button(onClick = { onSaveApps(selectedApps.toList()) }) { Text(stringResource(R.string.button_save)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) } },
    )
}

@Composable
fun ReorderAppsDialog(
    group: AppGroup,
    onDismiss: () -> Unit,
    onSaveOrder: (List<AppInfo>) -> Unit
) {
    val apps = remember { mutableStateListOf<AppInfo>().apply { addAll(group.apps) } }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_reorder_apps_title)) },
        text = {
            LazyColumn(modifier = Modifier.height(400.dp)) {
                itemsIndexed(apps, key = { _, app -> app.packageName + "/" + app.activityName }) { index, app ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            IconButton(
                                onClick = {
                                    if (index > 0) {
                                        val item = apps.removeAt(index)
                                        apps.add(index - 1, item)
                                    }
                                },
                                enabled = index > 0,
                                modifier = Modifier.size(36.dp),
                            ) {
                                Icon(Icons.Default.KeyboardArrowUp, contentDescription = stringResource(R.string.cd_move_up))
                            }
                            IconButton(
                                onClick = {
                                    if (index < apps.size - 1) {
                                        val item = apps.removeAt(index)
                                        apps.add(index + 1, item)
                                    }
                                },
                                enabled = index < apps.size - 1,
                                modifier = Modifier.size(36.dp),
                            ) {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = stringResource(R.string.cd_move_down))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = app.appName, modifier = Modifier.weight(1f))
                    }
                    if (index < apps.size - 1) HorizontalDivider()
                }
            }
        },
        confirmButton = { Button(onClick = { onSaveOrder(apps.toList()) }) { Text(stringResource(R.string.button_save_order)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) } },
    )
}

@Composable
fun SortGroupsDialog(
    groups: List<AppGroup>,
    onDismiss: () -> Unit,
    onSaveOrder: (List<AppGroup>) -> Unit
) {
    val sortedGroups = remember { mutableStateListOf<AppGroup>().apply { addAll(groups) } }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_sort_groups_title)) },
        text = {
            LazyColumn(modifier = Modifier.height(400.dp)) {
                itemsIndexed(sortedGroups, key = { _, group -> group.name }) { index, group ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            IconButton(
                                onClick = {
                                    if (index > 0) {
                                        val item = sortedGroups.removeAt(index)
                                        sortedGroups.add(index - 1, item)
                                    }
                                },
                                enabled = index > 0,
                                modifier = Modifier.size(36.dp),
                            ) {
                                Icon(Icons.Default.KeyboardArrowUp, contentDescription = stringResource(R.string.cd_move_up))
                            }
                            IconButton(
                                onClick = {
                                    if (index < sortedGroups.size - 1) {
                                        val item = sortedGroups.removeAt(index)
                                        sortedGroups.add(index + 1, item)
                                    }
                                },
                                enabled = index < sortedGroups.size - 1,
                                modifier = Modifier.size(36.dp),
                            ) {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = stringResource(R.string.cd_move_down))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = group.name, modifier = Modifier.weight(1f))
                    }
                    if (index < sortedGroups.size - 1) HorizontalDivider()
                }
            }
        },
        confirmButton = { Button(onClick = { onSaveOrder(sortedGroups.toList()) }) { Text(stringResource(R.string.button_save_order)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.button_cancel)) } },
    )
}

@Composable
fun OnboardingDialog(onAutofill: () -> Unit, onManual: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = pagerState.currentPage > 0) {
        val prev = pagerState.currentPage - 1
        coroutineScope.launch { pagerState.animateScrollToPage(prev) }
    }

    Dialog(
        onDismissRequest = { /* Force action */ },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                ),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(3f)
                ) { page ->
                    when (page) {
                        0 -> OnboardingPage(
                            title = stringResource(R.string.onboarding_title_welcome),
                            description = stringResource(R.string.onboarding_desc_welcome),
                            icon = Icons.Default.Share,
                        )
                        1 -> OnboardingPage(
                            title = stringResource(R.string.onboarding_title_smart),
                            description = stringResource(R.string.onboarding_desc_smart),
                            icon = Icons.Default.AutoAwesome,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Page indicators
                Row {
                    repeat(2) { index ->
                        val color = if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        Box(modifier = Modifier.padding(4.dp).size(8.dp).background(color, androidx.compose.foundation.shape.CircleShape))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (pagerState.currentPage == 0) {
                    Button(
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.onboarding_next))
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = onAutofill, modifier = Modifier.fillMaxWidth()) {
                            Text(stringResource(R.string.onboarding_autofill))
                        }
                        TextButton(onClick = onManual, modifier = Modifier.fillMaxWidth()) {
                            Text(stringResource(R.string.onboarding_manual))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun OnboardingPage(title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = description, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
