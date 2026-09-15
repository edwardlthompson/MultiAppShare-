package com.multiappshare.ui.groups

import android.content.pm.PackageManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.multiappshare.R
import com.multiappshare.core.ui.highRefreshScroll
import com.multiappshare.domain.GroupCardTap
import com.multiappshare.domain.ListFabPadding
import com.multiappshare.domain.OverlayDestinationChips
import com.multiappshare.model.AppGroup

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
    packageManager: PackageManager,
    selectedGroupName: String? = null,
    onSelectClick: ((AppGroup) -> Unit)? = null,
    onDuplicateClick: (AppGroup) -> Unit = {},
    onRenameClick: (AppGroup) -> Unit = {},
    onMergeClick: (AppGroup) -> Unit = {},
    onPinClick: (AppGroup) -> Unit = {},
    pinnedGroupNames: Set<String> = emptySet(),
    lastSharedByGroup: Map<String, Long> = emptyMap(),
) {
    val tap = GroupCardTap.action(inShareMode, onSelectClick != null)
    LazyColumn(
        modifier = Modifier.highRefreshScroll(),
        contentPadding = PaddingValues(bottom = ListFabPadding.bottomDp(inShareMode).dp),
    ) {
        items(groups, key = { it.name }) { group ->
            GroupItem(
                group = group,
                selected = group.name == selectedGroupName,
                onModifyClick = { onModifyClick(group) },
                onReorderClick = { onReorderClick(group) },
                onDuplicateClick = { onDuplicateClick(group) },
                onRenameClick = { onRenameClick(group) },
                onMergeClick = { onMergeClick(group) },
                onPinClick = { onPinClick(group) },
                pinned = group.name in pinnedGroupNames,
                lastSharedMillis = lastSharedByGroup[group.name],
                onDeleteClick = { onDeleteClick(group) },
                onToggleExpanded = { onToggleExpanded(group) },
                onGroupClick = {
                    when (tap) {
                        GroupCardTap.Action.SHARE -> onGroupClick(group)
                        GroupCardTap.Action.SELECT -> onSelectClick?.invoke(group)
                        GroupCardTap.Action.TOGGLE_EXPAND -> onToggleExpanded(group)
                    }
                },
                onAddShortcutClick = { onAddShortcutClick(group) },
                inShareMode = inShareMode,
                selectable = true,
                packageManager = packageManager,
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
    packageManager: PackageManager,
    selected: Boolean = false,
    onDuplicateClick: () -> Unit = {},
    onRenameClick: () -> Unit = {},
    onMergeClick: () -> Unit = {},
    onPinClick: () -> Unit = {},
    pinned: Boolean = false,
    lastSharedMillis: Long? = null,
    selectable: Boolean = inShareMode,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth().clickable(enabled = selectable, onClick = onGroupClick),
        colors = when {
            selected -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            inShareMode -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
            else -> CardDefaults.cardColors()
        },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToggleExpanded) {
                    Icon(
                        imageVector = if (group.isExpanded && !inShareMode) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = stringResource(
                            if (group.isExpanded && !inShareMode) {
                                R.string.cd_collapse_group
                            } else {
                                R.string.cd_expand_group
                            },
                            group.name,
                        ),
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = group.name,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    GroupRowSubtitle(appCount = group.apps.size, lastSharedMillis = lastSharedMillis)
                    if (com.multiappshare.domain.GroupNotesHelper.hasNotes(group.notes)) {
                        Text(
                            text = group.notes,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (group.apps.isEmpty() && !inShareMode) {
                        GroupAddAppsButton(onClick = onModifyClick)
                    }
                }
                if (!inShareMode) {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.cd_more_options))
                        }
                        GroupItemOverflowMenu(
                            expanded = menuExpanded,
                            pinned = pinned,
                            onDismiss = { menuExpanded = false },
                            onModifyClick = onModifyClick,
                            onReorderClick = onReorderClick,
                            onDuplicateClick = onDuplicateClick,
                            onRenameClick = onRenameClick,
                            onMergeClick = onMergeClick,
                            onPinClick = onPinClick,
                            onAddShortcutClick = onAddShortcutClick,
                            onDeleteClick = onDeleteClick,
                        )
                    }
                }
            }

            if (OverlayDestinationChips.shouldShow(inShareMode, group.apps.size)) {
                OverlayAppChips(apps = group.apps, packageManager = packageManager)
            }

            AnimatedVisibility(visible = group.isExpanded && !inShareMode && group.apps.isNotEmpty()) {
                Column {
                    Spacer(modifier = Modifier.size(8.dp))
                    group.apps.forEachIndexed { index, app ->
                        AppListItem(app = app, packageManager = packageManager)
                        if (index < group.apps.size - 1) HorizontalDivider()
                    }
                }
            }
        }
    }
}
