package com.multiappshare.ui.groups

import android.content.pm.PackageManager
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.multiappshare.R
import com.multiappshare.domain.DesktopWindowingHelper
import com.multiappshare.domain.WindowSizeClass
import com.multiappshare.model.AppGroup
import com.multiappshare.ui.theme.Dimens

@Composable
fun GroupWorkspace(
    groups: List<AppGroup>,
    selectedGroup: AppGroup?,
    onSelectGroup: (AppGroup) -> Unit,
    onModifyClick: (AppGroup) -> Unit,
    onReorderClick: (AppGroup) -> Unit,
    onDuplicateClick: (AppGroup) -> Unit,
    onRenameClick: (AppGroup) -> Unit = {},
    onMergeClick: (AppGroup) -> Unit = {},
    onDeleteClick: (AppGroup) -> Unit,
    onToggleExpanded: (AppGroup) -> Unit,
    onGroupClick: (AppGroup) -> Unit,
    onAddShortcutClick: (AppGroup) -> Unit,
    onPinClick: (AppGroup) -> Unit = {},
    pinnedGroupNames: Set<String> = emptySet(),
    lastSharedByGroup: Map<String, Long> = emptyMap(),
    inShareMode: Boolean,
    packageManager: PackageManager,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        val sizeClass = DesktopWindowingHelper.classifyWindowSize(maxWidth.value.toInt())
        val twoPane = !inShareMode && sizeClass != WindowSizeClass.COMPACT
        val maxContent = DesktopWindowingHelper.overlayMaxContentWidthDp(sizeClass).dp
        val contentMod = Modifier.fillMaxSize().widthIn(max = maxContent)
        if (!twoPane) {
            Column(modifier = contentMod) {
                GroupList(
                    groups = groups,
                    selectedGroupName = null,
                    onSelectClick = null,
                    onModifyClick = onModifyClick,
                    onReorderClick = onReorderClick,
                    onDuplicateClick = onDuplicateClick,
                    onRenameClick = onRenameClick,
                    onMergeClick = onMergeClick,
                    onDeleteClick = onDeleteClick,
                    onToggleExpanded = onToggleExpanded,
                    onGroupClick = onGroupClick,
                    onAddShortcutClick = onAddShortcutClick,
                    onPinClick = onPinClick,
                    pinnedGroupNames = pinnedGroupNames,
                    lastSharedByGroup = lastSharedByGroup,
                    inShareMode = inShareMode,
                    packageManager = packageManager,
                )
            }
            return@BoxWithConstraints
        }
        Row(modifier = contentMod) {
            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                GroupList(
                    groups = groups,
                    selectedGroupName = selectedGroup?.name,
                    onSelectClick = onSelectGroup,
                    onModifyClick = onModifyClick,
                    onReorderClick = onReorderClick,
                    onDuplicateClick = onDuplicateClick,
                    onRenameClick = onRenameClick,
                    onMergeClick = onMergeClick,
                    onDeleteClick = onDeleteClick,
                    onToggleExpanded = onToggleExpanded,
                    onGroupClick = onGroupClick,
                    onAddShortcutClick = onAddShortcutClick,
                    onPinClick = onPinClick,
                    pinnedGroupNames = pinnedGroupNames,
                    lastSharedByGroup = lastSharedByGroup,
                    inShareMode = false,
                    packageManager = packageManager,
                )
            }
            GroupDetailPane(
                group = selectedGroup,
                lastSharedMillis = selectedGroup?.let { lastSharedByGroup[it.name] },
                onModifyClick = onModifyClick,
                onDeleteClick = onDeleteClick,
                packageManager = packageManager,
                modifier = Modifier.weight(1f).fillMaxHeight().padding(Dimens.Space4),
            )
        }
    }
}

@Composable
internal fun GroupDetailPane(
    group: AppGroup?,
    lastSharedMillis: Long?,
    onModifyClick: (AppGroup) -> Unit,
    onDeleteClick: (AppGroup) -> Unit,
    packageManager: PackageManager,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (group == null) {
            Text(stringResource(R.string.tablet_select_group), style = MaterialTheme.typography.bodyLarge)
            return
        }
        Text(group.name, style = MaterialTheme.typography.titleLarge)
        GroupRowSubtitle(appCount = group.apps.size, lastSharedMillis = lastSharedMillis)
        if (group.apps.isEmpty()) {
            Text(
                stringResource(R.string.group_empty_hint),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = Dimens.Space2),
            )
            Spacer(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth().padding(top = Dimens.Space2)) {
                itemsIndexed(group.apps, key = { _, app -> app.packageName + "/" + app.activityName }) { index, app ->
                    AppListItem(app = app, packageManager = packageManager)
                    if (index < group.apps.lastIndex) HorizontalDivider()
                }
            }
        }
        Button(onClick = { onModifyClick(group) }, modifier = Modifier.padding(top = Dimens.Space3).fillMaxWidth()) {
            Text(stringResource(R.string.menu_modify_apps))
        }
        TextButton(
            onClick = { onDeleteClick(group) },
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
        ) {
            Text(stringResource(R.string.menu_delete_group))
        }
    }
}
