package com.multiappshare

import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.HistoryRepository
import com.multiappshare.domain.SettingsRepository
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import com.multiappshare.model.HistoryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class MainViewModelState(
    private val groupsRepository: GroupsRepository,
    private val historyRepository: HistoryRepository,
    private val settingsRepository: SettingsRepository,
    private val uiState: MutableStateFlow<MainUiState>,
    private val mutationMutex: Mutex,
    onDeepLinkMissing: (String) -> Unit = {},
) {
    private val deepLink = MainDeepLinkExpand(
        groupsRepository,
        uiState,
        mutationMutex,
        onDeepLinkMissing,
    )
    suspend fun applyPendingExpand() = deepLink.applyPendingExpand()
    fun expandGroupIfPresent(scope: CoroutineScope, id: String?, name: String?) =
        deepLink.expandGroupIfPresent(scope, id, name)
    fun expandGroupByNameIfPresent(scope: CoroutineScope, name: String) =
        deepLink.expandGroupByNameIfPresent(scope, name)

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
        scope.launch { runMutation({ GroupMutations.createGroup(it, groupsRepository, groupName) }, onResult) }
    }

    fun duplicateGroup(
        scope: CoroutineScope,
        group: AppGroup,
        copyLabel: String,
        onResult: (Boolean) -> Unit = {},
    ) {
        scope.launch {
            runMutation({ GroupMutations.duplicateGroup(it, groupsRepository, group, copyLabel) }, onResult)
        }
    }

    fun renameGroup(
        scope: CoroutineScope,
        group: AppGroup,
        newName: String,
        notes: String,
        onResult: (Boolean) -> Unit = {},
    ) {
        scope.launch {
            runMutation(
                { GroupMutationsRename.renameGroup(it, groupsRepository, group, newName, notes) },
                onResult,
            )
        }
    }

    fun mergeGroups(scope: CoroutineScope, target: AppGroup, source: AppGroup, onResult: (Boolean) -> Unit = {}) {
        scope.launch {
            runMutation(
                { GroupMutationsMerge.mergeGroups(it, groupsRepository, target, source) },
                onResult,
            )
        }
    }

    fun restoreGroup(scope: CoroutineScope, group: AppGroup, onResult: (Boolean) -> Unit = {}) {
        scope.launch { runMutation({ GroupMutationsRename.restoreGroup(it, groupsRepository, group) }, onResult) }
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
        scope.launch {
            val pinned = settingsRepository.pinnedGroupNames.first()
            runMutationNonNull { GroupMutations.incrementUsage(it, groupsRepository, group, pinned) }
        }
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
