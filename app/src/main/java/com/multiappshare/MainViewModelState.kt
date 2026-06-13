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

    suspend fun applyPendingExpand(applyExpand: suspend (String) -> Unit) {
        pendingExpandGroupName?.let { name ->
            pendingExpandGroupName = null
            applyExpand(name)
        }
    }

    suspend fun applyExpandByName(name: String) {
        mutationMutex.withLock {
            val updated = runGroupMutation(uiState.value) {
                GroupMutations.expandByName(it, groupsRepository, name)
            }
            if (updated != null) uiState.value = updated
        }
    }

    fun expandGroupByNameIfPresent(scope: CoroutineScope, name: String) {
        scope.launch {
            val normalized = GroupNameHelper.normalize(name)
            if (normalized.isEmpty()) return@launch
            if (uiState.value is MainUiState.Loading) {
                pendingExpandGroupName = normalized
                return@launch
            }
            applyExpandByName(normalized)
        }
    }

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
