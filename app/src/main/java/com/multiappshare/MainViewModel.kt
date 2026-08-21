package com.multiappshare

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.HistoryRepository
import com.multiappshare.domain.SettingsRepository
import com.multiappshare.domain.ShareSessionStore
import com.multiappshare.domain.SharingDelay
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import com.multiappshare.model.HistoryItem
import com.multiappshare.share.ShareSessionCoordinator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    groupsRepository: GroupsRepository,
    historyRepository: HistoryRepository,
    private val packageManager: PackageManager,
    settingsRepository: SettingsRepository,
    shareSessionStore: ShareSessionStore,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    var showOnboardingDialog by mutableStateOf(false)
        private set
    var importPassphrasePendingUri by mutableStateOf<Uri?>(null)
        private set
    var shareSession by mutableStateOf(ShareSessionState())
        private set
    var notificationsEnabled by mutableStateOf(true)
        private set
    var hasLastSharePayload by mutableStateOf(false)
        private set
    var lastDeletedGroup by mutableStateOf<AppGroup?>(null)
        private set
    var sharingDelayMs by mutableStateOf(500)
        private set
    val darkTheme = settingsRepository.isDarkThemeEnabled

    private val session = MainViewModelSession(
        ShareSessionCoordinator(shareSessionStore),
        viewModelScope,
        setSession = { shareSession = it },
        getSession = { shareSession },
        setHasLast = { hasLastSharePayload = it },
    )
    private val compatiblePackagesCache = mutableMapOf<Pair<String, String>, Set<String>>()
    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState
    private val stateHelper = MainViewModelState(groupsRepository, historyRepository, _uiState, Mutex())
    private val groups = MainGroupCommands(
        viewModelScope,
        stateHelper,
        settingsRepository,
        setLastDeleted = { lastDeletedGroup = it },
        getLastDeleted = { lastDeletedGroup },
    )
    private val data = MainDataCommands(
        MainDataDeps(
            viewModelScope,
            context,
            MainRepoDeps(groupsRepository, historyRepository, settingsRepository, shareSessionStore),
            packageManager,
            _uiState,
        ),
        stateHelper,
        compatiblePackagesCache,
        setOnboarding = { showOnboardingDialog = it },
        setImportUri = { importPassphrasePendingUri = it },
    )

    init {
        data.loadData()
        session.refreshLastPayloadFlag()
        viewModelScope.launch {
            settingsRepository.sharingDelay.collect { sharingDelayMs = SharingDelay.clamp(it) }
        }
    }

    fun onNotificationsPermissionResult(granted: Boolean) {
        notificationsEnabled = granted
    }

    fun setOnboardingDismissed() = data.setOnboardingDismissed()
    fun exportGroupsToUri(uri: Uri, passphrase: CharArray) = data.exportGroupsToUri(uri, passphrase)
    fun importGroupsFromUri(uri: Uri) = data.importGroupsFromUri(uri)
    fun dismissImportPassphraseRequest() {
        importPassphrasePendingUri = null
    }
    fun importGroupsWithPassphrase(uri: Uri, passphrase: CharArray) =
        data.importGroupsWithPassphrase(uri, passphrase)
    fun createShortcutForGroup(group: AppGroup) = MainViewModelPins.pinShortcut(context, group)
    fun getCompatiblePackages(action: String, mime: String): Set<String> =
        AppListResolver.getCompatiblePackages(packageManager, compatiblePackagesCache, action, mime)

    fun updateShareSession(update: ShareSessionState.() -> ShareSessionState) = session.update(update)
    fun clearShareSession() = session.clear()
    fun finishShareSession() = session.finish()
    fun restoreInflightIfFresh() = session.restoreInflightIfFresh()
    fun restoreLastPayload(onResult: (Boolean) -> Unit = {}) = session.restoreLastPayload(onResult)
    fun restoreHistoryPayload(item: HistoryItem, onResult: (Boolean) -> Unit = {}) =
        session.restoreHistoryPayload(item.payloadJson, onResult)
    fun shareFromClipboard(host: Context = context) = MainViewModelPins.shareClipboard(host, context, session)
    fun setAppLanguage(tag: String?) = groups.setAppLanguage(tag)
    fun setDarkTheme(enabled: Boolean?) = groups.setDarkTheme(enabled)
    fun setSharingDelay(delayMs: Int) = groups.setSharingDelay(delayMs)
    fun loadData() = data.loadData()
    fun autoGroupApps(allApps: List<AppInfo>, append: Boolean, singleCategoryOnly: Int? = null) =
        data.autoGroupApps(allApps, append, singleCategoryOnly)
    fun createGroup(groupName: String, onResult: (Boolean) -> Unit = {}) =
        groups.createGroup(groupName, onResult)
    fun duplicateGroup(group: AppGroup, onResult: (Boolean) -> Unit = {}) =
        groups.duplicateGroup(group, onResult)
    fun renameGroup(group: AppGroup, newName: String, onResult: (Boolean) -> Unit = {}) =
        groups.renameGroup(group, newName) { ok ->
            if (ok) MainViewModelPins.afterRename(context, group, newName)
            onResult(ok)
        }
    fun mergeGroups(target: AppGroup, source: AppGroup, onResult: (Boolean) -> Unit = {}) =
        groups.mergeGroups(target, source, onResult)
    fun deleteGroup(group: AppGroup) = groups.deleteGroup(group)
    fun undoDeleteGroup() = groups.undoDeleteGroup()
    fun clearLastDeletedGroup() {
        lastDeletedGroup = null
    }
    fun toggleGroupExpanded(group: AppGroup) = groups.toggleGroupExpanded(group)
    fun updateGroupApps(group: AppGroup, apps: List<AppInfo>) = groups.updateGroupApps(group, apps)
    fun incrementGroupUsage(group: AppGroup) = groups.incrementGroupUsage(group)
    fun updateGroupsOrder(list: List<AppGroup>) = groups.updateGroupsOrder(list)
    fun expandGroupByNameIfPresent(name: String) = groups.expandGroupByNameIfPresent(name)
    fun expandGroupIfPresent(id: String?, name: String?) = groups.expandGroupIfPresent(id, name)
    fun addHistoryItem(item: HistoryItem) = groups.addHistoryItem(item)
}
