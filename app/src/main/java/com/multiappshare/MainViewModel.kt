package com.multiappshare

import com.multiappshare.domain.GroupsRepository
import com.multiappshare.domain.HistoryRepository
import com.multiappshare.domain.SettingsRepository

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import timber.log.Timber

@HiltViewModel
class MainViewModel @Inject constructor(
    private val groupsRepository: GroupsRepository,
    private val historyRepository: HistoryRepository,
    private val packageManager: PackageManager,
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var showOnboardingDialog by mutableStateOf(false)
        private set

    var expandedGroupName by mutableStateOf<String?>(null)

    fun setOnboardingDismissed() {
        viewModelScope.launch {
            settingsRepository.setOnboardingCompleted()
            showOnboardingDialog = false
        }
    }

    fun exportGroupsToUri(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val groups = groupsRepository.loadGroups()
                val jsonString = Json.encodeToString(groups)
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(jsonString.toByteArray())
                }
            } catch (e: Exception) {
                Timber.e(e, "Export failed")
            }
        }
    }

    fun importGroupsFromUri(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val jsonString = inputStream.bufferedReader().use { it.readText() }
                    val importedGroups: List<AppGroup> = Json.decodeFromString(jsonString)
                    groupsRepository.saveGroups(importedGroups)
                    loadData()
                }
            } catch (e: Exception) {
                Timber.e(e, "Import failed")
            }
        }
    }

    fun createShortcutForGroup(group: AppGroup) {
        if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
            val intent = Intent(context, MainActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                putExtra("GROUP_NAME", group.name)
            }
            
            val shortcut = ShortcutInfoCompat.Builder(context, group.name)
                .setShortLabel(group.name)
                .setIcon(IconCompat.createWithResource(context, R.drawable.ic_launcher_foreground))
                .setIntent(intent)
                .build()

            ShortcutManagerCompat.requestPinShortcut(context, shortcut, null)
        }
    }

    private val compatiblePackagesCache = mutableMapOf<Pair<String, String>, Set<String>>()

    fun getCompatiblePackages(action: String, mime: String): Set<String> {
        val key = Pair(action, mime)
        if (compatiblePackagesCache.containsKey(key)) return compatiblePackagesCache[key]!!

        val mimeTypesToCheck = if (mime == "*/*") listOf("*/*", "text/plain", "image/*", "video/*") else listOf(mime)
        val compatiblePackages = mutableSetOf<String>()
        
        for (m in mimeTypesToCheck) {
            val shareIntent = Intent(action).apply { type = m }
            val resolveInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.queryIntentActivities(shareIntent, PackageManager.ResolveInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                packageManager.queryIntentActivities(shareIntent, 0)
            }
            compatiblePackages.addAll(resolveInfos.map { "${it.activityInfo.packageName}/${it.activityInfo.name}" })
        }
        
        compatiblePackagesCache[key] = compatiblePackages
        return compatiblePackages
    }

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        compatiblePackagesCache.clear()
        viewModelScope.launch(Dispatchers.IO) {
            val groups = groupsRepository.loadGroups().sortedByDescending { it.usageCount }
            val history = historyRepository.loadHistory()
            
            val mimeTypes = listOf("*/*", "text/plain", "image/*", "video/*", "application/*", "text/html", "audio/*")
            val resolveInfos = mutableListOf<android.content.pm.ResolveInfo>()
            
            val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                PackageManager.ResolveInfoFlags.of(0)
            } else {
                0
            }

            for (mime in mimeTypes) {
                val shareIntent = Intent(Intent.ACTION_SEND).apply { type = mime }
                val infos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    packageManager.queryIntentActivities(shareIntent, flag as PackageManager.ResolveInfoFlags)
                } else {
                    @Suppress("DEPRECATION")
                    packageManager.queryIntentActivities(shareIntent, flag as Int)
                }
                resolveInfos.addAll(infos)
                
                val shareMultipleIntent = Intent(Intent.ACTION_SEND_MULTIPLE).apply { type = mime }
                val multipleInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    packageManager.queryIntentActivities(shareMultipleIntent, flag as PackageManager.ResolveInfoFlags)
                } else {
                    @Suppress("DEPRECATION")
                    packageManager.queryIntentActivities(shareMultipleIntent, flag as Int)
                }
                resolveInfos.addAll(multipleInfos)
            }

            val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
            val launcherInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.queryIntentActivities(launcherIntent, flag as PackageManager.ResolveInfoFlags)
            } else {
                @Suppress("DEPRECATION")
                packageManager.queryIntentActivities(launcherIntent, flag as Int)
            }
            resolveInfos.addAll(launcherInfos)

            val allApps = resolveInfos
                .distinctBy { it.activityInfo.packageName + "/" + it.activityInfo.name }
                .map {
                    val appLabel = it.activityInfo.applicationInfo.loadLabel(packageManager).toString()
                    val activityLabel = it.loadLabel(packageManager).toString()
                    
                    val finalName = if (appLabel == activityLabel) {
                        val shortName = it.activityInfo.name.substringAfterLast('.')
                        "$appLabel - $shortName"
                    } else {
                        "$appLabel - $activityLabel"
                    }

                    val category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        it.activityInfo.applicationInfo.category
                    } else {
                        -1
                    }

                    AppInfo(
                        appName = finalName,
                        packageName = it.activityInfo.packageName,
                        activityName = it.activityInfo.name,
                        category = category
                    )
                }
                .filter { it.packageName != context.packageName }
                .sortedBy { it.appName.lowercase() }

            val initialOnboardingCompleted = settingsRepository.isOnboardingCompleted.first()

            if (groups.isEmpty() && !initialOnboardingCompleted) {
                showOnboardingDialog = true
                autoGroupApps(allApps, append = false, singleCategoryOnly = android.content.pm.ApplicationInfo.CATEGORY_SOCIAL)
            }

            _uiState.value = MainUiState.Success(groupsRepository.loadGroups(), allApps, history)
        }
    }

    fun autoGroupApps(allApps: List<AppInfo>, append: Boolean, singleCategoryOnly: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? MainUiState.Success
            val existingGroups = if (append) currentState?.groups ?: emptyList() else emptyList()
            val categoryToApps = mutableMapOf<String, MutableList<AppInfo>>()
            
            for (app in allApps) {
                if (singleCategoryOnly != null && app.category != singleCategoryOnly) continue

                val nameLower = app.appName.lowercase()
                val pkgLower = app.packageName.lowercase()

                val categoryLabel = when {
                    nameLower.contains("message") || nameLower.contains("chat") || nameLower.contains("messenger") || pkgLower.contains("messenger") || pkgLower.contains("telegram") || pkgLower.contains("whatsapp") -> "Messaging"
                    nameLower.contains("mail") || pkgLower.contains("email") || pkgLower.contains("gmail") || pkgLower.contains("outlook") -> "Email"
                    nameLower.contains("contact") || pkgLower.contains("contact") || nameLower.contains("people") -> "Contacts"
                    
                    else -> when (app.category) {
                        android.content.pm.ApplicationInfo.CATEGORY_SOCIAL -> "Social Media"
                        android.content.pm.ApplicationInfo.CATEGORY_GAME -> "Games"
                        android.content.pm.ApplicationInfo.CATEGORY_VIDEO -> "Video"
                        android.content.pm.ApplicationInfo.CATEGORY_AUDIO -> "Audio"
                        android.content.pm.ApplicationInfo.CATEGORY_IMAGE -> "Photography"
                        android.content.pm.ApplicationInfo.CATEGORY_MAPS -> "Maps"
                        android.content.pm.ApplicationInfo.CATEGORY_NEWS -> "News"
                        android.content.pm.ApplicationInfo.CATEGORY_PRODUCTIVITY -> "Productivity"
                        else -> null
                    }
                }
                
                if (categoryLabel != null) {
                    categoryToApps.getOrPut(categoryLabel) { mutableListOf() }.add(app)
                }
            }

            val newGroups = categoryToApps.map { (name, apps) ->
                val existing = existingGroups.find { it.name == name }
                if (existing != null && append) {
                    AppGroup(name = name, apps = (existing.apps + apps).distinctBy { it.packageName + "/" + it.activityName })
                } else {
                    AppGroup(name = name, apps = apps)
                }
            }

            val mergedGroups = existingGroups.filter { ex -> newGroups.none { it.name == ex.name } } + newGroups
            groupsRepository.saveGroups(mergedGroups)
            
            if (currentState != null) {
                _uiState.value = currentState.copy(groups = mergedGroups)
            }
        }
    }

    fun createGroup(groupName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? MainUiState.Success ?: return@launch
            val newGroup = AppGroup(name = groupName, apps = emptyList())
            val updatedGroups = currentState.groups + newGroup
            groupsRepository.saveGroups(updatedGroups)
            _uiState.value = currentState.copy(groups = updatedGroups)
        }
    }

    fun deleteGroup(group: AppGroup) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? MainUiState.Success ?: return@launch
            val updatedGroups = currentState.groups.filter { it.name != group.name }
            groupsRepository.saveGroups(updatedGroups)
            _uiState.value = currentState.copy(groups = updatedGroups)
        }
    }

    fun toggleGroupExpanded(group: AppGroup) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? MainUiState.Success ?: return@launch
            val updatedGroups = currentState.groups.map {
                if (it.name == group.name) it.copy(isExpanded = !it.isExpanded) else it
            }
            groupsRepository.saveGroups(updatedGroups)
            _uiState.value = currentState.copy(groups = updatedGroups)
        }
    }

    fun updateGroupApps(group: AppGroup, apps: List<AppInfo>) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? MainUiState.Success ?: return@launch
            val updatedGroups = currentState.groups.map { 
                if (it.name == group.name) it.copy(apps = apps) else it 
            }
            groupsRepository.saveGroups(updatedGroups)
            _uiState.value = currentState.copy(groups = updatedGroups)
        }
    }

    fun incrementGroupUsage(group: AppGroup) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? MainUiState.Success ?: return@launch
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
            val currentState = _uiState.value as? MainUiState.Success ?: return@launch
            groupsRepository.saveGroups(groups)
            _uiState.value = currentState.copy(groups = groups)
        }
    }

    fun addHistoryItem(item: HistoryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentState = _uiState.value as? MainUiState.Success ?: return@launch
            val updatedHistory = (listOf(item) + currentState.history).take(50)
            historyRepository.saveHistory(updatedHistory)
            _uiState.value = currentState.copy(history = updatedHistory)
        }
    }
}

sealed class MainUiState {
    data object Loading : MainUiState()
    data class Success(val groups: List<AppGroup>, val allApps: List<AppInfo>, val history: List<HistoryItem>) : MainUiState()
}
