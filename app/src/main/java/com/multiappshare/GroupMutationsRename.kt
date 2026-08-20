package com.multiappshare

import com.multiappshare.domain.GroupNameHelper
import com.multiappshare.domain.GroupsRepository
import com.multiappshare.model.AppGroup

internal object GroupMutationsRename {

    suspend fun renameGroup(
        state: MainUiState.Success,
        groupsRepository: GroupsRepository,
        group: AppGroup,
        newName: String,
    ): MainUiState.Success? {
        val normalized = GroupNameHelper.normalize(newName)
        val invalid = normalized.isBlank() ||
            GroupNameHelper.isDuplicateExcluding(normalized, state.groups, group.name)
        if (invalid) return null
        val updated = state.copy(
            groups = state.groups.map {
                if (it.name == group.name) it.copy(name = normalized) else it
            },
        )
        groupsRepository.saveGroups(updated.groups)
        return updated
    }

    suspend fun restoreGroup(
        state: MainUiState.Success,
        groupsRepository: GroupsRepository,
        group: AppGroup,
    ): MainUiState.Success? {
        if (GroupNameHelper.isDuplicate(group.name, state.groups)) return null
        val updated = state.copy(groups = state.groups + group)
        groupsRepository.saveGroups(updated.groups)
        return updated
    }
}
