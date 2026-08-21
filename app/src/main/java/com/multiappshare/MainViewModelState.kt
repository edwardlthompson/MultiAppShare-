package com.multiappshare

import com.multiappshare.domain.GroupNameHelper
import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.HistoryRepository
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import com.multiappshare.model.HistoryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class MainViewModelState(
    private val groupsRepository: GroupsRepository,
    private val historyRepository: HistoryRepository,
    private val uiState: MutableStateFlow<MainUiState>,
    private val mutationMutex: Mutex,
) {
    var pendingExpandGroupName: String? = null
    var pendingExpandGroupId: String? = null

    suspend fun applyPendingExpand() {
        val id = pendingExpandGroupId
        val name = pendingExpandGroupName
        pendingExpandGroupId = null
        pendingExpandGroupName = null
        if (id != null || name != null) applyExpand(id, name)
    }

    suspend fun applyExpand(id: String?, name: String?) {
        mutationMutex.withLock {
            val updated = runGroupMutation(uiState.value) {
                GroupMutations.expandByIdOrName(it, groupsRepository, id, name)
            }
            if (updated != null) uiState.value = updated
        }
    }

    fun expandGroupIfPresent(scope: CoroutineScope, id: String?, name: String?) {
        scope.launch {
            val normalized = name?.let { GroupNameHelper.normalize(it) }.orEmpty()
            val trimmedId = id?.trim().orEmpty()
            if (trimmedId.isEmpty() && normalized.isEmpty()) return@launch
            if (uiState.value is MainUiState.Loading) {
                pendingExpandGroupId = trimmedId.takeIf { it.isNotEmpty() }
                pendingExpandGroupName = normalized.takeIf { it.isNotEmpty() }
                return@launch
            }
            applyExpand(trimmedId.takeIf { it.isNotEmpty() }, normalized.takeIf { it.isNotEmpty() })
        }
    }

    fun expandGroupByNameIfPresent(scope: CoroutineScope, name: String) =
        expandGroupIfPresent(scope, id = null, name = name)

    suspend fun runMutation(
        block: suspend (MainUiState.Success) -> MainUiState.Success?,
        onResult: (Boolean) -> Unit = {},
    ) {
        mutationMutex.withLock {
            val updated = runGroupMutation(uiState.value) { block(it) }
            if (updated != null) {
                uiState.value = updated
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    suspend fun runMutationNonNull(block: suspend (MainUiState.Success) -> MainUiState.Success) {
        mutationMutex.withLock {
            val updated = runGroupMutationNonNull(uiState.value) { block(it) }
            if (updated != null) uiState.value = updated
        }
    }

    fun createGroup(scope: CoroutineScope, groupName: String, onResult: (Boolean) -> Unit = {}) {
        scope.launch {
            runMutation({ GroupMutations.createGroup(it, groupsRepository, groupName) }, onResult)
        }
    }

    fun duplicateGroup(scope: CoroutineScope, group: AppGroup, onResult: (Boolean) -> Unit = {}) {
        scope.launch {
            runMutation({ GroupMutations.duplicateGroup(it, groupsRepository, group) }, onResult)
        }
    }

    fun renameGroup(scope: CoroutineScope, group: AppGroup, newName: String, onResult: (Boolean) -> Unit = {}) {
        scope.launch {
            runMutation({ GroupMutationsRename.renameGroup(it, groupsRepository, group, newName) }, onResult)
        }
    }

    fun mergeGroups(
        scope: CoroutineScope,
        target: AppGroup,
        source: AppGroup,
        onResult: (Boolean) -> Unit = {},
    ) {
        scope.launch {
            runMutation({ GroupMutationsMerge.mergeGroups(it, groupsRepository, target, source) }, onResult)
        }
    }

    fun restoreGroup(scope: CoroutineScope, group: AppGroup, onResult: (Boolean) -> Unit = {}) {
        scope.launch {
            runMutation({ GroupMutationsRename.restoreGroup(it, groupsRepository, group) }, onResult)
        }
    }

    fun deleteGroup(scope: CoroutineScope, group: AppGroup) {
        scope.launch { runMutationNonNull { GroupMutations.deleteGroup(it, groupsRepository, group) } }
    }

    fun toggleGroupExpanded(scope: CoroutineScope, group: AppGroup) {
        scope.launch { runMutationNonNull { GroupMutations.toggleExpanded(it, groupsRepository, group) } }
    }

    fun updateGroupApps(scope: CoroutineScope, group: AppGroup, apps: List<AppInfo>) {
        scope.launch { runMutationNonNull { GroupMutations.updateApps(it, groupsRepository, group, apps) } }
    }

    fun incrementGroupUsage(scope: CoroutineScope, group: AppGroup) {
        scope.launch { runMutationNonNull { GroupMutations.incrementUsage(it, groupsRepository, group) } }
    }

    fun updateGroupsOrder(scope: CoroutineScope, groups: List<AppGroup>) {
        scope.launch { runMutationNonNull { GroupMutations.updateOrder(it, groupsRepository, groups) } }
    }

    fun addHistoryItem(scope: CoroutineScope, item: HistoryItem) {
        scope.launch(Dispatchers.IO) { persistHistoryItem(item) }
    }

    private suspend fun persistHistoryItem(item: HistoryItem) {
        mutationMutex.withLock {
            val currentState = uiState.value as? MainUiState.Success ?: return
            val updatedHistory = (listOf(item) + currentState.history).take(50)
            historyRepository.saveHistory(updatedHistory)
            uiState.value = currentState.copy(history = updatedHistory)
        }
    }
}
