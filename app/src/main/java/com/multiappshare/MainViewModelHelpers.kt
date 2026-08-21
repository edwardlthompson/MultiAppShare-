package com.multiappshare

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.HistoryRepository
import com.multiappshare.domain.SettingsRepository
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import com.multiappshare.model.HistoryItem
import kotlinx.coroutines.flow.first

internal suspend fun loadMainUiData(
    groupsRepository: GroupsRepository,
    historyRepository: HistoryRepository,
    packageManager: PackageManager,
    excludePackage: String,
): Triple<List<AppGroup>, List<AppInfo>, List<HistoryItem>> {
    val groups = groupsRepository.loadGroups().sortedByDescending { it.usageCount }
    val history = historyRepository.loadHistory()
    val allApps = AppListResolver.resolveAllApps(packageManager, excludePackage)
    return Triple(groups, allApps, history)
}

internal suspend fun shouldShowOnboarding(
    groups: List<AppGroup>,
    settingsRepository: SettingsRepository,
): Boolean {
    val completed = settingsRepository.isOnboardingCompleted.first()
    return groups.isEmpty() && !completed
}

internal suspend fun mergeAutoGroups(
    allApps: List<AppInfo>,
    append: Boolean,
    singleCategoryOnly: Int?,
    currentGroups: List<AppGroup>,
    groupsRepository: GroupsRepository,
): List<AppGroup> {
    val existingGroups = if (append) currentGroups else emptyList()
    val mergedGroups = AutoGroupHelper.buildAutoGroups(allApps, existingGroups, append, singleCategoryOnly)
    groupsRepository.saveGroups(mergedGroups)
    return mergedGroups
}

internal object MainViewModelBackup {
    fun export(
        viewModelScope: kotlinx.coroutines.CoroutineScope,
        context: Context,
        repos: MainRepoDeps,
        uri: Uri,
        passphrase: CharArray,
    ) {
        BackupOperations.exportGroupsToUri(viewModelScope, context, repos, uri, passphrase)
    }

    fun import(
        viewModelScope: kotlinx.coroutines.CoroutineScope,
        context: Context,
        repos: MainRepoDeps,
        uri: Uri,
        onEncrypted: (Uri) -> Unit,
        onComplete: () -> Unit,
    ) {
        BackupOperations.importGroupsFromUri(viewModelScope, context, repos, uri, onEncrypted, onComplete)
    }

    fun importWithPassphrase(
        viewModelScope: kotlinx.coroutines.CoroutineScope,
        context: Context,
        repos: MainRepoDeps,
        uri: Uri,
        passphrase: CharArray,
        onSuccess: () -> Unit,
    ) {
        BackupOperations.importGroupsWithPassphrase(viewModelScope, context, repos, uri, passphrase, onSuccess)
    }
}
