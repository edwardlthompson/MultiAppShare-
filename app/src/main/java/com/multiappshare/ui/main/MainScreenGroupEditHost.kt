package com.multiappshare.ui.main

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.multiappshare.MainViewModel
import com.multiappshare.R
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
    val context = LocalContext.current
    val duplicateToast = stringResource(R.string.toast_duplicate_group_name)
    groupToRename?.let { group ->
        RenameGroupDialog(
            group = group,
            onDismiss = { onGroupToRename(null) },
            onRename = { newName ->
                viewModel.renameGroup(group, newName) { ok ->
                    if (ok) {
                        onRenamed(group, newName.trim())
                        onGroupToRename(null)
                    } else {
                        Toast.makeText(context, duplicateToast, Toast.LENGTH_SHORT).show()
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
            onDismiss = { onGroupToMerge(null) },
            onMergeInto = { target ->
                viewModel.mergeGroups(target, source) { ok ->
                    if (ok) {
                        onMerged(source.name)
                        onGroupToMerge(null)
                    } else {
                        Toast.makeText(context, duplicateToast, Toast.LENGTH_SHORT).show()
                    }
                }
            },
        )
    }
}
