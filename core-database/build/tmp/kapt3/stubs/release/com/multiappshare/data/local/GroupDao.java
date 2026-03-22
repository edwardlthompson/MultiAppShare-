package com.multiappshare.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\n\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\u000b\u001a\u00020\u00032\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\bH\u00a7@\u00a2\u0006\u0002\u0010\r\u00a8\u0006\u000e"}, d2 = {"Lcom/multiappshare/data/local/GroupDao;", "", "deleteGroup", "", "group", "Lcom/multiappshare/model/AppGroup;", "(Lcom/multiappshare/model/AppGroup;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllGroups", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertGroup", "insertGroups", "groups", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "core-database_release"})
@androidx.room.Dao()
public abstract interface GroupDao {
    
    @androidx.room.Query(value = "SELECT * FROM groups")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllGroups(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.multiappshare.model.AppGroup>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertGroups(@org.jetbrains.annotations.NotNull()
    java.util.List<com.multiappshare.model.AppGroup> groups, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertGroup(@org.jetbrains.annotations.NotNull()
    com.multiappshare.model.AppGroup group, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteGroup(@org.jetbrains.annotations.NotNull()
    com.multiappshare.model.AppGroup group, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}