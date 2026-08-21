package com.multiappshare

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.HistoryRepository
import com.multiappshare.domain.SettingsRepository
import com.multiappshare.domain.ShareSessionStore
import com.multiappshare.model.AppInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class MainRepoDeps(
    val groups: GroupsRepository,
    val history: HistoryRepository,
    val settings: SettingsRepository,
    val shareSession: ShareSessionStore,
)

internal class MainDataDeps(
    val scope: CoroutineScope,
    val context: Context,
    val repos: MainRepoDeps,
    val packageManager: PackageManager,
    val uiState: MutableStateFlow<MainUiState>,
)

internal class MainDataCommands(
    private val deps: MainDataDeps,
    private val stateHelper: MainViewModelState,
    private val compatiblePackagesCache: MutableMap<Pair<String, String>, Set<String>>,
    private val setOnboarding: (Boolean) -> Unit,
    private val setImportUri: (Uri?) -> Unit,
) {
    fun setOnboardingDismissed() {
        deps.scope.launch {
            deps.repos.settings.setOnboardingCompleted()
            setOnboarding(false)
        }
    }

    fun exportGroupsToUri(uri: Uri, passphrase: CharArray) =
        MainViewModelBackup.export(deps.scope, deps.context, deps.repos, uri, passphrase)

    fun importGroupsFromUri(uri: Uri) =
        MainViewModelBackup.import(
            deps.scope,
            deps.context,
            deps.repos,
            uri,
            onEncrypted = { setImportUri(it) },
            onComplete = { loadData() },
        )

    fun importGroupsWithPassphrase(uri: Uri, passphrase: CharArray) =
        MainViewModelBackup.importWithPassphrase(
            deps.scope,
            deps.context,
            deps.repos,
            uri,
            passphrase,
            onSuccess = {
                setImportUri(null)
                loadData()
            },
        )

    fun loadData() {
        compatiblePackagesCache.clear()
        deps.scope.launch(Dispatchers.IO) {
            val (groups, allApps, history) = loadMainUiData(
                deps.repos.groups,
                deps.repos.history,
                deps.packageManager,
                deps.context.packageName,
            )
            val showOnboarding = shouldShowOnboarding(groups, deps.repos.settings)
            withContext(Dispatchers.Main) {
                setOnboarding(showOnboarding)
                deps.uiState.value = MainUiState.Success(groups, allApps, history)
            }
            ShortcutHelper.syncAfterLoad(deps.context, groups)
            stateHelper.applyPendingExpand()
        }
    }

    fun autoGroupApps(allApps: List<AppInfo>, append: Boolean, singleCategoryOnly: Int? = null) {
        deps.scope.launch(Dispatchers.IO) {
            val currentState = deps.uiState.value as? MainUiState.Success
            val merged = mergeAutoGroups(
                allApps,
                append,
                singleCategoryOnly,
                currentState?.groups ?: emptyList(),
                deps.repos.groups,
            )
            if (currentState != null) deps.uiState.value = currentState.copy(groups = merged)
        }
    }
}
