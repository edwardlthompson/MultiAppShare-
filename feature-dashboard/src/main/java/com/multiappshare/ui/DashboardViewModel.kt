package com.multiappshare.ui

import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.HistoryRepository
import com.multiappshare.domain.SettingsRepository

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multiappshare.domain.CreateAutoGroupsUseCase
import com.multiappshare.domain.GetCompatibleAppsUseCase
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
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val groupsRepository: GroupsRepository,
    private val historyRepository: HistoryRepository,
    private val settingsRepository: SettingsRepository,
    private val createAutoGroupsUseCase: CreateAutoGroupsUseCase,
    private val getCompatibleAppsUseCase: GetCompatibleAppsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState

    var showOnboardingDialog by mutableStateOf(false)
        private set

    init {
        loadData()
    }

    fun loadData() {
        getCompatibleAppsUseCase.clearCache()
        viewModelScope.launch(Dispatchers.IO) {
            val groups = groupsRepository.loadGroups().sortedByDescending { it.usageCount }
            val history = historyRepository.loadHistory()

            // Fetching apps is common logic. We'll reuse the logic here from MainViewModel.
            // (Keeping it concise as it's extracted already or will be modularized further)
            // For now, let's keep allApps population in ViewModel or a Repo.
            // Let's assume allApps logic can be kept in a repository later, but ViewModel is fine for now.
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
            val updated = createAutoGroupsUseCase(allApps, append)
            val state = _uiState.value
            if (state is DashboardUiState.Success) {
                _uiState.value = state.copy(groups = updated)
            }
        }
    }

    fun createGroup(groupName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? DashboardUiState.Success ?: return@launch
            val newGroup = AppGroup(name = groupName, apps = emptyList())
            val updatedGroups = currentState.groups + newGroup
            groupsRepository.saveGroups(updatedGroups)
            _uiState.value = currentState.copy(groups = updatedGroups)
        }
    }

    fun deleteGroup(group: AppGroup) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? DashboardUiState.Success ?: return@launch
            val updatedGroups = currentState.groups.filter { it.name != group.name }
            groupsRepository.saveGroups(updatedGroups)
            _uiState.value = currentState.copy(groups = updatedGroups)
        }
    }

    fun toggleGroupExpanded(group: AppGroup) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? DashboardUiState.Success ?: return@launch
            val updatedGroups = currentState.groups.map {
                if (it.name == group.name) it.copy(isExpanded = !it.isExpanded) else it
            }
            groupsRepository.saveGroups(updatedGroups)
            _uiState.value = currentState.copy(groups = updatedGroups)
        }
    }

    fun updateGroupApps(group: AppGroup, apps: List<AppInfo>) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? DashboardUiState.Success ?: return@launch
            val updatedGroups = currentState.groups.map { 
                if (it.name == group.name) it.copy(apps = apps) else it 
            }
            groupsRepository.saveGroups(updatedGroups)
            _uiState.value = currentState.copy(groups = updatedGroups)
        }
    }

    fun incrementGroupUsage(group: AppGroup) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? DashboardUiState.Success ?: return@launch
            val updatedGroups = currentState.groups.map { 
                if (it.name == group.name) it.copy(usageCount = it.usageCount + 1) else it 
            }
            val sorted = updatedGroups.sortedByDescending { it.usageCount }
            groupsRepository.saveGroups(sorted)
            _uiState.value = currentState.copy(groups = sorted)
        }
    }

    fun updateGroupsOrder(groups: List<AppGroup>) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? DashboardUiState.Success ?: return@launch
            groupsRepository.saveGroups(groups)
            _uiState.value = currentState.copy(groups = groups)
        }
    }

    fun addHistoryItem(item: HistoryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? DashboardUiState.Success ?: return@launch
            val updatedHistory = (listOf(item) + currentState.history).take(50)
            historyRepository.saveHistory(updatedHistory)
            _uiState.value = currentState.copy(history = updatedHistory)
        }
    }
}

sealed class DashboardUiState {
    data object Loading : DashboardUiState()
    data class Success(val groups: List<AppGroup>, val allApps: List<AppInfo>, val history: List<HistoryItem>) : DashboardUiState()
}

