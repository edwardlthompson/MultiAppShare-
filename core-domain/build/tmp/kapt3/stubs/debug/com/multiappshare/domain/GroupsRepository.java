package com.multiappshare.domain;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0086@\u00a2\u0006\u0002\u0010\fJ\u001c\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/multiappshare/domain/GroupsRepository;", "", "groupDao", "Lcom/multiappshare/data/local/GroupDao;", "context", "Landroid/content/Context;", "(Lcom/multiappshare/data/local/GroupDao;Landroid/content/Context;)V", "file", "Ljava/io/File;", "loadGroups", "", "Lcom/multiappshare/model/AppGroup;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveGroups", "", "groups", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveToJsonBackup", "core-domain_debug"})
public final class GroupsRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.multiappshare.data.local.GroupDao groupDao = null;
    @org.jetbrains.annotations.NotNull()
    private final java.io.File file = null;
    
    public GroupsRepository(@org.jetbrains.annotations.NotNull()
    com.multiappshare.data.local.GroupDao groupDao, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveGroups(@org.jetbrains.annotations.NotNull()
    java.util.List<com.multiappshare.model.AppGroup> groups, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void saveToJsonBackup(java.util.List<com.multiappshare.model.AppGroup> groups) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object loadGroups(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.multiappshare.model.AppGroup>> $completion) {
        return null;
    }
}