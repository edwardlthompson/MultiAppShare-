package com.multiappshare.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.multiappshare.MainViewModel
import com.multiappshare.R
import com.multiappshare.domain.InlineFieldError
import com.multiappshare.model.AppGroup
import com.multiappshare.ui.groups.MergeGroupDialog
import com.multiappshare.ui.groups.RenameGroupDialog

@Composable
internal fun MainScreenGroupEditHost(
    viewModel: MainViewModel,
    allGroups: List<AppGroup>,
    groupToRename: AppGroup?,
    onGroupToRename: (AppGroup?) -> Unit,
    groupToMerge: AppGroup?,
    onGroupToMerge: (AppGroup?) -> Unit,
    onRenamed: (AppGroup, String) -> Unit,
    onMerged: (sourceName: String) -> Unit,
) {
    val duplicateMessage = stringResource(R.string.toast_duplicate_group_name)
    var renameError by remember { mutableStateOf<String?>(null) }
    var mergeError by remember { mutableStateOf<String?>(null) }
    groupToRename?.let { group ->
        RenameGroupDialog(
            group = group,
            onDismiss = {
                renameError = null
                onGroupToRename(null)
            },
            nameError = renameError,
            onNameEdited = { renameError = null },
            onRename = { newName, notes ->
                viewModel.renameGroup(group, newName, notes) { ok ->
                    renameError = InlineFieldError.of(ok, duplicateMessage)
                    if (ok) {
                        onRenamed(group, newName.trim())
                        onGroupToRename(null)
                    }
                }
            },
        )
    }
    groupToMerge?.let { source ->
        val targets = allGroups.filter { it.name != source.name }
        if (targets.isEmpty()) return@let
        MergeGroupDialog(
            source = source,
            targets = targets,
            onDismiss = {
                mergeError = null
                onGroupToMerge(null)
            },
            errorText = mergeError,
            onMergeInto = { target ->
                viewModel.mergeGroups(target, source) { ok ->
                    mergeError = InlineFieldError.of(ok, duplicateMessage)
                    if (ok) {
                        onMerged(source.name)
                        onGroupToMerge(null)
                    }
                }
            },
        )
    }
}
