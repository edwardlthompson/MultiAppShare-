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
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import com.multiappshare.model.HistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val groupsRepository: GroupsRepository,
    private val historyRepository: HistoryRepository,
    private val packageManager: PackageManager,
    private val settingsRepository: SettingsRepository,
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

    private val compatiblePackagesCache = mutableMapOf<Pair<String, String>, Set<String>>()
    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState
    private val stateHelper = MainViewModelState(groupsRepository, historyRepository, _uiState, Mutex())

    init {
        loadData()
    }

    fun onNotificationsPermissionResult(granted: Boolean) {
        notificationsEnabled = granted
    }

    fun setOnboardingDismissed() {
        viewModelScope.launch {
            settingsRepository.setOnboardingCompleted()
            showOnboardingDialog = false
        }
    }

    fun exportGroupsToUri(uri: Uri, passphrase: CharArray) =
        MainViewModelBackup.export(viewModelScope, context, groupsRepository, uri, passphrase)

    fun importGroupsFromUri(uri: Uri) =
        MainViewModelBackup.import(
            viewModelScope,
            context,
            groupsRepository,
            uri,
            onEncrypted = { importPassphrasePendingUri = it },
            onComplete = { loadData() },
        )

    fun dismissImportPassphraseRequest() {
        importPassphrasePendingUri = null
    }

    fun importGroupsWithPassphrase(uri: Uri, passphrase: CharArray) =
        MainViewModelBackup.importWithPassphrase(
            viewModelScope,
            context,
            groupsRepository,
            uri,
            passphrase,
            onSuccess = {
                importPassphrasePendingUri = null
                loadData()
            },
        )

    fun createShortcutForGroup(group: AppGroup) = ShortcutHelper.createPinShortcut(context, group)

    fun getCompatiblePackages(action: String, mime: String): Set<String> =
        AppListResolver.getCompatiblePackages(packageManager, compatiblePackagesCache, action, mime)

    fun updateShareSession(update: ShareSessionState.() -> ShareSessionState) {
        shareSession = shareSession.update()
    }

    fun clearShareSession() {
        shareSession = ShareSessionState()
    }

    fun loadData() {
        compatiblePackagesCache.clear()
        viewModelScope.launch(Dispatchers.IO) {
            val (groups, allApps, history) = loadMainUiData(
                groupsRepository,
                historyRepository,
                packageManager,
                context.packageName,
            )
            val showOnboarding = shouldShowOnboarding(groups, settingsRepository)
            withContext(Dispatchers.Main) {
                showOnboardingDialog = showOnboarding
                _uiState.value = MainUiState.Success(groups, allApps, history)
            }
            stateHelper.applyPendingExpand { stateHelper.applyExpandByName(it) }
        }
    }

    fun autoGroupApps(allApps: List<AppInfo>, append: Boolean, singleCategoryOnly: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? MainUiState.Success
            val merged = mergeAutoGroups(
                allApps,
                append,
                singleCategoryOnly,
                currentState?.groups ?: emptyList(),
                groupsRepository,
            )
            if (currentState != null) _uiState.value = currentState.copy(groups = merged)
        }
    }

    fun createGroup(groupName: String, onResult: (Boolean) -> Unit = {}) =
        stateHelper.createGroup(viewModelScope, groupName, onResult)

    fun deleteGroup(group: AppGroup) = stateHelper.deleteGroup(viewModelScope, group)

    fun toggleGroupExpanded(group: AppGroup) = stateHelper.toggleGroupExpanded(viewModelScope, group)

    fun updateGroupApps(group: AppGroup, apps: List<AppInfo>) =
        stateHelper.updateGroupApps(viewModelScope, group, apps)

    fun incrementGroupUsage(group: AppGroup) = stateHelper.incrementGroupUsage(viewModelScope, group)

    fun updateGroupsOrder(groups: List<AppGroup>) = stateHelper.updateGroupsOrder(viewModelScope, groups)

    fun expandGroupByNameIfPresent(name: String) = stateHelper.expandGroupByNameIfPresent(viewModelScope, name)

    fun addHistoryItem(item: HistoryItem) = stateHelper.addHistoryItem(viewModelScope, item)
}
