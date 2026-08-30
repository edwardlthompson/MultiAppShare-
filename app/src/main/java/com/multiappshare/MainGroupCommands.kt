package com.multiappshare

import com.multiappshare.domain.SettingsRepository
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import com.multiappshare.model.HistoryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class MainGroupCommands(
    private val scope: CoroutineScope,
    private val stateHelper: MainViewModelState,
    private val settingsRepository: SettingsRepository,
    private val setLastDeleted: (AppGroup?) -> Unit,
    private val getLastDeleted: () -> AppGroup?,
) {
    fun setAppLanguage(tag: String?) {
        scope.launch { settingsRepository.setAppLanguage(tag) }
    }

    fun setDarkTheme(enabled: Boolean?) {
        scope.launch { settingsRepository.setDarkTheme(enabled) }
    }

    fun setSharingDelay(delayMs: Int) {
        scope.launch { settingsRepository.setSharingDelay(delayMs) }
    }

    fun setCrashCaptureEnabled(enabled: Boolean) {
        scope.launch { settingsRepository.setCrashCaptureEnabled(enabled) }
    }

    fun setHighRefreshEnabled(enabled: Boolean) {
        scope.launch { settingsRepository.setHighRefreshEnabled(enabled) }
    }

    fun createGroup(groupName: String, onResult: (Boolean) -> Unit = {}) =
        stateHelper.createGroup(scope, groupName, onResult)

    fun duplicateGroup(group: AppGroup, onResult: (Boolean) -> Unit = {}) =
        stateHelper.duplicateGroup(scope, group, onResult)

    fun renameGroup(group: AppGroup, newName: String, onResult: (Boolean) -> Unit = {}) =
        stateHelper.renameGroup(scope, group, newName, onResult)

    fun mergeGroups(target: AppGroup, source: AppGroup, onResult: (Boolean) -> Unit = {}) =
        stateHelper.mergeGroups(scope, target, source, onResult)

    fun deleteGroup(group: AppGroup) {
        setLastDeleted(group)
        stateHelper.deleteGroup(scope, group)
    }

    fun undoDeleteGroup() {
        val group = getLastDeleted() ?: return
        setLastDeleted(null)
        stateHelper.restoreGroup(scope, group)
    }

    fun toggleGroupExpanded(group: AppGroup) = stateHelper.toggleGroupExpanded(scope, group)
    fun updateGroupApps(group: AppGroup, apps: List<AppInfo>) =
        stateHelper.updateGroupApps(scope, group, apps)
    fun incrementGroupUsage(group: AppGroup) = stateHelper.incrementGroupUsage(scope, group)
    fun updateGroupsOrder(groups: List<AppGroup>) = stateHelper.updateGroupsOrder(scope, groups)
    fun expandGroupByNameIfPresent(name: String) = stateHelper.expandGroupByNameIfPresent(scope, name)
    fun expandGroupIfPresent(id: String?, name: String?) = stateHelper.expandGroupIfPresent(scope, id, name)
    fun addHistoryItem(item: HistoryItem) = stateHelper.addHistoryItem(scope, item)
}
