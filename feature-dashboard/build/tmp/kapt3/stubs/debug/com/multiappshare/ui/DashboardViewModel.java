package com.multiappshare.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 J\u001c\u0010!\u001a\u00020\u001e2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020$0#2\u0006\u0010%\u001a\u00020\u0011J\u000e\u0010&\u001a\u00020\u001e2\u0006\u0010\'\u001a\u00020(J\u000e\u0010)\u001a\u00020\u001e2\u0006\u0010*\u001a\u00020+J\u000e\u0010,\u001a\u00020\u001e2\u0006\u0010*\u001a\u00020+J\u0006\u0010-\u001a\u00020\u001eJ\u0006\u0010.\u001a\u00020\u001eJ\u000e\u0010/\u001a\u00020\u001e2\u0006\u0010*\u001a\u00020+J\u001c\u00100\u001a\u00020\u001e2\u0006\u0010*\u001a\u00020+2\f\u00101\u001a\b\u0012\u0004\u0012\u00020$0#J\u0014\u00102\u001a\u00020\u001e2\f\u00103\u001a\b\u0012\u0004\u0012\u00020+0#R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R+\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u00118F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000f0\u001a\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001c\u00a8\u00064"}, d2 = {"Lcom/multiappshare/ui/DashboardViewModel;", "Landroidx/lifecycle/ViewModel;", "groupsRepository", "Lcom/multiappshare/domain/GroupsRepository;", "historyRepository", "Lcom/multiappshare/domain/HistoryRepository;", "settingsRepository", "Lcom/multiappshare/domain/SettingsRepository;", "createAutoGroupsUseCase", "Lcom/multiappshare/domain/CreateAutoGroupsUseCase;", "getCompatibleAppsUseCase", "Lcom/multiappshare/domain/GetCompatibleAppsUseCase;", "(Lcom/multiappshare/domain/GroupsRepository;Lcom/multiappshare/domain/HistoryRepository;Lcom/multiappshare/domain/SettingsRepository;Lcom/multiappshare/domain/CreateAutoGroupsUseCase;Lcom/multiappshare/domain/GetCompatibleAppsUseCase;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/multiappshare/ui/DashboardUiState;", "<set-?>", "", "showOnboardingDialog", "getShowOnboardingDialog", "()Z", "setShowOnboardingDialog", "(Z)V", "showOnboardingDialog$delegate", "Landroidx/compose/runtime/MutableState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "addHistoryItem", "", "item", "Lcom/multiappshare/model/HistoryItem;", "autoGroup", "allApps", "", "Lcom/multiappshare/model/AppInfo;", "append", "createGroup", "groupName", "", "deleteGroup", "group", "Lcom/multiappshare/model/AppGroup;", "incrementGroupUsage", "loadData", "setOnboardingDismissed", "toggleGroupExpanded", "updateGroupApps", "apps", "updateGroupsOrder", "groups", "feature-dashboard_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class DashboardViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.multiappshare.domain.GroupsRepository groupsRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.multiappshare.domain.HistoryRepository historyRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.multiappshare.domain.SettingsRepository settingsRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.multiappshare.domain.CreateAutoGroupsUseCase createAutoGroupsUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.multiappshare.domain.GetCompatibleAppsUseCase getCompatibleAppsUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.multiappshare.ui.DashboardUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.multiappshare.ui.DashboardUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState showOnboardingDialog$delegate = null;
    
    @javax.inject.Inject()
    public DashboardViewModel(@org.jetbrains.annotations.NotNull()
    com.multiappshare.domain.GroupsRepository groupsRepository, @org.jetbrains.annotations.NotNull()
    com.multiappshare.domain.HistoryRepository historyRepository, @org.jetbrains.annotations.NotNull()
    com.multiappshare.domain.SettingsRepository settingsRepository, @org.jetbrains.annotations.NotNull()
    com.multiappshare.domain.CreateAutoGroupsUseCase createAutoGroupsUseCase, @org.jetbrains.annotations.NotNull()
    com.multiappshare.domain.GetCompatibleAppsUseCase getCompatibleAppsUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.multiappshare.ui.DashboardUiState> getUiState() {
        return null;
    }
    
    public final boolean getShowOnboardingDialog() {
        return false;
    }
    
    private final void setShowOnboardingDialog(boolean p0) {
    }
    
    public final void loadData() {
    }
    
    public final void setOnboardingDismissed() {
    }
    
    public final void autoGroup(@org.jetbrains.annotations.NotNull()
    java.util.List<com.multiappshare.model.AppInfo> allApps, boolean append) {
    }
    
    public final void createGroup(@org.jetbrains.annotations.NotNull()
    java.lang.String groupName) {
    }
    
    public final void deleteGroup(@org.jetbrains.annotations.NotNull()
    com.multiappshare.model.AppGroup group) {
    }
    
    public final void toggleGroupExpanded(@org.jetbrains.annotations.NotNull()
    com.multiappshare.model.AppGroup group) {
    }
    
    public final void updateGroupApps(@org.jetbrains.annotations.NotNull()
    com.multiappshare.model.AppGroup group, @org.jetbrains.annotations.NotNull()
    java.util.List<com.multiappshare.model.AppInfo> apps) {
    }
    
    public final void incrementGroupUsage(@org.jetbrains.annotations.NotNull()
    com.multiappshare.model.AppGroup group) {
    }
    
    public final void updateGroupsOrder(@org.jetbrains.annotations.NotNull()
    java.util.List<com.multiappshare.model.AppGroup> groups) {
    }
    
    public final void addHistoryItem(@org.jetbrains.annotations.NotNull()
    com.multiappshare.model.HistoryItem item) {
    }
}