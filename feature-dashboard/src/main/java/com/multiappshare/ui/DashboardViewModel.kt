package com.multiappshare.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multiappshare.domain.CreateAutoGroupsUseCase
import com.multiappshare.domain.GetCompatibleAppsUseCase
import com.multiappshare.domain.GroupNameHelper
import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.HistoryRepository
import com.multiappshare.domain.ListInstalledAppsUseCase
import com.multiappshare.domain.SettingsRepository
import com.multiappshare.model.AppGroup
import com.multiappshare.model.AppInfo
import com.multiappshare.model.HistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val groupsRepository: GroupsRepository,
    private val historyRepository: HistoryRepository,
    private val settingsRepository: SettingsRepository,
    private val createAutoGroupsUseCase: CreateAutoGroupsUseCase,
    private val getCompatibleAppsUseCase: GetCompatibleAppsUseCase,
    private val listInstalledAppsUseCase: ListInstalledAppsUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState

    var showOnboardingDialog by mutableStateOf(false)
        private set

    private val mutationMutex = Mutex()

    init {
        loadData()
    }

    fun loadData() {
        getCompatibleAppsUseCase.clearCache()
        viewModelScope.launch(Dispatchers.IO) {
            val groups = groupsRepository.loadGroups().sortedByDescending { it.usageCount }
            val history = historyRepository.loadHistory()
            val allApps = listInstalledAppsUseCase(context.packageName)
            val onboardingCompleted = settingsRepository.isOnboardingCompleted.first()

            withContext(Dispatchers.Main) {
                if (groups.isEmpty() && !onboardingCompleted) {
                    showOnboardingDialog = true
                }
                _uiState.value = DashboardUiState.Success(groups, allApps, history)
            }
        }
    }

    fun setOnboardingDismissed() {
        viewModelScope.launch {
            settingsRepository.setOnboardingCompleted()
            showOnboardingDialog = false
        }
    }

    fun autoGroup(allApps: List<AppInfo>, append: Boolean) {
        viewModelScope.launch {
            mutationMutex.withLock {
                val state = _uiState.value as? DashboardUiState.Success ?: return@launch
                val updated = createAutoGroupsUseCase(allApps, append)
                _uiState.value = state.copy(groups = updated)
            }
        }
    }

    fun createGroup(groupName: String, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            mutationMutex.withLock {
                val currentState = _uiState.value as? DashboardUiState.Success ?: return@launch
                val normalized = GroupNameHelper.normalize(groupName)
                if (normalized.isBlank() || GroupNameHelper.isDuplicate(normalized, currentState.groups)) {
                    onResult(false)
                    return@launch
                }
                val updatedGroups = currentState.groups + AppGroup(
                    name = normalized,
                    apps = emptyList(),
                    id = com.multiappshare.domain.GroupIds.newId(),
                )
                groupsRepository.saveGroups(updatedGroups)
                _uiState.value = currentState.copy(groups = updatedGroups)
                onResult(true)
            }
        }
    }

    fun deleteGroup(group: AppGroup) {
        viewModelScope.launch {
            mutationMutex.withLock {
                val currentState = _uiState.value as? DashboardUiState.Success ?: return@launch
                val updatedGroups = currentState.groups.filter { it.name != group.name }
                groupsRepository.saveGroups(updatedGroups)
                _uiState.value = currentState.copy(groups = updatedGroups)
            }
        }
    }

    fun toggleGroupExpanded(group: AppGroup) {
        viewModelScope.launch {
            mutationMutex.withLock {
                val currentState = _uiState.value as? DashboardUiState.Success ?: return@launch
                val updatedGroups = currentState.groups.map {
                    if (it.name == group.name) it.copy(isExpanded = !it.isExpanded) else it
                }
                groupsRepository.saveGroups(updatedGroups)
                _uiState.value = currentState.copy(groups = updatedGroups)
            }
        }
    }

    fun updateGroupApps(group: AppGroup, apps: List<AppInfo>) {
        viewModelScope.launch {
            mutationMutex.withLock {
                val currentState = _uiState.value as? DashboardUiState.Success ?: return@launch
                val updatedGroups = currentState.groups.map {
                    if (it.name == group.name) it.copy(apps = apps) else it
                }
                groupsRepository.saveGroups(updatedGroups)
                _uiState.value = currentState.copy(groups = updatedGroups)
            }
        }
    }

    fun incrementGroupUsage(group: AppGroup) {
        viewModelScope.launch {
            mutationMutex.withLock {
                val currentState = _uiState.value as? DashboardUiState.Success ?: return@launch
                val updatedGroups = currentState.groups.map {
                    if (it.name == group.name) it.copy(usageCount = it.usageCount + 1) else it
                }.sortedByDescending { it.usageCount }
                groupsRepository.saveGroups(updatedGroups)
                _uiState.value = currentState.copy(groups = updatedGroups)
            }
        }
    }

    fun updateGroupsOrder(groups: List<AppGroup>) {
        viewModelScope.launch {
            mutationMutex.withLock {
                val currentState = _uiState.value as? DashboardUiState.Success ?: return@launch
                groupsRepository.saveGroups(groups)
                _uiState.value = currentState.copy(groups = groups)
            }
        }
    }

    fun addHistoryItem(item: HistoryItem) {
        viewModelScope.launch {
            mutationMutex.withLock {
                val currentState = _uiState.value as? DashboardUiState.Success ?: return@launch
                val updatedHistory = withContext(Dispatchers.IO) {
                    val list = (listOf(item) + currentState.history).take(50)
                    historyRepository.saveHistory(list)
                    list
                }
                _uiState.value = currentState.copy(history = updatedHistory)
            }
        }
    }
}

sealed class DashboardUiState {
    data object Loading : DashboardUiState()
    data class Success(val groups: List<AppGroup>, val allApps: List<AppInfo>, val history: List<HistoryItem>) : DashboardUiState()
}
