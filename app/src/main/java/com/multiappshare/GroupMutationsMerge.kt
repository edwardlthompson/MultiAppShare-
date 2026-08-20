package com.multiappshare

import com.multiappshare.domain.GroupsRepository
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo

internal object GroupMutationsMerge {

    suspend fun mergeGroups(
        state: MainUiState.Success,
        groupsRepository: GroupsRepository,
        target: AppGroup,
        source: AppGroup,
    ): MainUiState.Success? {
        val currentTarget = state.groups.find { it.name == target.name }
        val currentSource = state.groups.find { it.name == source.name }
        val same = target.name.equals(source.name, ignoreCase = true)
        if (same || currentTarget == null || currentSource == null) return null
        val merged = currentTarget.copy(
            apps = unionApps(currentTarget.apps, currentSource.apps),
            usageCount = currentTarget.usageCount + currentSource.usageCount,
        )
        val updated = state.copy(
            groups = state.groups.mapNotNull { group ->
                when (group.name) {
                    target.name -> merged
                    source.name -> null
                    else -> group
                }
            },
        )
        groupsRepository.saveGroups(updated.groups)
        return updated
    }

    internal fun unionApps(target: List<AppInfo>, source: List<AppInfo>): List<AppInfo> {
        val seen = mutableSetOf<Pair<String, String>>()
        val out = mutableListOf<AppInfo>()
        for (app in target + source) {
            if (seen.add(app.packageName to app.activityName)) out.add(app)
        }
        return out
    }
}
