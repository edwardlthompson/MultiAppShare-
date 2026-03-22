package com.multiappshare.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b7\u0018\u00002\u00020\u0001:\u0002\u0003\u0004B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0002\u0005\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/multiappshare/ui/DashboardUiState;", "", "()V", "Loading", "Success", "Lcom/multiappshare/ui/DashboardUiState$Loading;", "Lcom/multiappshare/ui/DashboardUiState$Success;", "feature-dashboard_debug"})
public abstract class DashboardUiState {
    
    private DashboardUiState() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c7\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/multiappshare/ui/DashboardUiState$Loading;", "Lcom/multiappshare/ui/DashboardUiState;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "feature-dashboard_debug"})
    public static final class Loading extends com.multiappshare.ui.DashboardUiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.multiappshare.ui.DashboardUiState.Loading INSTANCE = null;
        
        private Loading() {
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B/\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003\u00a2\u0006\u0002\u0010\tJ\u000f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u000f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003H\u00c6\u0003J\u000f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\u00c6\u0003J9\u0010\u0011\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u00032\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000b\u00a8\u0006\u001a"}, d2 = {"Lcom/multiappshare/ui/DashboardUiState$Success;", "Lcom/multiappshare/ui/DashboardUiState;", "groups", "", "Lcom/multiappshare/model/AppGroup;", "allApps", "Lcom/multiappshare/model/AppInfo;", "history", "Lcom/multiappshare/model/HistoryItem;", "(Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", "getAllApps", "()Ljava/util/List;", "getGroups", "getHistory", "component1", "component2", "component3", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "feature-dashboard_debug"})
    public static final class Success extends com.multiappshare.ui.DashboardUiState {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.multiappshare.model.AppGroup> groups = null;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.multiappshare.model.AppInfo> allApps = null;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.multiappshare.model.HistoryItem> history = null;
        
        public Success(@org.jetbrains.annotations.NotNull()
        java.util.List<com.multiappshare.model.AppGroup> groups, @org.jetbrains.annotations.NotNull()
        java.util.List<com.multiappshare.model.AppInfo> allApps, @org.jetbrains.annotations.NotNull()
        java.util.List<com.multiappshare.model.HistoryItem> history) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.multiappshare.model.AppGroup> getGroups() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.multiappshare.model.AppInfo> getAllApps() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.multiappshare.model.HistoryItem> getHistory() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.multiappshare.model.AppGroup> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.multiappshare.model.AppInfo> component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.multiappshare.model.HistoryItem> component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.multiappshare.ui.DashboardUiState.Success copy(@org.jetbrains.annotations.NotNull()
        java.util.List<com.multiappshare.model.AppGroup> groups, @org.jetbrains.annotations.NotNull()
        java.util.List<com.multiappshare.model.AppInfo> allApps, @org.jetbrains.annotations.NotNull()
        java.util.List<com.multiappshare.model.HistoryItem> history) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}