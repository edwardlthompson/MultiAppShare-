package com.multiappshare.share

import com.multiappshare.ShareSessionState
import com.multiappshare.domain.ShareSessionStore
import com.multiappshare.domain.canRestore
import com.multiappshare.domain.hasPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class ShareSessionCoordinator(
    private val store: ShareSessionStore,
    private val nowMillis: () -> Long = { System.currentTimeMillis() },
) {
    fun persistInflight(scope: CoroutineScope, session: ShareSessionState) {
        if (!session.inShareMode) {
            clearInflight(scope)
            return
        }
        val snapshot = session.toSnapshot(nowMillis())
        scope.launch { store.saveInflight(snapshot) }
    }

    fun clearInflight(scope: CoroutineScope) {
        scope.launch { store.clearInflight() }
    }

    fun rememberLastPayload(scope: CoroutineScope, session: ShareSessionState) {
        if (!session.inShareMode) return
        val snapshot = session.toSnapshot(nowMillis())
        scope.launch {
            store.saveLastPayload(snapshot)
            store.clearInflight()
        }
    }

    suspend fun restoreInflight(): ShareSessionState? {
        val snapshot = store.loadInflight() ?: return null
        return snapshot.takeIf { it.canRestore(nowMillis()) }?.toState()
    }

    suspend fun restoreLastPayload(): ShareSessionState? {
        val snapshot = store.loadLastPayload() ?: return null
        return snapshot.takeIf { it.hasPayload() }?.toState()?.copy(sharingStarted = false, currentIndex = 0)
    }

    suspend fun hasLastPayload(): Boolean = store.loadLastPayload()?.hasPayload() == true
}
