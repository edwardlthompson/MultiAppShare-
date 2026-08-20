package com.multiappshare

import com.multiappshare.domain.GroupNameHelper
import com.multiappshare.domain.GroupsRepository
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal object GroupMutations {

    suspend fun createGroup(
        state: MainUiState.Success,
        groupsRepository: GroupsRepository,
        groupName: String,
    ): MainUiState.Success? {
        val normalized = GroupNameHelper.normalize(groupName)
        if (normalized.isBlank() || GroupNameHelper.isDuplicate(normalized, state.groups)) {
            return null
        }
        val updated = state.copy(groups = state.groups + AppGroup(name = normalized, apps = emptyList()))
        groupsRepository.saveGroups(updated.groups)
        return updated
    }

    suspend fun deleteGroup(state: MainUiState.Success, groupsRepository: GroupsRepository, group: AppGroup): MainUiState.Success {
        val updated = state.copy(groups = state.groups.filter { it.name != group.name })
        groupsRepository.saveGroups(updated.groups)
        return updated
    }

    suspend fun toggleExpanded(state: MainUiState.Success, groupsRepository: GroupsRepository, group: AppGroup): MainUiState.Success {
        val updated = state.copy(
            groups = state.groups.map {
                if (it.name == group.name) it.copy(isExpanded = !it.isExpanded) else it
            },
        )
        groupsRepository.saveGroups(updated.groups)
        return updated
    }

    suspend fun updateApps(state: MainUiState.Success, groupsRepository: GroupsRepository, group: AppGroup, apps: List<AppInfo>): MainUiState.Success {
        val updated = state.copy(
            groups = state.groups.map { if (it.name == group.name) it.copy(apps = apps) else it },
        )
        groupsRepository.saveGroups(updated.groups)
        return updated
    }

    suspend fun incrementUsage(state: MainUiState.Success, groupsRepository: GroupsRepository, group: AppGroup): MainUiState.Success {
        val sorted = state.groups.map {
            if (it.name == group.name) it.copy(usageCount = it.usageCount + 1) else it
        }.sortedByDescending { it.usageCount }
        val updated = state.copy(groups = sorted)
        groupsRepository.saveGroups(updated.groups)
        return updated
    }

    suspend fun updateOrder(state: MainUiState.Success, groupsRepository: GroupsRepository, groups: List<AppGroup>): MainUiState.Success {
        val updated = state.copy(groups = groups)
        groupsRepository.saveGroups(updated.groups)
        return updated
    }

    suspend fun duplicateGroup(
        state: MainUiState.Success,
        groupsRepository: GroupsRepository,
        group: AppGroup,
    ): MainUiState.Success? {
        val copyName = GroupNameHelper.uniqueCopyName(group.name, state.groups)
        if (GroupNameHelper.isDuplicate(copyName, state.groups)) return null
        val copy = group.copy(name = copyName, isExpanded = false, usageCount = 0)
        val updated = state.copy(groups = state.groups + copy)
        groupsRepository.saveGroups(updated.groups)
        return updated
    }

    suspend fun expandByName(
        state: MainUiState.Success,
        groupsRepository: GroupsRepository,
        name: String,
    ): MainUiState.Success? {
        val group = GroupNameHelper.findGroupByName(name, state.groups)
        if (group == null || group.isExpanded) return null
        val updated = state.copy(
            groups = state.groups.map {
                if (it.name == group.name) it.copy(isExpanded = true) else it
            },
        )
        groupsRepository.saveGroups(updated.groups)
        return updated
    }
}

internal suspend fun runGroupMutation(
    currentState: MainUiState?,
    block: suspend (MainUiState.Success) -> MainUiState.Success?,
): MainUiState.Success? {
    val state = currentState as? MainUiState.Success ?: return null
    return withContext(Dispatchers.IO) { block(state) }
}

internal suspend fun runGroupMutationNonNull(
    currentState: MainUiState?,
    block: suspend (MainUiState.Success) -> MainUiState.Success,
): MainUiState.Success? {
    val state = currentState as? MainUiState.Success ?: return null
    return withContext(Dispatchers.IO) { block(state) }
}
