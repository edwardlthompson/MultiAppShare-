package com.multiappshare.ui.groups

import android.content.pm.PackageManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.multiappshare.R
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo

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
) {
    LazyColumn {
        items(groups, key = { it.name }) { group ->
            GroupItem(
                group = group,
                onModifyClick = { onModifyClick(group) },
                onReorderClick = { onReorderClick(group) },
                onDeleteClick = { onDeleteClick(group) },
                onToggleExpanded = { onToggleExpanded(group) },
                onGroupClick = { onGroupClick(group) },
                onAddShortcutClick = { onAddShortcutClick(group) },
                inShareMode = inShareMode,
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
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth().clickable(enabled = inShareMode, onClick = onGroupClick),
        colors = if (inShareMode) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        } else {
            CardDefaults.cardColors()
        },
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
                        imageVector = if (group.isExpanded && !inShareMode) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = stringResource(R.string.cd_toggle_group),
                    )
                }
                Text(
                    text = group.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f),
                )
                if (!inShareMode) {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.cd_more_options))
                        }
                        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.menu_modify_apps)) },
                                onClick = { menuExpanded = false; onModifyClick() },
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.menu_reorder_apps)) },
                                onClick = { menuExpanded = false; onReorderClick() },
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.menu_add_home_shortcut)) },
                                onClick = { menuExpanded = false; onAddShortcutClick() },
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.menu_delete_group), color = MaterialTheme.colorScheme.error) },
                                onClick = { menuExpanded = false; onDeleteClick() },
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = group.isExpanded && !inShareMode) {
                Column {
                    Spacer(modifier = Modifier.size(8.dp))
                    if (group.apps.isEmpty()) {
                        Text(
                            text = stringResource(R.string.group_empty_hint),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(vertical = 8.dp),
                        )
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
fun AppListItem(app: AppInfo, @Suppress("UNUSED_PARAMETER") packageManager: PackageManager) {
    val placeholderColor = remember(app.packageName) {
        val hash = app.packageName.hashCode()
        Color(
            red = ((hash shr 16) and 0xFF) / 255f,
            green = ((hash shr 8) and 0xFF) / 255f,
            blue = (hash and 0xFF) / 255f,
            alpha = 1f,
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SubcomposeAsyncImage(
            model = app,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
        ) {
            if (painter.state is AsyncImagePainter.State.Loading) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(placeholderColor, shape = CircleShape),
                )
            } else {
                SubcomposeAsyncImageContent()
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = app.appName)
    }
}
