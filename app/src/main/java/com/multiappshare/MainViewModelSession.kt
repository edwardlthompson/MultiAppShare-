package com.multiappshare

import com.multiappshare.share.ShareSessionCoordinator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class MainViewModelSession(
    private val coordinator: ShareSessionCoordinator,
    private val scope: CoroutineScope,
    private val setSession: (ShareSessionState) -> Unit,
    private val getSession: () -> ShareSessionState,
    private val setHasLast: (Boolean) -> Unit,
) {
    fun update(update: ShareSessionState.() -> ShareSessionState) {
        val next = getSession().update()
        setSession(next)
        coordinator.persistInflight(scope, next)
    }

    fun clear() {
        setSession(ShareSessionState())
        coordinator.clearInflight(scope)
    }

    fun finish() {
        val current = getSession()
        coordinator.rememberLastPayload(scope, current)
        setHasLast(current.inShareMode)
        setSession(ShareSessionState())
    }

    fun restoreInflightIfFresh() {
        if (getSession().inShareMode) return
        scope.launch {
            val restored = coordinator.restoreInflight() ?: return@launch
            setSession(restored)
        }
    }

    fun restoreLastPayload(onResult: (Boolean) -> Unit) {
        scope.launch {
            val restored = coordinator.restoreLastPayload()
            if (restored == null) {
                onResult(false)
                return@launch
            }
            setSession(restored)
            coordinator.persistInflight(this, restored)
            onResult(true)
        }
    }

    fun refreshLastPayloadFlag() {
        scope.launch { setHasLast(coordinator.hasLastPayload()) }
    }
}
