package com.multiappshare

import com.multiappshare.domain.DeepLinkGroupLookup
import com.multiappshare.domain.GroupNameHelper
import com.multiappshare.domain.GroupsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class MainDeepLinkExpand(
    private val groupsRepository: GroupsRepository,
    private val uiState: MutableStateFlow<MainUiState>,
    private val mutationMutex: Mutex,
    private val onDeepLinkMissing: (String) -> Unit,
) {
    private var pendingExpandGroupName: String? = null
    private var pendingExpandGroupId: String? = null

    suspend fun applyPendingExpand() {
        val id = pendingExpandGroupId
        val name = pendingExpandGroupName
        pendingExpandGroupId = null
        pendingExpandGroupName = null
        if (id != null || name != null) {
            val found = applyExpand(id, name)
            if (!found) onDeepLinkMissing(name ?: id.orEmpty())
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
            val found = applyExpand(
                trimmedId.takeIf { it.isNotEmpty() },
                normalized.takeIf { it.isNotEmpty() },
            )
            if (!found) onDeepLinkMissing(normalized.ifEmpty { trimmedId })
        }
    }

    fun expandGroupByNameIfPresent(scope: CoroutineScope, name: String) =
        expandGroupIfPresent(scope, id = null, name = name)

    private suspend fun applyExpand(id: String?, name: String?): Boolean {
        return mutationMutex.withLock {
            val current = uiState.value as? MainUiState.Success
            val found = current?.let { DeepLinkGroupLookup.existingName(it.groups, id, name) } != null
            val updated = runGroupMutation(uiState.value) {
                GroupMutations.expandByIdOrName(it, groupsRepository, id, name)
            }
            if (updated != null) uiState.value = updated
            found
        }
    }
}
