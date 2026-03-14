package com.multiappshare

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.multiappshare.ui.theme.MultiAppShareTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class AppInfo(
    val appName: String,
    val packageName: String
)

@Serializable
data class AppGroup(
    val name: String,
    val apps: List<AppInfo>,
    val isExpanded: Boolean = false
)

@Serializable
data class HistoryItem(
    val timestamp: Long,
    val groupName: String,
    val contentDescription: String,
    val status: String,
    val isError: Boolean = false
)

class GroupsRepository(context: Context) {
    private val file = File(context.filesDir, "groups.json")

    fun saveGroups(groups: List<AppGroup>) {
        val jsonString = Json.encodeToString(groups)
        file.writeText(jsonString)
    }

    fun loadGroups(): List<AppGroup> {
        if (!file.exists()) return emptyList()
        return try {
            Json.decodeFromString(file.readText())
        } catch (_: Exception) {
            emptyList()
        }
    }
}

class HistoryRepository(context: Context) {
    private val file = File(context.filesDir, "history.json")

    fun saveHistory(history: List<HistoryItem>) {
        val jsonString = Json.encodeToString(history.take(50))
        file.writeText(jsonString)
    }

    fun loadHistory(): List<HistoryItem> {
        if (!file.exists()) return emptyList()
        return try {
            Json.decodeFromString(file.readText())
        } catch (_: Exception) {
            emptyList()
        }
    }
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val groupsRepository = GroupsRepository(application)
    private val historyRepository = HistoryRepository(application)
    private val packageManager: PackageManager = application.packageManager

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
            compatiblePackages.addAll(resolveInfos.map { it.activityInfo.packageName })
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
            val groups = groupsRepository.loadGroups()
            val history = historyRepository.loadHistory()
            
            // Query for apps that handle common MIME types for ACTION_SEND and ACTION_SEND_MULTIPLE
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

            // Also query for launcher apps to capture apps that might only specify specific MIME types we missed
            val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
            val launcherInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.queryIntentActivities(launcherIntent, flag as PackageManager.ResolveInfoFlags)
            } else {
                @Suppress("DEPRECATION")
                packageManager.queryIntentActivities(launcherIntent, flag as Int)
            }
            resolveInfos.addAll(launcherInfos)

            val allApps = resolveInfos.map {
                AppInfo(
                    appName = it.loadLabel(packageManager).toString(),
                    packageName = it.activityInfo.packageName
                )
            }.filter { it.packageName != getApplication<Application>().packageName } // Exclude self
             .distinctBy { it.packageName }
             .sortedBy { it.appName.lowercase() }

            _uiState.value = MainUiState.Success(groups, allApps, history)
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

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    private val currentUris = mutableStateOf<List<Uri>?>(null)
    private val currentText = mutableStateOf<String?>(null)
    private val currentMimeType = mutableStateOf<String?>(null)
    
    private val appPackages = mutableStateOf<List<String>?>(null)
    private val currentIndex = mutableIntStateOf(0)
    private val isSharingStarted = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        handleIntent(intent)

        setContent {
            MultiAppShareTheme {
                MainScreen(
                    uris = currentUris.value,
                    text = currentText.value,
                    mimeType = currentMimeType.value,
                    sharingStarted = isSharingStarted.value,
                    currentIndex = currentIndex.intValue,
                    appPackages = appPackages.value,
                    onStartSharing = { group, viewModel ->
                        val mime = currentMimeType.value ?: "*/*"
                        val compatiblePackages = handleIncompatibleApps(currentUris.value, mime, group, viewModel)
                        val contentDesc = getContentDescription(mime, currentText.value, currentUris.value)
                        
                        if (compatiblePackages.isEmpty()) {
                            viewModel.addHistoryItem(HistoryItem(
                                System.currentTimeMillis(),
                                group.name,
                                contentDesc,
                                "Failed: No compatible apps",
                                true
                            ))
                            Toast.makeText(this@MainActivity, "No apps in '${group.name}' support this content.", Toast.LENGTH_LONG).show()
                        } else {
                            appPackages.value = compatiblePackages
                            currentIndex.intValue = 0
                            shareStep(currentUris.value, currentText.value, mime, compatiblePackages, 0)
                            isSharingStarted.value = true
                            
                            viewModel.addHistoryItem(HistoryItem(
                                System.currentTimeMillis(),
                                group.name,
                                contentDesc,
                                "Started sharing to ${compatiblePackages.size} apps"
                            ))
                        }
                    },
                    onNextStep = {
                        val packages = appPackages.value
                        val next = currentIndex.intValue + 1
                        if (packages != null && next < packages.size) {
                            currentIndex.intValue = next
                            shareStep(currentUris.value, currentText.value, currentMimeType.value ?: "*/*", packages, next)
                        } else {
                            isSharingStarted.value = false
                            stopSharingService()
                            Toast.makeText(this, "Sharing complete!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    packageManager = packageManager
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND || intent?.action == Intent.ACTION_SEND_MULTIPLE) {
            val isMultiple = intent.action == Intent.ACTION_SEND_MULTIPLE
            val uris: List<Uri>? = if (isMultiple) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM, Uri::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                }
            } else {
                val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri
                }
                if (uri != null) listOf(uri) else null
            }
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)
            val mime = intent.type ?: "*/*"
            
            currentUris.value = uris
            currentText.value = text
            currentMimeType.value = mime
            isSharingStarted.value = false
        }
    }

    private fun shareStep(uris: List<Uri>?, text: String?, mime: String, packages: List<String>, index: Int) {
        val serviceIntent = Intent(this, SharingService::class.java).apply {
            action = SharingService.ACTION_START_SHARING
            type = mime
            if (uris != null) putParcelableArrayListExtra(SharingService.EXTRA_IMAGE_URIS, ArrayList(uris))
            putExtra(Intent.EXTRA_TEXT, text)
            putStringArrayListExtra(SharingService.EXTRA_APP_PACKAGES, ArrayList(packages))
            putExtra(SharingService.EXTRA_CURRENT_INDEX, index)
            if (uris != null) {
                // Grant read permission for all URIs
                for (uri in uris) {
                    val clipDataItem = ClipData.Item(uri)
                    if (clipData == null) {
                        clipData = ClipData(null, arrayOf(mime), clipDataItem)
                    } else {
                        clipData?.addItem(clipDataItem)
                    }
                }
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun stopSharingService() {
        val serviceIntent = Intent(this, SharingService::class.java)
        stopService(serviceIntent)
    }

    private fun handleIncompatibleApps(uris: List<Uri>?, mime: String, group: AppGroup, viewModel: MainViewModel): List<String> {
        val shareAction = if (uris != null && uris.size > 1) Intent.ACTION_SEND_MULTIPLE else Intent.ACTION_SEND
        val compatiblePackages = viewModel.getCompatiblePackages(shareAction, mime)
        val compatible = mutableListOf<String>()
        val incompatible = mutableListOf<String>()

        for (app in group.apps) {
            if (app.packageName in compatiblePackages) {
                compatible.add(app.packageName)
            } else {
                incompatible.add(app.appName)
            }
        }

        if (incompatible.isNotEmpty()) {
            showIncompatibleNotification(incompatible)
        }

        return compatible
    }

    private fun showIncompatibleNotification(incompatibleAppNames: List<String>) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channelId = "incompatible_apps_channel"
        val channel = NotificationChannel(channelId, "Compatibility Alerts", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val appList = incompatibleAppNames.joinToString(", ")
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Incompatible Apps Skipped")
            .setContentText("Some apps in the group don't support this content.")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Skipped: $appList"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(2, notification)
    }

    private fun getContentDescription(mimeType: String?, text: String?, uris: List<Uri>?): String {
        val countStr = if (uris != null && uris.size > 1) " (${uris.size})" else ""
        return when {
            mimeType?.startsWith("image/") == true -> "Photo$countStr"
            mimeType?.startsWith("video/") == true -> "Video$countStr"
            text != null && uris.isNullOrEmpty() -> if (text.startsWith("http")) "Link" else "Text"
            else -> "Media$countStr"
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uris: List<Uri>?,
    text: String?,
    mimeType: String?,
    sharingStarted: Boolean,
    currentIndex: Int,
    appPackages: List<String>?,
    onStartSharing: (AppGroup, MainViewModel) -> Unit,
    onNextStep: () -> Unit,
    packageManager: PackageManager,
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var showCreateGroupDialog by remember { mutableStateOf(false) }
    var showModifyGroupDialog by remember { mutableStateOf<AppGroup?>(null) }
    var showReorderDialog by remember { mutableStateOf<AppGroup?>(null) }
    var showSortGroupsDialog by remember { mutableStateOf(false) }
    var groupToDelete by remember { mutableStateOf<AppGroup?>(null) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    val inShareMode = !uris.isNullOrEmpty() || text != null

    Scaffold(
        topBar = {
            if (!inShareMode) {
                TopAppBar(
                    title = { Text("Groups") },
                    actions = {
                        Box {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                            }
                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Sort Groups") },
                                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.List, null) },
                                    onClick = {
                                        showSortGroupsDialog = true
                                        menuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("History") },
                                    leadingIcon = { Icon(Icons.Default.Refresh, null) },
                                    onClick = {
                                        showHistoryDialog = true
                                        menuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("About") },
                                    leadingIcon = { Icon(Icons.Default.Info, null) },
                                    onClick = {
                                        showAboutDialog = true
                                        menuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (!inShareMode) {
                ExtendedFloatingActionButton(
                    onClick = { showCreateGroupDialog = true },
                    icon = { Icon(Icons.Default.Add, null) },
                    text = { Text("Add Group") }
                )
            }
        }
    ) { padding ->
        val backgroundModifier = if (inShareMode) {
            Modifier.fillMaxSize().padding(padding).padding(16.dp)
        } else {
            Modifier.fillMaxSize().padding(padding)
        }

        Surface(
            modifier = backgroundModifier,
            color = if (inShareMode) MaterialTheme.colorScheme.surface else Color.Transparent,
            shape = if (inShareMode) MaterialTheme.shapes.large else androidx.compose.ui.graphics.RectangleShape,
            tonalElevation = if (inShareMode) 8.dp else 0.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (inShareMode && sharingStarted && appPackages != null) {
                SharingInProgress(
                    mimeType = mimeType,
                    text = text,
                    uris = uris,
                    currentIndex = currentIndex,
                    totalApps = appPackages.size,
                    onNextStep = onNextStep
                )
            } else {
                when (val state = uiState) {
                    is MainUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    is MainUiState.Success -> {
                        Column {
                            if (inShareMode) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Share with a group", style = MaterialTheme.typography.headlineMedium)
                                    Text("Select a group below to start.", style = MaterialTheme.typography.bodyMedium)
                                }
                            }

                            if (showCreateGroupDialog) {
                                CreateGroupDialog(
                                    onDismiss = { showCreateGroupDialog = false },
                                    onCreateGroup = { name -> viewModel.createGroup(name); showCreateGroupDialog = false }
                                )
                            }

                            showModifyGroupDialog?.let { group ->
                                ModifyGroupAppsDialog(
                                    allApps = state.allApps,
                                    group = group,
                                    onDismiss = { showModifyGroupDialog = null },
                                    onSaveApps = { apps -> viewModel.updateGroupApps(group, apps); showModifyGroupDialog = null },
                                    onRefresh = { 
                                        viewModel.loadData() 
                                        Toast.makeText(context, "Refreshing apps...", Toast.LENGTH_SHORT).show()
                                    },
                                    packageManager = packageManager
                                )
                            }

                            showReorderDialog?.let { group ->
                                ReorderAppsDialog(
                                    group = group,
                                    onDismiss = { showReorderDialog = null },
                                    onSaveOrder = { apps -> viewModel.updateGroupApps(group, apps); showReorderDialog = null }
                                )
                            }

                            if (showSortGroupsDialog) {
                                SortGroupsDialog(
                                    groups = state.groups,
                                    onDismiss = { showSortGroupsDialog = false },
                                    onSaveOrder = { groups -> viewModel.updateGroupsOrder(groups); showSortGroupsDialog = false }
                                )
                            }

                            groupToDelete?.let { group ->
                                DeleteGroupDialog(
                                    groupName = group.name,
                                    onDismiss = { groupToDelete = null },
                                    onConfirm = { viewModel.deleteGroup(group); groupToDelete = null }
                                )
                            }

                            if (showHistoryDialog) HistoryDialog(history = state.history, onDismiss = { showHistoryDialog = false })
                            if (showAboutDialog) AboutDialog(onDismiss = { showAboutDialog = false })

                            if (state.groups.isEmpty() && !inShareMode) {
                                EmptyGroupsPlaceholder()
                            } else {
                                GroupList(
                                    groups = state.groups,
                                    onModifyClick = { showModifyGroupDialog = it },
                                    onReorderClick = { showReorderDialog = it },
                                    onDeleteClick = { groupToDelete = it },
                                    onToggleExpanded = { viewModel.toggleGroupExpanded(it) },
                                    onGroupClick = { onStartSharing(it, viewModel) },
                                    inShareMode = inShareMode,
                                    packageManager = packageManager
                                )
                            }
                        }
                    }
                }
            }
        }
        }
    }
}

@Composable
fun SharingInProgress(
    mimeType: String?,
    text: String?,
    uris: List<Uri>?,
    currentIndex: Int,
    totalApps: Int,
    onNextStep: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val countStr = if (uris != null && uris.size > 1) " (${uris.size})" else ""
        val contentDesc = when {
            mimeType?.startsWith("image/") == true -> "Photo$countStr"
            mimeType?.startsWith("video/") == true -> "Video$countStr"
            text != null && uris.isNullOrEmpty() -> "Text"
            else -> "Media$countStr"
        }
        Text("Sharing $contentDesc!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Step ${currentIndex + 1} of $totalApps", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "After you finish posting in the current app, return here and tap 'Next App' to continue.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNextStep, modifier = Modifier.fillMaxWidth()) {
            Text(if (currentIndex + 1 < totalApps) "Next App" else "Finish")
        }
    }
}

@Composable
fun EmptyGroupsPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "No groups yet.\nTap 'Add Group' to create your first group.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DeleteGroupDialog(groupName: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Group") },
        text = { Text("Are you sure you want to delete the group '$groupName'?") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Delete", color = MaterialTheme.colorScheme.error) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun HistoryDialog(history: List<HistoryItem>, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("History (Last 50)") },
        text = {
            if (history.isEmpty()) {
                Text("No activity yet.")
            } else {
                LazyColumn(modifier = Modifier.height(400.dp)) {
                    items(history) { item ->
                        val date = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(item.timestamp))
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = item.groupName,
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.isError) Color.Red else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(text = date, style = MaterialTheme.typography.bodySmall)
                            }
                            Text(text = "Shared ${item.contentDescription}", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = item.status,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (item.isError) Color.Red else MaterialTheme.colorScheme.primary
                            )
                            HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text("Close") }
        }
    )
}

@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val version = try {
        val pInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, 0)
        }
        pInfo.versionName
    } catch (_: Exception) {
        "Unknown"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("About Multi App Share") },
        text = {
            Column {
                Text("Version: $version", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Simplify sharing content across multiple apps sequentially.")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Developer Contact:", fontWeight = FontWeight.Bold)
                Text(
                    text = "Telegram: @EdwardLeeThompson",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(Intent.ACTION_VIEW, "https://t.me/EdwardLeeThompson".toUri()))
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Support the Developer:", fontWeight = FontWeight.Bold)
                Text(
                    text = "Venmo Donation Link",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(Intent.ACTION_VIEW, "https://venmo.com/code?user_id=1857304970395648420".toUri()))
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) { Text("OK") }
        }
    )
}

@Composable
fun GroupList(
    groups: List<AppGroup>,
    onModifyClick: (AppGroup) -> Unit,
    onReorderClick: (AppGroup) -> Unit,
    onDeleteClick: (AppGroup) -> Unit,
    onToggleExpanded: (AppGroup) -> Unit,
    onGroupClick: (AppGroup) -> Unit,
    inShareMode: Boolean,
    packageManager: PackageManager
) {
    LazyColumn {
        items(groups) { group ->
            GroupItem(
                group = group,
                onModifyClick = { onModifyClick(group) },
                onReorderClick = { onReorderClick(group) },
                onDeleteClick = { onDeleteClick(group) },
                onToggleExpanded = { onToggleExpanded(group) },
                onGroupClick = { onGroupClick(group) },
                inShareMode = inShareMode,
                packageManager = packageManager
            )
        }
    }
}

@Composable
fun GroupItem(
    group: AppGroup,
    onModifyClick: () -> Unit,
    onReorderClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onToggleExpanded: () -> Unit,
    onGroupClick: () -> Unit,
    inShareMode: Boolean,
    packageManager: PackageManager
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth().clickable(enabled = inShareMode, onClick = onGroupClick),
        colors = if (inShareMode) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer) else CardDefaults.cardColors()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToggleExpanded) {
                    Icon(
                        imageVector = if (group.isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle"
                    )
                }
                Text(text = group.name, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                if (!inShareMode) {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) { Icon(Icons.Default.MoreVert, null) }
                        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                            DropdownMenuItem(text = { Text("Modify Apps") }, onClick = { menuExpanded = false; onModifyClick() })
                            DropdownMenuItem(text = { Text("Reorder Apps") }, onClick = { menuExpanded = false; onReorderClick() })
                            DropdownMenuItem(text = { Text("Delete Group", color = MaterialTheme.colorScheme.error) }, onClick = { menuExpanded = false; onDeleteClick() })
                        }
                    }
                }
            }
            
            AnimatedVisibility(visible = group.isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    if (group.apps.isEmpty()) {
                        Text(text = "Empty. Add apps via menu.", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 8.dp))
                    } else {
                        group.apps.forEachIndexed { index, app ->
                            AppListItem(app = app, packageManager = packageManager)
                            if (index < group.apps.size - 1) HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppListItem(app: AppInfo, packageManager: PackageManager) {
    val icon = try { packageManager.getApplicationIcon(app.packageName) } catch (_: Exception) { null }
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        if (icon != null) {
            Image(painter = rememberDrawablePainter(icon), contentDescription = null, modifier = Modifier.size(40.dp))
        } else {
            Spacer(modifier = Modifier.size(40.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = app.appName)
    }
}

@Composable
fun CreateGroupDialog(onDismiss: () -> Unit, onCreateGroup: (String) -> Unit) {
    var groupName by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Group") },
        text = {
            OutlinedTextField(value = groupName, onValueChange = { groupName = it }, label = { Text("Group Name") }, singleLine = true)
        },
        confirmButton = {
            Button(onClick = { if (groupName.isNotBlank()) onCreateGroup(groupName) }) { Text("Create") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun ModifyGroupAppsDialog(
    allApps: List<AppInfo>,
    group: AppGroup,
    onDismiss: () -> Unit,
    onSaveApps: (List<AppInfo>) -> Unit,
    onRefresh: () -> Unit,
    packageManager: PackageManager
) {
    val selectedApps = remember { mutableStateListOf<AppInfo>().apply { addAll(group.apps) } }
    var searchQuery by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Modify ${group.name}", modifier = Modifier.weight(1f))
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh App List")
                }
            }
        },
        text = {
            Column {
                OutlinedTextField(value = searchQuery, onValueChange = { searchQuery = it }, label = { Text("Search apps") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.height(300.dp)) {
                    val filteredApps = allApps.filter { it.appName.contains(searchQuery, ignoreCase = true) }
                    items(filteredApps) { app ->
                        val isSelected = selectedApps.any { it.packageName == app.packageName }
                        Row(modifier = Modifier.fillMaxWidth().clickable {
                            if (isSelected) selectedApps.removeAll { it.packageName == app.packageName } else selectedApps.add(app)
                        }.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            val icon = try { packageManager.getApplicationIcon(app.packageName) } catch (_: Exception) { null }
                            if (icon != null) Image(painter = rememberDrawablePainter(icon), contentDescription = null, modifier = Modifier.size(40.dp))
                            else Spacer(modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = app.appName, modifier = Modifier.weight(1f))
                            Checkbox(checked = isSelected, onCheckedChange = null)
                        }
                    }
                }
            }
        },
        confirmButton = { Button(onClick = { onSaveApps(selectedApps.toList()) }) { Text("Save") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun ReorderAppsDialog(
    group: AppGroup,
    onDismiss: () -> Unit,
    onSaveOrder: (List<AppInfo>) -> Unit
) {
    val apps = remember { mutableStateListOf<AppInfo>().apply { addAll(group.apps) } }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reorder Apps (Drag Handle)") },
        text = {
            LazyColumn(modifier = Modifier.height(300.dp)) {
                itemsIndexed(apps, key = { _, app -> app.packageName }) { index, app ->
                    var accumulatedDrag by remember { mutableFloatStateOf(0f) }
                    val itemHeight = 120f
                    
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Menu, 
                            contentDescription = "Drag Handle", 
                            modifier = Modifier
                                .padding(8.dp)
                                .pointerInput(Unit) {
                                    detectVerticalDragGestures(
                                        onDragEnd = { accumulatedDrag = 0f },
                                        onDragCancel = { accumulatedDrag = 0f },
                                        onVerticalDrag = { change, dragAmount ->
                                            change.consume()
                                            accumulatedDrag += dragAmount
                                            if (accumulatedDrag > itemHeight && index < apps.size - 1) {
                                                val item = apps.removeAt(index)
                                                apps.add(index + 1, item)
                                                accumulatedDrag = 0f
                                            } else if (accumulatedDrag < -itemHeight && index > 0) {
                                                val item = apps.removeAt(index)
                                                apps.add(index - 1, item)
                                                accumulatedDrag = 0f
                                            }
                                        }
                                    )
                                }, 
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(text = app.appName, modifier = Modifier.weight(1f))
                    }
                    if (index < apps.size - 1) HorizontalDivider()
                }
            }
        },
        confirmButton = { Button(onClick = { onSaveOrder(apps.toList()) }) { Text("Save Order") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun SortGroupsDialog(
    groups: List<AppGroup>,
    onDismiss: () -> Unit,
    onSaveOrder: (List<AppGroup>) -> Unit
) {
    val sortedGroups = remember { mutableStateListOf<AppGroup>().apply { addAll(groups) } }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sort Groups (Drag Handle)") },
        text = {
            LazyColumn(modifier = Modifier.height(300.dp)) {
                itemsIndexed(sortedGroups, key = { _, group -> group.name }) { index, group ->
                    var accumulatedDrag by remember { mutableFloatStateOf(0f) }
                    val itemHeight = 120f
                    
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Menu, 
                            contentDescription = "Drag Handle", 
                            modifier = Modifier
                                .padding(8.dp)
                                .pointerInput(Unit) {
                                    detectVerticalDragGestures(
                                        onDragEnd = { accumulatedDrag = 0f },
                                        onDragCancel = { accumulatedDrag = 0f },
                                        onVerticalDrag = { change, dragAmount ->
                                            change.consume()
                                            accumulatedDrag += dragAmount
                                            if (accumulatedDrag > itemHeight && index < sortedGroups.size - 1) {
                                                val item = sortedGroups.removeAt(index)
                                                sortedGroups.add(index + 1, item)
                                                accumulatedDrag = 0f
                                            } else if (accumulatedDrag < -itemHeight && index > 0) {
                                                val item = sortedGroups.removeAt(index)
                                                sortedGroups.add(index - 1, item)
                                                accumulatedDrag = 0f
                                            }
                                        }
                                    )
                                }, 
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(text = group.name, modifier = Modifier.weight(1f))
                    }
                    if (index < sortedGroups.size - 1) HorizontalDivider()
                }
            }
        },
        confirmButton = { Button(onClick = { onSaveOrder(sortedGroups.toList()) }) { Text("Save Order") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
